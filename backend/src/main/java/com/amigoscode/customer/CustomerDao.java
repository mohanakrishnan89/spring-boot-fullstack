package com.amigoscode.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {

    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);
    void insertCustomer(Customer customer);
    boolean isEmailExists(String email);
    boolean isPersonIdExists(Integer id);
    void deleteCustomerById(Integer id);
    void updateCustomer(Customer customer);

}
