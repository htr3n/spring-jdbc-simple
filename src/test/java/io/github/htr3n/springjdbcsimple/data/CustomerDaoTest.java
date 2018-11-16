package io.github.htr3n.springjdbcsimple.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerDaoTest {

    @Autowired
    private CustomerDao customerDao;

    @Test
    @Transactional
    @Rollback
    public void testCreate() {
        Customer alice = new Customer();
        alice.setEmail("alice@test.com");
        alice.setName("Alice");
        customerDao.create(alice);
        // the newly created and stored customer must return a valid id
        assertNotNull(alice.getId());

        // look up and retrieve
        Customer result = customerDao.findCustomerById(alice.getId());
        // then compare
        assertNotNull(result);
        assertEquals(alice.getName(), result.getName());
        assertEquals(alice.getEmail(), result.getEmail());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindCustomerById() {
        // test an invalid case: no customer in the DB
        Customer invalidCustomer = customerDao.findCustomerById(new Random().nextInt());
        assertNull(invalidCustomer);

        // now insert a customer
        Customer alice = new Customer();
        alice.setEmail("alice@test.com");
        alice.setName("Alice");
        customerDao.create(alice);

        // and look up that customer
        Customer result = customerDao.findCustomerById(alice.getId());
        assertNotNull(result);
        assertEquals(alice.getName(), result.getName());
        assertEquals(alice.getEmail(), result.getEmail());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAll() {
        // test against an empty DB
        // where we should retrieve a non-null empty list
        List<Customer> noCustomers = customerDao.findAll();
        assertNotNull(noCustomers);
        assertEquals(0, noCustomers.size());

        // first customer
        Customer alice = new Customer();
        alice.setEmail("alice@test.com");
        alice.setName("Alice");
        customerDao.create(alice);

        List<Customer> oneCustomer = customerDao.findAll();
        assertNotNull(oneCustomer);
        assertEquals(1, oneCustomer.size());

        // compare for exact result
        Customer result = oneCustomer.get(0);
        assertEquals(alice.getName(), result.getName());
        assertEquals(alice.getEmail(), result.getEmail());

        // second customer
        Customer bob = new Customer();
        bob.setEmail("bob@test.com");
        bob.setName("Bob");
        customerDao.create(bob);

        List<Customer> twoCustomer = customerDao.findAll();
        assertNotNull(twoCustomer);
        assertEquals(2, twoCustomer.size());

        // third customer
        Customer joe = new Customer();
        joe.setEmail("joe@test.com");
        joe.setName("Joe");
        customerDao.create(joe);

        List<Customer> threeCustomer = customerDao.findAll();
        assertNotNull(threeCustomer);
        assertEquals(3, threeCustomer.size());
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdate() {
        // create and store a customer
        Customer customer = new Customer();
        customer.setEmail("alice@test.com");
        customer.setName("Alice");
        customerDao.create(customer);

        assertNotNull(customer.getId());

        // update an existing customer
        customer.setName("Bob");
        customer.setEmail("bob@test.com");
        customerDao.update(customer);

        // retrieve and compare
        Customer result = customerDao.findCustomerById(customer.getId());
        assertNotNull(result);
        assertEquals(customer.getName(), result.getName());
        assertEquals(customer.getEmail(), result.getEmail());
    }

    @Test
    @Transactional
    @Rollback
    public void testDelete() {
        // create and store a customer
        Customer alice = new Customer();
        alice.setEmail("alice@test.com");
        alice.setName("Alice");
        customerDao.create(alice);

        assertEquals(1, customerDao.findAll().size());

        Customer bob = new Customer();
        bob.setEmail("bob@test.com");
        bob.setName("Bob");
        customerDao.create(bob);

        assertEquals(2, customerDao.findAll().size());

        // now delete Alice and check
        assertTrue(customerDao.delete(alice.getId()));
        // Alice was deleted
        assertNull(customerDao.findCustomerById(alice.getId()));
        assertEquals(1, customerDao.findAll().size());

        // now Bob's turn
        assertTrue(customerDao.delete(bob.getId()));
        assertNull(customerDao.findCustomerById(bob.getId()));

        // DB table should be empty now
        assertEquals(0, customerDao.findAll().size());
    }
}