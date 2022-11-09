package io.study.cuncurrency.counter.repository;

import io.study.cuncurrency.counter.domain.Counter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository<Counter, Long> {
}
