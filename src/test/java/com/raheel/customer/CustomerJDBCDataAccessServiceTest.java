package com.raheel.customer;

import com.raheel.AbstractTestcontainers;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();


    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().emailAddress(),
                20
        );

        underTest.insertCustomer(customer);

        List<Customer> customerList = underTest.selectAllCustomers();

        assertThat(customerList).isNotEmpty();


    }

    @Test
    void selectCustomerById() {
        var email  = FAKER.internet().emailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        int id = -1;

        var actual = underTest.selectCustomerById(id);

        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().emailAddress(),
                20
        );

        underTest.insertCustomer(customer);
    }

    @Test
    void existsPersonWithEmail() {
        var email = FAKER.internet().emailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        boolean actual = underTest.existsPersonWithEmail(email);

        assertThat(actual).isTrue();
    }

    @Test
    void deleteCustomer() {
        var email = FAKER.internet().emailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        underTest.deleteCustomer(id);

        var actual = underTest.selectCustomerById(id);

        assertThat(actual).isNotPresent();

    }

    @Test
    void existsPersonWithId() {
        var email = FAKER.internet().emailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        boolean actual = underTest.existsPersonWithId(id);

        assertThat(actual).isTrue();
    }

    @Test
    void updateCustomer() {
        var email  = FAKER.internet().emailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Customer updatedCustomer = new Customer(
                id,
                "Updated Name",
                "updated email",
                21
        );

        underTest.updateCustomer(updatedCustomer);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
           // assertThat(c.getName()).isEqualTo(updatedCustomer.getName());
          //  assertThat(c.getEmail()).isEqualTo(updatedCustomer.getEmail());
            assertThat(c.getAge()).isEqualTo(updatedCustomer.getAge());
        });


    }
}