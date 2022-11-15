package io.study.cuncurrency.counter.facade;

import io.study.cuncurrency.counter.service.CounterService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CounterRedissonLockFacade {

    private final CounterService counterService;

    private final RedissonClient redissonClient;

    public CounterRedissonLockFacade(
            CounterService counterService, RedissonClient redissonClient
    ){
        this.counterService = counterService;
        this.redissonClient = redissonClient;
    }

    public void decrease(Long id, Long diff){
        RLock lock = redissonClient.getLock(String.valueOf(id));

        try{
            // 만약 아래의 1초 타임아웃을 주어 락을 얻는 로직이 테스트 실패하면
//            boolean lockResult = lock.tryLock(10, TimeUnit.SECONDS);
            // 아래처럼 타임아웃을 10초의 타임아웃으로 적용해주면 테스트가 실패하지 않는다.
            boolean lockResult = lock.tryLock(10, TimeUnit.SECONDS);

            if(!lockResult){
                System.out.println("Lock 획득 실패");
                return;
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("예외발생 " + e.getMessage());
        } finally {
            counterService.decrease(id, diff);
            lock.unlock();
        }
    }
}
