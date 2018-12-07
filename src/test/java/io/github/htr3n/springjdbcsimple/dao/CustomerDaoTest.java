package io.github.htr3n.springjdbcsimple.dao;

import io.github.htr3n.springjdbcsimple.entity.Customer;
import org.junit.Before;
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

    private Customer alice;
    private Customer bob;

    @Before
    public void setUp(){
        alice = new Customer();
        alice.setName(ALICE_NAME);
        alice.setEmail(ALICE_EMAIL);

        bob = new Customer();
        bob.setName(BOB_NAME);
        bob.setEmail(BOB_EMAIL);
    }

    @Test
    public void create_shouldReturnValidCustomer_whenAddingNewCustomer() {

        customerDao.create(alice);

        assertThat(alice.getId()).isNotNull();
        
        Optional<Customer> result = customerDao.findById(alice.getId());

        assertThat(result).isPresent();
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
        customerDao.create(alice);

        Optional<Customer> validCustomer = customerDao.findById(alice.getId());

        assertThat(validCustomer).isPresent();
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

        customerDao.create(alice);
        List<Customer> customers = customerDao.findAll();

        assertThat(customers).isNotNull().hasSize(ONE_CUSTOMER);

        Customer result = customers.get(0);

        assertThat(result).hasFieldOrPropertyWithValue("name", ALICE_NAME);
        assertThat(result).hasFieldOrPropertyWithValue("email", ALICE_EMAIL);

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
        customerDao.create(alice);

        assertThat(alice.getId()).isNotNull();
        assertThat(customerDao.update(alice)).isTrue();

        alice.setName(BOB_NAME);
        alice.setEmail(BOB_EMAIL);
        assertThat(customerDao.update(alice)).isTrue();

        Optional<Customer> found = customerDao.findById(alice.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(alice.getName());
        assertThat(found.get().getEmail()).isEqualTo(alice.getEmail());
    }

    @Test
    public void delete_shouldYieldFalse_forEmptyDatabaseOrNonexistingCustomer() {
    	assertThat(customerDao.delete(new Random().nextInt())).isFalse();
    }

    @Test
    public void delete_shouldYieldTrue_forExistingCustomer() {
        customerDao.create(alice);
        assertThat(customerDao.findAll()).hasSize(ONE_CUSTOMER);
        assertThat(customerDao.delete(alice.getId())).isTrue();
        assertThat(customerDao.findById(alice.getId()).isPresent()).isFalse();
        assertThat(customerDao.findAll()).isEmpty();
    }
}
