package dk.via.bank;

import java.net.URI;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import dk.via.bank.adapter.AccountRestAdapter;
import dk.via.bank.adapter.CustomerRestAdapter;
import dk.via.bank.adapter.ExchangeRateRestAdapter;
import dk.via.bank.adapter.TransactionRestAdapter;
import dk.via.bank.data.*;
import dk.via.bank.service.BranchAccountService;
import dk.via.bank.service.BranchCustomerService;
import dk.via.bank.service.BranchExchangeService;
import dk.via.bank.service.BranchTransactionService;

public class RunBranch {
	public static final int REG_NUMBER = 4711;

	public static void main(String[] args) throws Exception {
		String uri = "http://localhost:8080/";

		CustomerData customerData = new CustomerRestAdapter(uri);
		AccountData accountData = new AccountRestAdapter(uri);
		ExchangeRateData exchangeData = new ExchangeRateRestAdapter(uri);
		TransactionData transactionData = new TransactionRestAdapter(uri);

		BranchCustomerService customerService = new BranchCustomerService(customerData);
		BranchAccountService accountService = new BranchAccountService(REG_NUMBER, accountData);
		BranchTransactionService transactionService = new BranchTransactionService(accountData, transactionData, exchangeData);
		BranchExchangeService exchangeService = new BranchExchangeService(exchangeData);

		BranchFacade branch = new BranchFacade(customerService, accountService, transactionService, exchangeService);
		Remote remote = UnicastRemoteObject.exportObject(branch, 0);

		Registry branchRegistry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		branchRegistry.rebind("Branch 1", remote);
		System.out.println("Branch started");
	}
}
