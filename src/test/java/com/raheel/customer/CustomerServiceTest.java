package com.raheel.customer;

import com.raheel.exception.BadRequestException;
import com.raheel.exception.DuplicateResourceException;
import com.raheel.exception.ResourceNotFound;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;

    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        underTest.getAllCustomers();
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        int id = 1;

        Customer customer = new Customer(id, "alex", "alex@gmail.com", 19);

        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        Customer actual = underTest.getCustomer(id);

        assertThat(actual).isEqualTo(customer);

    }

    @Test
    void willThrowWhenGetCustomerReturnsEmptyOptional() {
        int id = 1;
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessageContaining("customer with id [%s] not found".formatted(id));


    }

    @Test
    void addCustomerWithValidEmail() {
        int id = 1;
        String email = "testemail@gmail.com";
        CustomerRegistrationRequest customer = new CustomerRegistrationRequest("alex", email, 19);

        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        underTest.addCustomer(customer);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(customer.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.age());
    }

    @Test
    void willThrowWhenAddCustomerWithEmailAlreadyExists() {
        int id = 1;
        String email = "testemail@gmail.com";
        CustomerRegistrationRequest customer = new CustomerRegistrationRequest("alex", email, 19);

        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> underTest.addCustomer(customer))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email already exists");
    }



    @Test
    void deleteCustomer() {
        int id = 1;

        Mockito.when(customerDao.existsPersonWithId(id)).thenReturn(true);

        underTest.deleteCustomer(id);

        verify(customerDao).deleteCustomer(id);
    }

    @Test
    void willThrowWhenDeletedCustomerWithIdDoesNotExist() {
        int id = 1;

        Mockito.when(customerDao.existsPersonWithId(id)).thenReturn(false);

        assertThatThrownBy(() -> underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessageContaining("Cannot find customer with that id");
    }

    @Test
    void updateCustomer() {
        int id = 1;

        Customer customer = new Customer(id, "alex", "alex@gmail.com", 19);

        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("Bobby", "Bobby@gmail.com", 20);

        underTest.updateCustomer(id, customerUpdateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customerUpdateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerUpdateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customerUpdateRequest.age());
    }

    @Test
    void willThrowWhenUpdateCustomerExists() {
        int id = 1;

        Customer customer = new Customer(id, "alex", "alex@gmail.com", 19);

        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("alex", "alex@gmail.com", 19);

        assertThatThrownBy(() -> underTest.updateCustomer(id,customerUpdateRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Customer already exists");
    }
}