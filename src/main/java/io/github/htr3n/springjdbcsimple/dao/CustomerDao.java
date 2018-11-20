package io.github.htr3n.springjdbcsimple.dao;

import io.github.htr3n.springjdbcsimple.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

    // Create
    public Customer create(final Customer customer) {
        final String sql = "INSERT INTO customer (name, email) VALUES (?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        
        this.jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            return statement;
        }, keyHolder);
        
        Integer newCustomerId = keyHolder.getKey().intValue();
        customer.setId(newCustomerId);
        return customer;
	}

    // Retrieve
    public List<Customer> findAll() {
        final String sql = "SELECT * FROM customer";
		return this.jdbcTemplate.query(sql, new CustomerMapper());
	}

    // Retrieve
    // Note when no row exists: <https://stackoverflow.com/a/16390624/339302>
    public Optional<Customer> findById(Integer id) {
		final String sql = "SELECT id, name, email FROM customer WHERE id = ?";
		
		return this.jdbcTemplate.query(
		        sql,
				rs -> rs.next() ?
                        Optional.of(new CustomerMapper().mapRow(rs, 1)):
                        Optional.empty(),
                id);
	}

    // Update
    public boolean update(final Customer customer) {
        final String sql = "UPDATE customer SET name=?, email=? WHERE id=?";
        final Object[] params = new Object[]{customer.getName(), customer.getEmail(), customer.getId()};
        
		return this.jdbcTemplate.update(sql, params) == 1;
	}

    // Delete
    public boolean delete(Integer id) {
        final String sql = "DELETE FROM customer WHERE id = ?";
        final Object[] params = new Object[]{id};
        
		return this.jdbcTemplate.update(sql, params) == 1;
	}

    class CustomerMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            final Customer customer = new Customer();
            customer.setId(rs.getInt("id"));
            customer.setName(rs.getString("name"));
            customer.setEmail(rs.getString("email"));
            return customer;
        }

    }
}
