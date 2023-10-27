package com.raheel.customer;

import com.raheel.exception.BadRequestException;
import com.raheel.exception.DuplicateResourceException;
import com.raheel.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {

        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {

        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFound("customer with id [%s] not found".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        String email = customerRegistrationRequest.email();

        if(customerDao.existsPersonWithEmail(email))
            throw new DuplicateResourceException("email already exists");

        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()

        );
        customerDao.insertCustomer(customer);

    }

    public void deleteCustomer(Integer id) {

        if(!customerDao.existsPersonWithId(id))
            throw new ResourceNotFound("Cannot find customer with that id");

        customerDao.deleteCustomer(id);
    }

    public void updateCustomer(Integer id, CustomerUpdateRequest updateRequest) {
        Customer customer = getCustomer(id);

        if(
                Objects.equals(customer.getEmail(), updateRequest.email()) ||
                Objects.equals(customer.getName(), updateRequest.name()) ||
                Objects.equals(customer.getAge(), updateRequest.age())
        )
            throw new BadRequestException("Customer already exists");

        customer.setName(updateRequest.name());
        customer.setAge(updateRequest.age());
        customer.setEmail(updateRequest.email());

        customerDao.updateCustomer(customer);


    }
}
