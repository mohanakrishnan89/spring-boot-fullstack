package com.amigoscode.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{
    private static List<Customer> customers;

    static {
        customers = new ArrayList<Customer>();
        Customer alex = new Customer(1L,"Alex","Alex@gmail.com",21);
        Customer anita = new Customer(2L,"Anita","Anita@gmail.com",19);
        Customer mona = new Customer(3L,"Mona","mona@gmail.com",19);
        customers.add(alex);
        customers.add(anita);
        customers.add(mona);
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        return customers.stream().filter(c -> c.getId().equals(customerId))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean isEmailExists(String email) {
        return customers.stream().anyMatch(c -> c.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public boolean isPersonIdExists(Integer id) {
        return customers.stream().anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customers.stream().filter(c -> c.getId().equals(id)).findFirst().ifPresent(customers::remove);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.add(customer);
    }
}
