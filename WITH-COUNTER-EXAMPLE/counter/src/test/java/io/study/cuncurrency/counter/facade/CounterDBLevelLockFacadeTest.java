package io.study.cuncurrency.counter.facade;

import io.study.cuncurrency.counter.domain.Counter;
import io.study.cuncurrency.counter.repository.CounterRepository;
import io.study.cuncurrency.counter.repository.DBLevelLockRepository;
import io.study.cuncurrency.counter.service.CounterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CounterDBLevelLockFacadeTest {

    @Autowired
    private DBLevelLockRepository lockRepository;

    @Autowired
    private CounterRepository counterRepository;

    private CounterService counterService;

    private CounterDBLevelLockFacade facade;

//    @Autowired
//    private CounterDBLevelLockFacade facade;

    @BeforeEach
    public void init(){
        counterService = new CounterService(counterRepository);
        facade = new CounterDBLevelLockFacade(counterService, lockRepository);
        counterRepository.saveAndFlush(new Counter(1L, 100L));
    }

    @AfterEach
    public void deleteAll(){
        counterRepository.deleteAll();
    }

    @Test
    public void 멀티스레드환경에서_100개의_요청을_동기화하여_수행() throws InterruptedException {
        int MAX_COUNTING = 100;
        System.out.println("*** core = " + Runtime.getRuntime().availableProcessors());
        ExecutorService service = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()/2);

        CountDownLatch latch = new CountDownLatch(MAX_COUNTING);

        for(int i=0; i<MAX_COUNTING; i++){
            service.submit(()->{
                try{
                    facade.decrease(1L, 1L, 9000L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Counter counter = counterRepository.findById(1L).orElseThrow();
        assertThat(counter.getCnt())
                .isEqualTo(0L);
    }

}
