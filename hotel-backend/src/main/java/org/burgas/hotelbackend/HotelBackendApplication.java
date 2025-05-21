package org.burgas.hotelbackend;

import org.burgas.hotelbackend.filter.EmployeeWebFilter;
import org.burgas.hotelbackend.filter.IdentityWebFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ServletComponentScan(
        basePackageClasses = {
                IdentityWebFilter.class, EmployeeWebFilter.class
        }
)
public class HotelBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelBackendApplication.class, args);
    }

}
