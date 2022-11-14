package io.study.cuncurrency.counter.facade;

import io.study.cuncurrency.counter.repository.DBLevelLockRepository;
import io.study.cuncurrency.counter.service.CounterService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CounterDBLevelLockFacade {

    private final CounterService counterService;

    private final DBLevelLockRepository dbLevelLockRepository;

    public CounterDBLevelLockFacade(CounterService counterService, DBLevelLockRepository lockRepository){
        this.counterService = counterService;
        this.dbLevelLockRepository = lockRepository;
    }

    @Transactional
    public void decrease(Long id, Long diff, Long timeout){
        try{
            System.out.println(">>> id = " + String.valueOf(id));
            dbLevelLockRepository.getLock(String.valueOf(id), timeout);
            counterService.decrease(id, diff);
        } finally {
            dbLevelLockRepository.releaseLock(String.valueOf(id));
        }
    }

}
