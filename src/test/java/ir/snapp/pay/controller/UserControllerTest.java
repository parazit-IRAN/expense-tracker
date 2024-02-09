package ir.snapp.pay.controller;

import ir.snapp.pay.constant.Constants;
import ir.snapp.pay.domain.Authority;
import ir.snapp.pay.domain.User;
import ir.snapp.pay.repository.UserRepository;
import ir.snapp.pay.util.DbTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@IntegrationTest
class UserControllerTest extends AbstractRestControllerTest {

	private User user;
	private User adminUser;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DbTestUtils dbTestUtils;

	private static final String EMAIL = "user@example.com";
	private static final String ADMIN_EMAIL = "admin@localhost";
	private static final String PASSWORD = "passwordEncrypt";


	@BeforeEach
	public void initTest() {
		dbTestUtils.cleanAllTables();
		adminUser = createAdminUser();
		user = createUser();
	}

	@Test
	@WithMockUser(username = EMAIL, password = PASSWORD, authorities = {Constants.USER})
	void getUser() throws Exception {
		this.mockMvc
				.perform(get("/users/{email}", EMAIL))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.email").value(EMAIL))
				.andExpect(jsonPath("$.isActivated").value(true));
	}

	private User createUser() {
		User user = new User();
		user.setEmail(EMAIL);
		user.setActivated(true);
		user.setPassword(passwordEncoder.encode(PASSWORD));
		return userRepository.saveAndFlush(user);
	}

	private User createAdminUser() {
		User user = new User();
		user.setEmail(ADMIN_EMAIL);
		user.setActivated(true);
		user.setPassword(passwordEncoder.encode(PASSWORD));
		Authority authority = new Authority();
		authority.setName(Constants.ADMIN);
		user.setAuthorities(List.of(authority));
		return userRepository.saveAndFlush(user);
	}

}