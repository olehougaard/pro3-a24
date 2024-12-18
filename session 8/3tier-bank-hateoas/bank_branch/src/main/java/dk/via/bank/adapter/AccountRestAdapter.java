package dk.via.bank.adapter;

import dk.via.bank.data.AccountData;
import dk.via.bank.dto.AccountSpecification;
import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Customer;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.server.core.TypeReferences;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class AccountRestAdapter implements AccountData {
    private final String endpoint;
    private final RestTemplate restTemplate;

    public AccountRestAdapter(String endpoint) {
        this.endpoint = endpoint + "accounts";
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Account create(int regNumber, Customer customer, String currency) throws RemoteException {
        try {
            AccountSpecification spec = new AccountSpecification(regNumber, currency, customer.getCpr());
            return restTemplate.postForEntity(endpoint, spec, Account.class).getBody();
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public Account read(AccountNumber accountNumber) throws RemoteException {
        try {
            return restTemplate.getForObject(endpoint + "/" + accountNumber, Account.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public Collection<Account> readAccountsFor(Customer customer) throws RemoteException {
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString("http://localhost:8080/customers/")
                    .path(customer.getCpr())
                    .build(0);
            Traverson traverson = new Traverson(uri, MediaTypes.HAL_JSON);
            CollectionModel<Account> accountsModel = traverson
                    .follow("accounts")
                    .toObject(new TypeReferences.CollectionModelType<Account>() {});
            if (accountsModel == null) throw new RemoteException("No response from " + uri.toString());
            return accountsModel.getContent();
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void update(Account account) throws RemoteException {
        try {
            restTemplate.put(endpoint + "/" + account.getAccountNumber(), account);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void delete(Account account) throws RemoteException {
        try {
            restTemplate.delete(endpoint + "/" + account.getAccountNumber());
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }
}
