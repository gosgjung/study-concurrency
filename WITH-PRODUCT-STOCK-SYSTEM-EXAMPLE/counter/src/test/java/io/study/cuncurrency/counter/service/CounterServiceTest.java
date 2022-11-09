package io.study.cuncurrency.counter.service;

import io.study.cuncurrency.counter.domain.Counter;
import io.study.cuncurrency.counter.repository.CounterRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
