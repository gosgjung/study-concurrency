package io.study.cuncurrency.counter.facade;

import io.study.cuncurrency.counter.domain.Counter;
import io.study.cuncurrency.counter.repository.CounterRepository;
import io.study.cuncurrency.counter.service.CounterService;
import org.assertj.core.api.Assertions;
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
public class CounterRedissonLockFacadeTest {

    @Autowired
    private CounterRepository counterRepository;

    @Autowired
    private CounterService counterService;

    @Autowired
    private CounterRedissonLockFacade counterRedissonLockFacade;

    @BeforeEach
    public void init(){
        counterRepository.saveAndFlush(new Counter(1L, 100L));
    }

    @AfterEach
    public void close(){
        counterRepository.deleteAll();
    }

    @Test
    public void 동시성환경에서_Redisson_을_이용한_락_및_데이터_동기화() throws InterruptedException {
        final int MAX = 100;
        CountDownLatch latch = new CountDownLatch(MAX);

        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());

        for(int i=0; i<MAX; i++){
            executorService.submit(()->{
                try{
                    counterRedissonLockFacade.decrease(1L, 1L);
                    latch.countDown();
                } catch (Exception e){
                    e.printStackTrace();
                }
            });
        }

        latch.await();

        Counter counter = counterRepository.findById(1L).orElseThrow();
        assertThat(counter.getCnt()).isEqualTo(0L);
    }
}
