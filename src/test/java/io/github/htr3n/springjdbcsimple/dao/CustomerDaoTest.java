package io.github.htr3n.springjdbcsimple.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.htr3n.springjdbcsimple.entity.Customer;

@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan
public class CustomerDaoTest {

    @Autowired
    private CustomerDao customerDao;

    @Test
    public void create_shouldReturnValidCustomer_whenAddingNewCustomer() {
        Customer alice = new Customer();
        alice.setEmail("alice@test.com");
        alice.setName("Alice");
        customerDao.create(alice);
        assertNotNull(alice.getId());
        
        Optional<Customer> result = customerDao.findById(alice.getId());
        assertTrue(result.isPresent());
        assertEquals(alice.getName(), result.get().getName());
        assertEquals(alice.getEmail(), result.get().getEmail());
    }

    @Test
    public void findById_shouldReturnInvalidCustomer_forEmptyDatabase() {
        Optional<Customer> invalidCustomer = customerDao.findById(new Random().nextInt());
        assertFalse(invalidCustomer.isPresent());    	
    }

    @Test
    public void findById_shouldReturnValidCustomer_forExistingCustomer() {
        final Customer alice = new Customer();
        alice.setEmail("alice@test.com");
        alice.setName("Alice");
        customerDao.create(alice);

        Optional<Customer> validCustomer = customerDao.findById(alice.getId());
        assertTrue(validCustomer.isPresent());
        assertEquals(alice.getName(), validCustomer.get().getName());
        assertEquals(alice.getEmail(), validCustomer.get().getEmail());
    }

    @Test
    public void findAll_shouldYieldEmptyList_forEmptyDatabase() {
        List<Customer> noCustomers = customerDao.findAll();
        assertNotNull(noCustomers);
        assertEquals(0, noCustomers.size());
    }
    
    @Test
    public void findAll_shouldReturnCorrectListOfCustomers_forNonemptyDatabase() {
    	final int ONE_CUSTOMER = 1;
    	final int TWO_CUSTOMERS = 2;
    	
        Customer alice = new Customer();
        alice.setEmail("alice@test.com");
        alice.setName("Alice");
        customerDao.create(alice);

        List<Customer> customers = customerDao.findAll();
        assertNotNull(customers);
        assertEquals(ONE_CUSTOMER, customers.size());

        Customer result = customers.get(0);
        assertEquals(alice.getName(), result.getName());
        assertEquals(alice.getEmail(), result.getEmail());

        Customer bob = new Customer();
        bob.setEmail("bob@test.com");
        bob.setName("Bob");
        customerDao.create(bob);

        customers = customerDao.findAll();
        assertNotNull(customers);
        assertEquals(TWO_CUSTOMERS, customers.size());
    }

    @Test
    public void update_shouldYieldFalse_forEmptyDatabase() {
        Customer notFound = new Customer();        
        notFound.setEmail("nonexisting@test.com");
        notFound.setName("Non-existing");
        notFound.setId(new Random().nextInt());
        assertFalse(customerDao.update(notFound));    	
    }

    @Test
    public void update_shouldYieldTrue_forExistingCustomer() {
        Customer customer = new Customer();
        customer.setEmail("alice@test.com");
        customer.setName("Alice");
        customerDao.create(customer);

        assertNotNull(customer.getId());
        assertTrue(customerDao.update(customer));

        customer.setName("Bob");
        customer.setEmail("bob@test.com");
        assertTrue(customerDao.update(customer));

        Optional<Customer> found = customerDao.findById(customer.getId());
        assertTrue(found.isPresent());
        assertEquals(customer.getName(), found.get().getName());
        assertEquals(customer.getEmail(), found.get().getEmail());
    }

    @Test
    public void delete_shouldYieldFalse_forEmptyDatabaseOrNonexistingCustomer() {
    	assertFalse(customerDao.delete(new Random().nextInt()));
    }

    @Test
    public void delete_shouldYieldTrue_forExistingCustomer() {
        Customer alice = new Customer();
        alice.setEmail("alice@test.com");
        alice.setName("Alice");
        customerDao.create(alice);

        assertEquals(1, customerDao.findAll().size());
        assertTrue(customerDao.delete(alice.getId()));
        assertFalse(customerDao.findById(alice.getId()).isPresent());
        assertEquals(0, customerDao.findAll().size());
    }
}
