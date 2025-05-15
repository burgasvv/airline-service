package org.burgas.flightbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.Executors;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class FlightBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightBackendApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AsyncTaskExecutor taskExecutor() {
        return new TaskExecutorAdapter(Executors.newCachedThreadPool());
    }
}
