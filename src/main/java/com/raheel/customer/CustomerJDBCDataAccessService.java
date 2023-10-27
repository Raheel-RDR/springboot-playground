package com.raheel.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT * FROM customer
                """;

        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        var sql = """
                SELECT *
                FROM customer
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, age) 
                VALUES (?, ?, ?)
                """;
        int result = jdbcTemplate.update(sql,
                customer.getName(), customer.getEmail(), customer.getAge());

        System.out.println("jdbcTemplate.update = " + result);

    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql = """
                SELECT COUNT(*) 
                FROM customer
                WHERE email = ?
                """;

        int count = jdbcTemplate.queryForObject(sql, Integer.class, email);

        return (count > 0);

    }

    @Override
    public void deleteCustomer(Integer id) {

        var sql = """
                DELETE
                FROM customer
                WHERE id = ?
                """;

        int result = jdbcTemplate.update(sql, id);

        System.out.println("jdbcTemplate.update DELETE = " + result);
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        var sql = """
                SELECT COUNT(*) 
                FROM customer
                WHERE id = ?
                """;

        int count = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return (count > 0);
    }

    @Override
    public void updateCustomer(Customer update) {

        var sql = """
                UPDATE customer
                SET name = ?, email = ?, age = ?
                WHERE id = ?
                """;

        int result = jdbcTemplate.update(sql,
                update.getName(),
                update.getEmail(),
                update.getAge(),
                update.getId()
                );

        System.out.println("jdbcTemplate.update UPDATE = " + result);

    }
}
