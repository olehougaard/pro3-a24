package dk.via.bank.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import dk.via.bank.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class CustomerDataService  {
	private static final long serialVersionUID = 1L;
	private final DatabaseHelper<Customer> helper;

    public CustomerDataService(@Value("${jdbc.url}") String jdbcURL,
							   @Value("${jdbc.username}") String username,
							   @Value("${jdbc.password}") String password) {
		this.helper = new DatabaseHelper<>(jdbcURL, username, password);
    }

	private static Customer createCustomer(ResultSet rs) throws SQLException {
		String cpr = rs.getString("cpr");
		String name = rs.getString("name");
		String address = rs.getString("address");
		return new Customer(cpr, name, address);
	}

	public Customer create(String cpr, String name, String address) throws SQLException {
		helper.executeUpdate("INSERT INTO Customer VALUES (?, ?, ?)", cpr, name, address);
		return new Customer(cpr, name, address);
	}

	public Customer read(String cpr) throws SQLException {
        return helper.mapSingle(CustomerDataService::createCustomer, "SELECT * FROM Customer WHERE cpr = ?;", cpr);
	}

	public void update(Customer customer) throws SQLException {
		helper.executeUpdate("UPDATE Customer set name = ?, address = ? WHERE cpr = ?",
				customer.getName(),
				customer.getAddress(),
				customer.getCpr());
	}

	public void delete(String cpr) throws SQLException {
		helper.executeUpdate("DELETE FROM Customer WHERE cpr = ?", cpr);
	}
}
