package com.korit.authstudy.init;

import com.korit.authstudy.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

// init 자체가 서버를 열 때, 무조건 한 번 실행되는 것들이다.
// 초기값을 넣어줄 때나 초기에 설정값이 있을 때 사용된다. (main과 같은 역할을 한다.)

@RequiredArgsConstructor
class OptionalStudy<T> {

    private final T present;

    public void ifPresenetOrElse(Consumer<T> action, Runnable runnable) {
        if (present != null) {
            action.accept(present);
        } else {
            runnable.run();
        }
    }
}

@Component
public class Lambda implements CommandLineRunner {
    // Spring Boot가 실행되면, 서버가 실행되면 무조건 처음에 한번 실행된다.

    @Override
    public void run(String... args) throws Exception {
        User user = User.builder()
                .id(100)
                .username("test")
                .password("1234")
                .build();
        OptionalStudy<User> optionalStudy = new OptionalStudy<>(user);
        Consumer<User> consumer = new Consumer<User>() {
            @Override
            public void accept(User user) {
                System.out.println("user 객체 찾음 : " + user);
            }
        };
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("user 객체를 몿 찾아서 여기서 다른 작업을 해줄거임.");
            }
        };
        optionalStudy.ifPresenetOrElse(consumer, runnable);

        // 위를 Lambda로 한다면?
        Consumer<User> consumerLambda = (u) -> {
            System.out.println("user객체 찾음 : " + user);
        };
        Runnable runnableLambda= () -> {
            System.out.println("user 객체를 몿 찾아서 여기서 다른 작업을 해줄거임.");
        };
        optionalStudy.ifPresenetOrElse(consumerLambda, runnableLambda);

        // 이걸 더 쉽게 사용한다면?
        optionalStudy.ifPresenetOrElse(
                (u) -> {
                    System.out.println("user객체 찾음 : " + user);
                },
                () -> {
                    System.out.println("user 객체를 몿 찾아서 여기서 다른 작업을 해줄거임.");
                }
        );
    }
}
