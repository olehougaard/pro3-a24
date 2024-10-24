package dk.via.bank.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;
	private String cpr;
	private String name;
	private String address;

	public Customer(String cpr, String name, String address) {
		this.cpr = cpr;
		this.name = name;
		this.address = address;
	}

	public String getCpr() {
		return cpr;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public void move(String address) {
		this.address = address;
	}
	
	// Marshalling

	public Customer() {}

	public void setCpr(String cpr) {
		this.cpr = cpr;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
