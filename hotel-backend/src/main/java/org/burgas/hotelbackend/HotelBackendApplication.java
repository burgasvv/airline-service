package org.burgas.hotelbackend;

import org.burgas.hotelbackend.filter.IdentityWebFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.Executors;

@SpringBootApplication
@ServletComponentScan(
        basePackageClasses = {
                IdentityWebFilter.class
        }
)
public class HotelBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelBackendApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AsyncTaskExecutor taskExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }
}
