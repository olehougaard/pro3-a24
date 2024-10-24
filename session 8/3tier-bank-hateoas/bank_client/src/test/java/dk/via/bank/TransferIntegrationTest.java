package dk.via.bank;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dk.via.bank.model.Account;
import dk.via.bank.model.Customer;
import dk.via.bank.model.Money;
import dk.via.bank.model.transaction.DepositTransaction;
import dk.via.bank.model.transaction.Transaction;
import dk.via.bank.model.transaction.TransferTransaction;

import static org.junit.Assert.*;

public class TransferIntegrationTest {
	private Branch branch;
	private Account primaryAccount;
	private Account secondaryAccount;
	private Customer customer;

	@Before
	public void setUp() throws Exception {
		Registry registry = LocateRegistry.getRegistry("localhost", Registry.REGISTRY_PORT);
		branch = (Branch) registry.lookup("Branch 1");
		customer = branch.getCustomer("1234567890");
		primaryAccount = branch.createAccount(customer, "DKK");
		Customer other = branch.getCustomer("1122334455");
		secondaryAccount = branch.createAccount(other, "EUR");
	}
	
	@After
	public void tearDown() throws Exception {
		branch.cancelAccount(primaryAccount);
		branch.cancelAccount(secondaryAccount);
	}
	
	@Test
	public void test() throws RemoteException {
		Money startingAmount = new Money(new BigDecimal(10000), "DKK");
		Money transferAmount = new Money(new BigDecimal(1000), "DKK");
		Money remainingAmount = new Money(new BigDecimal(9000), "DKK");
		List<Transaction> primaryTransactionsBefore = branch.getTransactionsFor(primaryAccount);
		List<Transaction> secondaryTransactionsBefore = branch.getTransactionsFor(secondaryAccount);
		branch.execute(new DepositTransaction(-1, startingAmount, primaryAccount));
		primaryAccount = branch.getAccount(primaryAccount.getAccountNumber());
		assertEquals(startingAmount, primaryAccount.getBalance());
		branch.execute(new TransferTransaction(-1, transferAmount, primaryAccount, secondaryAccount));
		primaryAccount = branch.getAccount(primaryAccount.getAccountNumber());
		secondaryAccount = branch.getAccount(secondaryAccount.getAccountNumber());
		assertEquals(remainingAmount, primaryAccount.getBalance());
		assertEquals(branch.exchange(transferAmount, secondaryAccount.getSettledCurrency()), secondaryAccount.getBalance());
		assertEquals(primaryTransactionsBefore.size() + 2, branch.getTransactionsFor(primaryAccount).size());
		assertEquals(secondaryTransactionsBefore.size() + 1, branch.getTransactionsFor(secondaryAccount).size());
	}

	@Test
	public void testGetAccounts() throws RemoteException {
		Collection<Account> accounts = branch.getAccounts(customer);
		assertTrue(accounts.stream().anyMatch(acc -> acc.getAccountNumber().equals(primaryAccount.getAccountNumber())));
	}

}
