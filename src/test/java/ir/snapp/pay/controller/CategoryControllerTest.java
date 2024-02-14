package ir.snapp.pay.controller;

import ir.snapp.pay.constant.AccountType;
import ir.snapp.pay.constant.Constants;
import ir.snapp.pay.constant.TransactionType;
import ir.snapp.pay.domain.*;
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
class CategoryControllerTest extends AbstractRestControllerTest {

	@Autowired
	private DbTestUtils dbTestUtils;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private TransactionRepository transactionRepository;

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
	void testCreateCategory() throws Exception {
		String NEW_CATEGORY = "NEW_CATEGORY";

		this.mockMvc
				.perform(post("/categories").contentType(MediaType.APPLICATION_JSON).content(this.toJson(NEW_CATEGORY)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.name").value(NEW_CATEGORY));
	}

	@Test
	@WithMockUser(username = EMAIL, password = PASSWORD, authorities = {Constants.USER})
	void testDeleteCategory() throws Exception {
		Category category = createCategory(user);
		Account cardAccount = createCardAccount(user);
		Transaction transaction = createTransaction(category, user, cardAccount);

		assertThat(transactionRepository.findAll().size(), is(1));

		this.mockMvc
				.perform(delete("/categories/{id}", category.getId()))
				.andExpect(status().isOk())
				.andExpect(content().string("Deleted Category id: " + category.getId()));

		assertThat(transactionRepository.findAll().size(), is(0));
		assertThat(categoryRepository.findById(category.getId()).isEmpty(), is(true));

	}

	private Category createCategory(User user) {
		Category category = new Category();
		category.setName("NEW_CATEGORY");
		category.setUser(user);
		return categoryRepository.save(category);
	}

	private Transaction createTransaction(Category category, User user, Account account) {
		Transaction transaction = new Transaction();
		transaction.setAccount(account);
		transaction.setAmount(new BigDecimal(1000));
		transaction.setCategory(category);
		transaction.setUser(user);
		transaction.setType(TransactionType.EXPENSE);
		transactionRepository.save(transaction);
		return transaction;
	}

	private Account createCardAccount(User user) {
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