package com.raheel.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        underTest.selectAllCustomers();

        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        int id = 1;

        underTest.selectCustomerById(id);

        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        int id = 1;
        Customer customer = new Customer(id,"test","test@email.com", 20);

        underTest.insertCustomer(customer);

        verify(customerRepository).save(customer);

    }

    @Test
    void existsPersonWithEmail() {
        String email = "testemail@gmail.com";

        underTest.existsPersonWithEmail(email);

        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void deleteCustomer() {
        int id = 1;

        underTest.deleteCustomer(id);

        verify(customerRepository).deleteById(id);
    }

    @Test
    void existsPersonWithId() {
        int id = 1;

        underTest.existsPersonWithId(id);

        verify(customerRepository).existsById(id);
    }

    @Test
    void updateCustomer() {
        int id = 1;
        Customer customer = new Customer(id,"test","test@email.com", 20);

        underTest.updateCustomer(customer);

        verify(customerRepository).save(customer);
    }
}