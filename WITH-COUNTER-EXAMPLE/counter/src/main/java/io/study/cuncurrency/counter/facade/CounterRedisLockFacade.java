package io.study.cuncurrency.counter.facade;

import io.study.cuncurrency.counter.repository.RedisLockRepository;
import io.study.cuncurrency.counter.service.CounterService;
import org.springframework.stereotype.Component;

@Component
public class CounterRedisLockFacade {

    private final CounterService counterService;

    private final RedisLockRepository redisLockRepository;

    public CounterRedisLockFacade(CounterService counterService, RedisLockRepository redisLockRepository){
        this.counterService = counterService;
        this.redisLockRepository = redisLockRepository;
    }

    public void decrease(Long id, Long diff){
        while(!redisLockRepository.lock(id)){
            try{
                Thread.sleep(300);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        counterService.decrease(id, diff);
        redisLockRepository.unlock(id);
    }
}
