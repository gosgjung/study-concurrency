package io.study.cuncurrency.counter.repository;

import io.study.cuncurrency.counter.domain.Counter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DBLevelLockRepository extends JpaRepository<Counter, Long> {

    @Query(value = "select get_lock(:key, :timeout)", nativeQuery = true)
    void getLock(String key, Long timeout);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(String key);

}
