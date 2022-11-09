package io.study.cuncurrency.counter.service;

import io.study.cuncurrency.counter.domain.Counter;
import io.study.cuncurrency.counter.repository.CounterRepository;
import org.springframework.stereotype.Service;

@Service
public class CounterService {

    private final CounterRepository counterRepository;

    public CounterService(CounterRepository counterRepository){
        this.counterRepository = counterRepository;
    }

    public void decrease(Long id, Long diff){
        // get counter
        // counter 감소 연산
        // 결과 저장

        Counter counter = counterRepository.findById(id).orElseThrow();

        counter.decrease(diff);

        counterRepository.saveAndFlush(counter);
    }
}
