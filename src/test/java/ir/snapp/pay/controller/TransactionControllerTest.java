package ir.snapp.pay.controller;

import ir.snapp.pay.constant.AccountType;
import ir.snapp.pay.constant.Constants;
import ir.snapp.pay.constant.TransactionType;
import ir.snapp.pay.domain.*;
import ir.snapp.pay.dto.TransactionInputDto;
import ir.snapp.pay.repository.AccountRepository;
import ir.snapp.pay.repository.CategoryRepository;
import ir.snapp.pay.repository.TransactionRepository;
import ir.snapp.pay.repository.UserRepository;
import ir.snapp.pay.util.DbTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@IntegrationTest
class TransactionControllerTest extends AbstractRestControllerTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private DbTestUtils dbTestUtils;
	@Autowired
	private PasswordEncoder passwordEncoder;
	private User user;
	private Account account;
	private Category category;

	private static final String EMAIL = "user@example.com";
	private static final String PASSWORD = "passwordEncrypt";

	@BeforeEach
	public void initTest() {
		dbTestUtils.cleanAllTables();
		user = createUser();
		account = createAccount(user);
		category = getTransportCategory();
	}

	@AfterEach()
	public void after() {
		dbTestUtils.cleanAllTables();
	}

	@Test
	@WithMockUser(username = EMAIL, password = PASSWORD, authorities = {Constants.USER})
	void testCreateUser() throws Exception {
		BigDecimal AMOUNT = new BigDecimal(10.0);

		TransactionInputDto transactionInputDto = TransactionInputDto.builder()
				.userId(user.getId())
				.accountId(account.getId())
				.amount(AMOUNT)
				.categoryId(category.getId())
				.type("EXPENSE")
				.build();


		this.mockMvc
				.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON).content(this.toJson(transactionInputDto)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.amount").value(AMOUNT))
				.andExpect(jsonPath("$.categoryName").value(category.getName()))
				.andExpect(jsonPath("$.accountName").value(account.getName()))
				.andExpect(jsonPath("$.userEmail").value(user.getEmail()));
	}

	@Test
	@WithMockUser(username = EMAIL, password = PASSWORD, authorities = {Constants.USER})
	void testDeleteTransaction() throws Exception {
		Transaction transaction = createTransaction();

		this.mockMvc
				.perform(delete("/transactions/{id}", transaction.getId()))
				.andExpect(status().isOk())
				.andExpect(content().string("Deleted Transaction id: " + transaction.getId()));
	}

	private Transaction createTransaction() {
		Transaction transaction = new Transaction();
		transaction.setAccount(createAccount(user));
		transaction.setAmount(new BigDecimal(1000));
		transaction.setCategory(getTransportCategory());
		transaction.setUser(user);
		transaction.setType(TransactionType.EXPENSE);
		transactionRepository.save(transaction);
		return transaction;
	}

	private Category getTransportCategory() {
		return categoryRepository.findByName("Transport").orElseThrow(() -> new RuntimeException(""));
	}

	private Account createAccount(User user) {
		Account account = new Account();
		account.setName("card");
		account.setBalance(new BigDecimal(1000));
		account.setType(AccountType.CARD);
		account.setCurrency("USD");
		account.setUser(user);
		account.setTransactions(List.of());
		return accountRepository.save(account);
	}


	private User createUser() {
		User user = new User();
		user.setEmail(EMAIL);
		user.setFirstName("first-name");
		user.setLastName("last-name");
		user.setActivated(true);
		user.setPassword(passwordEncoder.encode(PASSWORD));
		Authority authority = new Authority();
		authority.setName(Constants.USER);
		user.setAuthorities(List.of(authority));
		return userRepository.saveAndFlush(user);
	}


}