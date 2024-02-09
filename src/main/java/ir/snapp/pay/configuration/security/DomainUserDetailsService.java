package ir.snapp.pay.configuration.security;


import ir.snapp.pay.domain.Authority;
import ir.snapp.pay.domain.User;
import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.exception.ExpenseExceptionType;
import ir.snapp.pay.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@AllArgsConstructor
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(final String email) {
		log.debug("Authenticating {}", email);

		if (new EmailValidator().isValid(email, null)) {
			return userRepository
					.findOneByEmailIgnoreCase(email)
					.map(this::createSpringSecurityUser)
					.orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " was not found in the database"));
		}

		return userRepository
				.findOneByEmailIgnoreCase(email)
				.map(this::createSpringSecurityUser)
				.orElseThrow(() -> new UsernameNotFoundException("User " + email + " was not found in the database"));
	}

	private org.springframework.security.core.userdetails.User createSpringSecurityUser(User user) {
		if (!user.isActivated()) {
			throw new ExpenseException(ExpenseExceptionType.USER_IS_NOT_ACTIVE_EXCEPTION);
		}
		List<SimpleGrantedAuthority> grantedAuthorities = user
				.getAuthorities()
				.stream()
				.map(Authority::getName)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
	}

}
