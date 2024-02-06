package ir.snapp.pay.service;


import ir.snapp.pay.domain.Authority;
import ir.snapp.pay.domain.User;
import ir.snapp.pay.dto.UserInputDto;
import ir.snapp.pay.dto.UserOutputDto;
import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.exception.ExpenseExceptionType;
import ir.snapp.pay.repository.AuthorityRepository;
import ir.snapp.pay.repository.UserRepository;
import ir.snapp.pay.service.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;
	private final AuthorityRepository authorityRepository;

	@Transactional(readOnly = true)
	public User getUser(String email, String password) throws ExpenseException {
		User user = userRepository.findOneByEmailIgnoreCase(email)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new ExpenseException(ExpenseExceptionType.PASSWORD_IS_NOT_CORRECT_EXCEPTION);
		}

		return user;
	}

	@Transactional
	public UserOutputDto createUser(UserInputDto userInputDto) throws ExpenseException {

		if (userRepository.findOneByEmailIgnoreCase(userInputDto.getEmail()).isPresent()) {
			throw new ExpenseException(ExpenseExceptionType.USER_EXISTS_EXCEPTION);
		}

		User user = new User();
		user.setFirstName(userInputDto.getFirstName());
		user.setLastName(userInputDto.getLastName());
		if (userInputDto.getEmail() != null) {
			user.setEmail(userInputDto.getEmail().toLowerCase());
		}
		String encryptedPassword = passwordEncoder.encode(userInputDto.getPassword());
		user.setPassword(encryptedPassword);
		if (userInputDto.getAuthorities() != null) {
			List<Authority> authorities = userInputDto
					.getAuthorities()
					.stream()
					.map(authorityRepository::findById)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(Collectors.toList());
			user.setAuthorities(authorities);
		}
		userRepository.save(user);
		log.debug("Created an user: {}", user);
		return userMapper.userToUserOutputDto(user);
	}


}
