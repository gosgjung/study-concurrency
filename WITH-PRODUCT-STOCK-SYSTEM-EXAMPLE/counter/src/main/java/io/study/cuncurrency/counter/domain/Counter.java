package io.study.cuncurrency.counter.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Counter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private Long cnt;

    public Counter(){}

    public Counter(Long id, Long cnt){
        this.id = id;
        this.cnt = cnt;
    }

    public void decrease(Long diff){
        if(this.cnt < 0){
            throw new RuntimeException("카운터를 이미 넘어섰습니다... ");
        }

        this.cnt = this.cnt - diff;
    }
}