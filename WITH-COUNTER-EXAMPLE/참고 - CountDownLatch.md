# 참고 - CountDownLatch 

## 1) 단순 예제

어떤 스레드가 다른 스레드에서 작업이 완료될 때 까지 기다릴 수 있도록 해주는 클래스.

뭔가 예를 들자면 아래와 같은 예제가 있다.<br>

설명은 일주일 뒤 쯤 돌아와서 정리할 수 있을 듯 하다.<br>

일단은 단순 예제만을 정리해보면 아래와 같다.<br>

<br>

```java
// ...

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

```

<br>

출력결과

```plain
(before) [메인 스레드] 스레드 id = 1
(after) [메인 스레드] 스레드 id = 1
count down, thread id = 26
count down, thread id = 25
count down, thread id = 27
(finished) [메인 스레드] 스레드 id = 1

Process finished with exit code 0
```

<br>

## 2) ExecutorService

위의 예제는 단순히 스레드를 생성해서 기다리는 예제인데, 실제로는 ExecutorService 로 ThreadPool을 만들어서 스레드를 재활용하면서 원하는 카운트가 소진될때까지 기다리는 코드를 작성하기도 한다.<br>

<br>

위의 예제처럼 필요할 때마다 스레드를 생성해버리면, 컴퓨터가 힘들어한다 ㅠㅠ<br>

<br>

예제는 시간날 때 추가 예정





