package com.raheel.customer;

import org.junit.jupiter.api.Test;


import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {


    @Test
    void mapRow() throws SQLException {
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();

        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("age")).thenReturn(19);
        when(resultSet.getString("name")).thenReturn("Test");
        when(resultSet.getString("email")).thenReturn("test@gmail.com");


        Customer actual = customerRowMapper.mapRow(resultSet,1);

        Customer expected = new Customer(1, "Test", "test@gmail.com", 19);

        assertThat(actual).isEqualTo(expected);


    }
}