package com.amigoscode.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        //Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getInt("age")).thenReturn(35);
        when(resultSet.getString("name")).thenReturn("Mohana");
        when(resultSet.getString("email")).thenReturn("mohana@gmail.com");


        //When
        Customer actual = customerRowMapper.mapRow(resultSet,1);
        //Then
        Customer expected = new Customer(
                1L,"Mohana","mohana@gmail.com",35
        );
        assertThat(actual).isEqualTo(expected);
    }
}