package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        //When
            underTest.getAllCustomers();
        //Then
            verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //Given
        long id = 10;
        Customer customer = new Customer(
                id,"Mohan","mohan@gmail.com",20,
                Gender.MALE);
        when(customerDao.selectCustomerById((int)id)).thenReturn(Optional.of(customer));
        //When
        Customer actual = underTest.getCustomerById(10);
        //Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnsEmptyOptional() {
        //Given
        long id = 10;
        when(customerDao.selectCustomerById((int)id)).thenReturn(Optional.empty());
        //When
        //Then
        assertThatThrownBy(() -> underTest.getCustomerById((int)id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] not found".formatted(id));

    }

    @Test
    void addCustomer() {
        //Given
        String email = "mohan@gmail.com";
        when(customerDao.isEmailExists(email)).thenReturn(false);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            "Mohan",email,35,Gender.MALE
                );
        //When
        underTest.addCustomer(request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowIfEmailExistsWhileAddingCustomer() {
        //Given
        String email = "mohan@gmail.com";
        when(customerDao.isEmailExists(email)).thenReturn(true);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Mohan",email,35,Gender.MALE
        );
        //When
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email id [%s] already taken".formatted(email));
        //Then
        verify(customerDao,never()).insertCustomer(any());

    }

    @Test
    void deleteCustomer() {
        //Given
        long id = 10;
        when(customerDao.isPersonIdExists((int) id)).thenReturn(true);
        //When
        underTest.deleteCustomer((int)id);
        //Then
        verify(customerDao).deleteCustomerById((int) id);
    }

    @Test
    void willThrowWhenDeleteCustomerIdNotFound() {
        //Given
        long id = 10;
        when(customerDao.isPersonIdExists((int) id)).thenReturn(false);
        //When
        assertThatThrownBy(() -> underTest.deleteCustomer((int) id))
                .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage("Customer with id [%s] not found".formatted((int) id));
        //Then
        verify(customerDao, never()).deleteCustomerById((int) id);
    }

    @Test
    void canUpdateAllCustomerProperties() {
        //Given
        long id = 10;
        Customer customer = new Customer(
                id,"Mohan","mohan@gmail.com",20,
                Gender.MALE);
        when(customerDao.selectCustomerById((int)id)).thenReturn(Optional.of(customer));
        String newEmail = "Mohana@amigoscode.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Mohana", newEmail,34
        );

        when(customerDao.isEmailExists(newEmail)).thenReturn(false);
        //When
        underTest.updateCustomer((int)id,updateRequest);
        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor =  ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        //Given
        long id = 10;
        Customer customer = new Customer(
                id,"Mohan","mohan@gmail.com",20,
                Gender.MALE);
        when(customerDao.selectCustomerById((int)id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Mohana", null,null
        );

        //When
        underTest.updateCustomer((int)id,updateRequest);
        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor =  ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        //Given
        long id = 10;
        Customer customer = new Customer(
                id,"Mohan","mohan@gmail.com",20,
                Gender.MALE);
        when(customerDao.selectCustomerById((int)id)).thenReturn(Optional.of(customer));
        String newEmail = "Mohana@amigoscode.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, newEmail,null
        );
        when(customerDao.isEmailExists(newEmail)).thenReturn(false);
        //When
        underTest.updateCustomer((int)id,updateRequest);
        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor =  ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        //Given
        long id = 10;
        Customer customer = new Customer(
                id,"Mohan","mohan@gmail.com",20,
                Gender.MALE);
        when(customerDao.selectCustomerById((int)id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, null,23
        );
        //When
        underTest.updateCustomer((int)id,updateRequest);
        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor =  ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void willThrowWhenUpdateCustomerEmailAlreadyTaken() {
        //Given
        long id = 10;
        Customer customer = new Customer(
                id,"Mohan","mohan@gmail.com",20,
                Gender.MALE);
        when(customerDao.selectCustomerById((int)id)).thenReturn(Optional.of(customer));
        String newEmail = "Mohana@amigoscode.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, newEmail,null
        );
        when(customerDao.isEmailExists(newEmail)).thenReturn(true);
        //When
        assertThatThrownBy(() -> underTest.updateCustomer((int)id,updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                        .hasMessage("email already taken");
        //Then

        verify(customerDao, never()).updateCustomer(any());

    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        // Given
        long id = 10;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19,
                Gender.MALE);
        when(customerDao.selectCustomerById((int)id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge());

        // When
        assertThatThrownBy(() -> underTest.updateCustomer((int)id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        // Then
        verify(customerDao, never()).updateCustomer(any());
    }
}