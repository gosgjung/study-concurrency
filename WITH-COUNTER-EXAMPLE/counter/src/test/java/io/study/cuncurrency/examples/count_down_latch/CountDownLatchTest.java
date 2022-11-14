package io.study.cuncurrency.examples.count_down_latch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CountDownLatchTest {

    @Test
    public void CountDownLatch_를_사용하는_예제() throws InterruptedException {
        final int MAX_CNT = 3;
        CountDownLatch latch = new CountDownLatch(MAX_CNT);

        List<Thread> collect = Stream
                .generate(() -> new Thread(new SimpleTask(latch)))
                .limit(MAX_CNT)
                .collect(Collectors.toList());

        System.out.println("(before) [메인 스레드] 스레드 id = " + Thread.currentThread().getId());

        collect.forEach(thread -> thread.start());

        System.out.println("(after) [메인 스레드] 스레드 id = " + Thread.currentThread().getId());

        latch.await(5L, TimeUnit.SECONDS);

        System.out.println("(finished) [메인 스레드] 스레드 id = " + Thread.currentThread().getId());
    }

    class SimpleTask implements Runnable{
        private final CountDownLatch latch;

        public SimpleTask(CountDownLatch latch){
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println("count down, thread id = " + Thread.currentThread().getId());
            latch.countDown();
        }
    }
}
