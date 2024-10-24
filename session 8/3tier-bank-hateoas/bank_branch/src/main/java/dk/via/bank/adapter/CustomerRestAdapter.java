package dk.via.bank.adapter;

import dk.via.bank.data.CustomerData;
import dk.via.bank.model.Customer;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.rmi.RemoteException;

public class CustomerRestAdapter implements CustomerData {
    private final String endpoint;
    // You don't want two different client objects in practice. It is only to show the possibilities.
    private final RestTemplate restTemplate;
    private final WebClient webClient;

    public CustomerRestAdapter(String endpoint) {
        this.endpoint = endpoint + "customers";
        this.restTemplate = new RestTemplate();
        this.webClient = WebClient.create(this.endpoint);
    }

    @Override
    public Customer create(String cpr, String name, String address) throws RemoteException {
        try {
            Customer customer = new Customer(cpr, name, address);
            return restTemplate.postForObject(endpoint, customer, Customer.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public Customer read(String cpr) throws RemoteException {
        try {
            return webClient.get().uri("/{cpr}", cpr).retrieve().bodyToMono(Customer.class).block();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }

    }

    @Override
    public void update(Customer customer) throws RemoteException {
        try {
            restTemplate.put(endpoint + "/" + customer.getCpr(), customer);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void delete(Customer customer) throws RemoteException {
        try {
            restTemplate.delete(endpoint + "/" + customer.getCpr());
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }
}
