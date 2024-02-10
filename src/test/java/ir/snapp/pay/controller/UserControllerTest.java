package ir.snapp.pay.controller;

import ir.snapp.pay.constant.Constants;
import ir.snapp.pay.domain.Authority;
import ir.snapp.pay.domain.User;
import ir.snapp.pay.dto.UserInputDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@IntegrationTest
class UserControllerTest extends AbstractRestControllerTest {

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
	}

	@Test
	@WithMockUser(username = ADMIN_EMAIL, password = PASSWORD, authorities = {Constants.ADMIN})
	void testCreateUser() throws Exception {
		String USER_TEST_EMAIL = "test@test.com";

		UserInputDto userInputDto = UserInputDto.builder()
				.email(USER_TEST_EMAIL)
				.firstName("first-name")
				.lastName("last-name")
				.password(PASSWORD)
				.authorities(List.of("USER"))
				.build();


		this.mockMvc
				.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(this.toJson(userInputDto)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.email").value(USER_TEST_EMAIL))
				.andExpect(jsonPath("$.isActivated").value(true));
	}

	@Test
	@WithMockUser(username = EMAIL, password = PASSWORD, authorities = {Constants.USER})
	void testGetUser() throws Exception {
		createUser();
		this.mockMvc
				.perform(get("/users/{email}", EMAIL))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.email").value(EMAIL))
				.andExpect(jsonPath("$.isActivated").value(true));
	}

	@Test
	@WithMockUser(username = ADMIN_EMAIL, password = PASSWORD, authorities = {Constants.ADMIN})
	void testUpdateUser() throws Exception {
		createUser();
		String USER_TEST_EMAIL = "test@test.com";
		String NEW_PASSWORD = "test@test.com";
		String FIRST_NAME = "first-name";
		String LAST_NAME = "last-name";

		UserInputDto updateUser = UserInputDto.builder()
				.email(USER_TEST_EMAIL)
				.firstName(FIRST_NAME)
				.lastName(LAST_NAME)
				.password(NEW_PASSWORD)
				.authorities(List.of("ADMIN"))
				.build();


		this.mockMvc
				.perform(put("/users/{email}", EMAIL).contentType(MediaType.APPLICATION_JSON).content(this.toJson(updateUser)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.email").value(USER_TEST_EMAIL))
				.andExpect(jsonPath("$.firstName").value(FIRST_NAME))
				.andExpect(jsonPath("$.lastName").value(LAST_NAME))
				.andExpect(jsonPath("$.isActivated").value(true));
	}

	@Test
	@WithMockUser(username = ADMIN_EMAIL, password = PASSWORD, authorities = {Constants.ADMIN})
	void testDeleteUser() throws Exception {
		createUser();

		this.mockMvc
				.perform(delete("/users/{email}", EMAIL))
				.andExpect(status().isOk())
				.andExpect(content().string("Deleted User: " + EMAIL));
	}


	@Test
	@WithMockUser(username = ADMIN_EMAIL, password = PASSWORD, authorities = {Constants.ADMIN})
	void testDeactivateUser() throws Exception {
		createUser();
		this.mockMvc
				.perform(get("/users/deactivate/{email}", EMAIL))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.email").value(EMAIL))
				.andExpect(jsonPath("$.isActivated").value(false));
	}

	@Test
	@WithMockUser(username = ADMIN_EMAIL, password = PASSWORD, authorities = {Constants.ADMIN})
	void testActivateUser() throws Exception {
		createDeactiveUser();
		this.mockMvc
				.perform(get("/users/activate/{email}", EMAIL))
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
		Authority authority = new Authority();
		authority.setName(Constants.USER);
		user.setAuthorities(List.of(authority));
		return userRepository.saveAndFlush(user);
	}

	private User createDeactiveUser() {
		User user = new User();
		user.setEmail(EMAIL);
		user.setActivated(false);
		user.setPassword(passwordEncoder.encode(PASSWORD));
		Authority authority = new Authority();
		authority.setName(Constants.USER);
		user.setAuthorities(List.of(authority));
		return userRepository.saveAndFlush(user);
	}

}