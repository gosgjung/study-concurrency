package io.study.cuncurrency.counter.facade;

import io.study.cuncurrency.counter.domain.Counter;
import io.study.cuncurrency.counter.repository.CounterRepository;
import io.study.cuncurrency.counter.repository.RedisLockRepository;
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
public class CounterRedisLockFacadeTest {

    @Autowired
    private CounterService counterService;

    @Autowired
    private RedisLockRepository redisLockRepository;

    @Autowired
    private CounterRepository counterRepository;

//    private final CounterRedisLockFacade counterRedisLockFacade =
//            new CounterRedisLockFacade(counterService, redisLockRepository);

    @Autowired
    private CounterRedisLockFacade counterRedisLockFacade;


    @BeforeEach
    public void init(){
        counterRepository.saveAndFlush(new Counter(1L, 100L));
    }

    @AfterEach
    public void close(){
        counterRepository.deleteAll();
    }

    @Test
    public void 레디스_락을_이용한_동시성_처리() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        final int MAX = 100;
        CountDownLatch latch = new CountDownLatch(MAX);

        // 키가 존재하는지를 검색해서 키가 없거나 삭제된 상태에서만 락을 획득할 수 있다.
        for(int i=0; i<MAX; i++){
            executorService.submit(()->{
                try{
                    counterRedisLockFacade.decrease(1L, 1L);
                    latch.countDown();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            });
        }

        latch.await();

        Counter counter = counterRepository.findById(1L).orElseThrow();

        assertThat(counter.getCnt()).isEqualTo(0);
    }

}
