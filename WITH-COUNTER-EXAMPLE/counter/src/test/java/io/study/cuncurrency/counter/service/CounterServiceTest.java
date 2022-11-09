package io.study.cuncurrency.counter.service;

import io.study.cuncurrency.counter.domain.Counter;
import io.study.cuncurrency.counter.repository.CounterRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class CounterServiceTest {

    @Autowired
    private CounterService counterService;

    @Autowired
    private CounterRepository counterRepository;

    @BeforeEach
    public void before(){
        Counter counter = new Counter(1L, 100L);
        counterRepository.saveAndFlush(counter);
    }

    @AfterEach
    public void after(){
        counterRepository.deleteAll();
    }

    @Test
    public void 카운터_감소연산_테스트(){
        counterService.decrease(1L, 1L);

        Counter counter = counterRepository.findById(1L).orElseThrow();

        Assertions.assertThat(99L).isEqualTo(counter.getCnt());
    }

    @Test
    public void 테스트_실패하는_예제__100개의_요청을_비동기_스레드로_동시성프로그래밍_처리() throws InterruptedException {
        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors());

        CountDownLatch latch = new CountDownLatch(threadCount); // 100 개의 스레드의 종료를 기다려야 한다.

        for(int i=0; i<threadCount; i++){
            executorService.submit(()->{
                try{
                    counterService.decrease(1L, 1L);
                }
                finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Counter counter = counterRepository.findById(1L).orElseThrow();
        Assertions.assertThat(counter.getCnt()).isEqualTo(0L);
    }
}
