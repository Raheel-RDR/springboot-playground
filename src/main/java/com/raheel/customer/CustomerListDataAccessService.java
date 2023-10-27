package com.raheel.customer;

import com.raheel.exception.BadRequestException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {

    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer Alex = new Customer(1,"Alex", "email@email.com",24);
        Customer Danny = new Customer(2,"Danny", "danny@email.com",18);

        customers.add(Alex);
        customers.add(Danny);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream().filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream().anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public void deleteCustomer(Integer id) {
        customers.remove(customers.stream().filter(c -> c.getId().equals(id)).findFirst());
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        return customers.stream().anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void updateCustomer(Customer updatedCustomer) {
       customers.add(updatedCustomer);
    }
}
