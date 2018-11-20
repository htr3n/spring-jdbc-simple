package io.github.htr3n.springjdbcsimple.dao;

import io.github.htr3n.springjdbcsimple.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan
public class CustomerDaoTest {

    private static final String ALICE_NAME = "Alice";
    private static final String ALICE_EMAIL = "alice@test.com";
    private static final String BOB_NAME = "Bob";
    private static final String BOB_EMAIL = "bob@test.com";
    private static final int ONE_CUSTOMER = 1;
    private static final int TWO_CUSTOMERS = 2;

    @Autowired
    private CustomerDao customerDao;

    @Test
    public void create_shouldReturnValidCustomer_whenAddingNewCustomer() {

        Customer alice = new Customer();
        alice.setName(ALICE_NAME);
        alice.setEmail(ALICE_EMAIL);
        customerDao.create(alice);

        assertThat(alice.getId()).isNotNull();
        
        Optional<Customer> result = customerDao.findById(alice.getId());

        assertThat(result.isPresent()).isTrue();
        assertThat(alice).hasFieldOrPropertyWithValue("name", ALICE_NAME);
        assertThat(alice).hasFieldOrPropertyWithValue("email", ALICE_EMAIL);
    }

    @Test
    public void findById_shouldReturnInvalidCustomer_forEmptyDatabase() {
        Optional<Customer> invalidCustomer = customerDao.findById(new Random().nextInt());
        assertThat(invalidCustomer.isPresent()).isFalse();    	
    }

    @Test
    public void findById_shouldReturnValidCustomer_forExistingCustomer() {
        final Customer alice = new Customer();
        alice.setName(ALICE_NAME);
        alice.setEmail(ALICE_EMAIL);
        customerDao.create(alice);

        Optional<Customer> validCustomer = customerDao.findById(alice.getId());
        assertThat(validCustomer.isPresent()).isTrue();
        assertThat(validCustomer.get().getName()).isEqualTo(alice.getName());
        assertThat(validCustomer.get().getEmail()).isEqualTo(alice.getEmail());
    }

    @Test
    public void findAll_shouldYieldEmptyList_forEmptyDatabase() {
        List<Customer> noCustomers = customerDao.findAll();
        assertThat(noCustomers).isNullOrEmpty();
    }
    
    @Test
    public void findAll_shouldYieldListOfCustomers_forNonemptyDatabase() {

        Customer alice = new Customer();
        alice.setName(ALICE_NAME);
        alice.setEmail(ALICE_EMAIL);
        customerDao.create(alice);

        List<Customer> customers = customerDao.findAll();
        assertThat(customers).isNotNull().hasSize(ONE_CUSTOMER);

        Customer result = customers.get(0);
        assertThat(result).hasFieldOrPropertyWithValue("name", ALICE_NAME);
        assertThat(result).hasFieldOrPropertyWithValue("email", ALICE_EMAIL);


        Customer bob = new Customer();
        bob.setName(BOB_NAME);
        bob.setEmail(BOB_EMAIL);
        customerDao.create(bob);

        customers = customerDao.findAll();
        assertThat(customers).isNotNull().hasSize(TWO_CUSTOMERS);
    }

    @Test
    public void update_shouldYieldFalse_forEmptyDatabase() {
        Customer notFound = new Customer();        
        notFound.setId(new Random().nextInt());
        assertThat(customerDao.update(notFound)).isFalse();
    }

    @Test
    public void update_shouldYieldTrue_forExistingCustomer() {
        Customer customer = new Customer();
        customer.setName(ALICE_NAME);
        customer.setEmail(ALICE_EMAIL);
        customerDao.create(customer);

        assertThat(customer.getId()).isNotNull();
        assertThat(customerDao.update(customer)).isTrue();

        customer.setName(BOB_NAME);
        customer.setEmail(BOB_EMAIL);
        assertThat(customerDao.update(customer)).isTrue();

        Optional<Customer> found = customerDao.findById(customer.getId());
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getName()).isEqualTo(customer.getName());
        assertThat(found.get().getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    public void delete_shouldYieldFalse_forEmptyDatabaseOrNonexistingCustomer() {
    	assertThat(customerDao.delete(new Random().nextInt())).isFalse();
    }

    @Test
    public void delete_shouldYieldTrue_forExistingCustomer() {
        Customer alice = new Customer();
        alice.setName(ALICE_NAME);
        alice.setEmail(ALICE_EMAIL);
        customerDao.create(alice);

        assertThat(customerDao.findAll()).hasSize(ONE_CUSTOMER);
        assertThat(customerDao.delete(alice.getId())).isTrue();
        assertThat(customerDao.findById(alice.getId()).isPresent()).isFalse();
        assertThat(customerDao.findAll()).isEmpty();
    }
}
