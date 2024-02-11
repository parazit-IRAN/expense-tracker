package ir.snapp.pay.controller;

import ir.snapp.pay.constant.AccountType;
import ir.snapp.pay.constant.Constants;
import ir.snapp.pay.constant.TransactionType;
import ir.snapp.pay.domain.*;
import ir.snapp.pay.dto.AccountInputDto;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@IntegrationTest
class AccountControllerTest extends AbstractRestControllerTest {

	@Autowired
	private DbTestUtils dbTestUtils;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private AccountRepository accountRepository;


	private User user;

	private static final String EMAIL = "user@example.com";
	private static final String ADMIN_EMAIL = "admin@localhost";
	private static final String PASSWORD = "passwordEncrypt";

	@BeforeEach
	@AfterEach
	public void initTest() {
		dbTestUtils.cleanAllTables();
		user = createUser();
	}

	@Test
	@WithMockUser(username = EMAIL, password = PASSWORD, authorities = {Constants.USER})
	void testCreateAccount() throws Exception {

		String accountName = "account_name";
		AccountType type = AccountType.CARD;
		BigDecimal balance = new BigDecimal(1000);
		String currency = "USD";
		AccountInputDto accountInputDto = createAccountInputDto(accountName, type, balance, currency);

		this.mockMvc
				.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(this.toJson(accountInputDto)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.name").value(accountName))
				.andExpect(jsonPath("$.type").value(type.name()))
				.andExpect(jsonPath("$.balance").value(balance))
				.andExpect(jsonPath("$.currency").value(currency));
	}

	@Test
	@WithMockUser(username = EMAIL, password = PASSWORD, authorities = {Constants.USER})
	void testDeleteAccount() throws Exception {
		Category category = createCategory(user, List.of());
		Account cardAccount = createCardAccount(user, List.of());
		Transaction transaction_1 = createExpenseTransaction(category, user, cardAccount, new BigDecimal(1000));
		Transaction transaction_2 = createExpenseTransaction(category, user, cardAccount, new BigDecimal(2000));


		assertThat(transactionRepository.findAll().size(), is(2));
		assertThat(categoryRepository.findById(category.getId()).isPresent(), is(true));
		assertThat(accountRepository.findById(cardAccount.getId()).isPresent(), is(true));

		this.mockMvc
				.perform(delete("/accounts/{id}", cardAccount.getId()))
				.andExpect(status().isOk())
				.andExpect(content().string("Deleted Account id: " + cardAccount.getId()));

		assertThat(transactionRepository.findAll().size(), is(0));
		assertThat(categoryRepository.findById(category.getId()).isPresent(), is(true));
		assertThat(accountRepository.findById(cardAccount.getId()).isPresent(), is(false));

	}

	private Category createCategory(User user, List<Transaction> transactions) {
		Category category = Category.builder()
				.name("NAME")
				.user(user)
				.transactions(transactions)
				.build();
		return categoryRepository.save(category);
	}

	private static AccountInputDto createAccountInputDto(String accountName, AccountType type, BigDecimal balance, String currency) {
		return AccountInputDto.builder()
				.name(accountName)
				.type(type)
				.balance(balance)
				.currency(currency)
				.build();
	}

	private Transaction createExpenseTransaction(Category category, User user, Account account, BigDecimal amount) {
		Transaction transaction = new Transaction();
		transaction.setAccount(account);
		transaction.setAmount(amount);
		transaction.setCategory(category);
		transaction.setUser(user);
		transaction.setType(TransactionType.EXPENSE);
		transactionRepository.save(transaction);
		return transaction;
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

	private Account createCardAccount(User user, List<Transaction> transactions) {
		Account account = new Account();
		account.setBalance(new BigDecimal(1200));
		account.setCurrency("USD");
		account.setType(AccountType.CARD);
		account.setTransactions(transactions);
		account.setUser(user);
		account.setName("MY_CARD");
		return accountRepository.save(account);
	}

}