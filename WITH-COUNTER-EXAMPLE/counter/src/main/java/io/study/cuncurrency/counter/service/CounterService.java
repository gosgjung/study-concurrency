package io.study.cuncurrency.counter.service;

import io.study.cuncurrency.counter.domain.Counter;
import io.study.cuncurrency.counter.repository.CounterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CounterService {

    private final CounterRepository counterRepository;

    public CounterService(CounterRepository counterRepository){
        this.counterRepository = counterRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decrease(Long id, Long diff){
        // get counter
        // counter 감소 연산
        // 결과 저장

        Counter counter = counterRepository.findById(id).orElseThrow();

        counter.decrease(diff);
        System.out.println("after >> counter.cnt = " + counter.getCnt());

        counterRepository.saveAndFlush(counter);
    }
}
