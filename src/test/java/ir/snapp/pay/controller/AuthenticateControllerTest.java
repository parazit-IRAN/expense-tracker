package ir.snapp.pay.controller;

import ir.snapp.pay.domain.User;
import ir.snapp.pay.dto.LoginDto;
import ir.snapp.pay.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@IntegrationTest
class AuthenticateControllerTest extends AbstractRestControllerTest {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;

	private static final String EMAIL = "user@example.com";
	private static final String PASSWORD = "passwordEncrypt";


	@Test
	@Transactional
	void testAuthorize() throws Exception {
		createUser();
		LoginDto login = new LoginDto();
		login.setEmail(EMAIL);
		login.setPassword(PASSWORD);
		this.mockMvc
				.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).content(this.toJson(login)))
				.andExpect(status().isOk())
				.andExpect(content().string(not(emptyString())));
	}

	@Test
	void testAuthorizeFails() throws Exception {
		LoginDto login = new LoginDto();
		login.setEmail("wrong-user@local.com");
		login.setPassword("wrong password");
		this.mockMvc
				.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).content(this.toJson(login)))
				.andExpect(status().isUnprocessableEntity());
	}


	private User createUser() {
		User user = new User();
		user.setEmail(EMAIL);
		user.setActivated(true);
		user.setPassword(passwordEncoder.encode(PASSWORD));
		return userRepository.saveAndFlush(user);
	}

}