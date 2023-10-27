package com.raheel;

import com.github.javafaker.Faker;
import com.raheel.customer.Customer;
import com.raheel.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner (CustomerRepository customerRepository) {
        Faker faker = new Faker();

        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName.toLowerCase() + "@gmail.com";
        Integer age = faker.number().numberBetween(1,100);
        return args -> {
            Customer customer = new Customer(firstName + " " + lastName, email,age);
            customerRepository.save(customer);

        };
    }
}
