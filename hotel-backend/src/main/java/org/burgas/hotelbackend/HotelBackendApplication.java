package org.burgas.hotelbackend;

import org.burgas.hotelbackend.filter.IdentityWebFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.Executor;

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
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setVirtualThreads(true);
        threadPoolTaskExecutor.setQueueCapacity(1200);
        threadPoolTaskExecutor.setCorePoolSize(2);
        threadPoolTaskExecutor.setMaxPoolSize(12);
        return threadPoolTaskExecutor;
    }
}
