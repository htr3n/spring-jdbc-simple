package io.github.htr3n.springjdbcsimple.data;

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

@Repository
public class CustomerDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

    // Create
	public Customer create(Customer customer) {
        String sql = "INSERT INTO customer (name, email) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
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
		String sql = "SELECT * FROM customer";
		return this.jdbcTemplate.query(sql, new CustomerMapper());
	}

    // Retrieve
    // When there is no row: <https://stackoverflow.com/a/16390624/339302>
	public Customer findCustomerById(Integer id) {
		String sql = "SELECT id, name, email FROM customer WHERE id = ?";

		// JdbcTemplate.queryForObject() only used for known outcome, e.g. the input must be
        // a single row/column query
        //return jdbcTemplate.queryForObject(sql, new Object[] { id }, new CustomerMapper());
		return this.jdbcTemplate.query(sql,
				rs -> rs.next() ? new CustomerMapper().mapRow(rs, 1): null,
                id);
	}

    // Update
	public boolean update(Customer customer) {
		String sql = "UPDATE customer SET name=?, email=? WHERE id=?";
		Object[] params = new Object[]{customer.getName(), customer.getEmail(), customer.getId()};
		return this.jdbcTemplate.update(sql, params) == 1;
	}

    // Delete
	public boolean delete(Integer id) {
		String sql = "DELETE FROM customer WHERE id = ?";
        Object[] params = new Object[]{id};
		return this.jdbcTemplate.update(sql, params) == 1;
	}

    class CustomerMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Customer customer = new Customer();
            customer.setId(rs.getInt("id"));
            customer.setName(rs.getString("name"));
            customer.setEmail(rs.getString("email"));
            return customer;
        }

    }
}
