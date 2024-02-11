package ir.snapp.pay.service.mapper;


import ir.snapp.pay.domain.Authority;
import ir.snapp.pay.domain.User;
import ir.snapp.pay.dto.UserInputDto;
import ir.snapp.pay.dto.UserOutputDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {

	public UserOutputDto userToUserOutputDto(User user) {
		return UserOutputDto.builder()
				.id(user.getId())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.isActivated(user.isActivated())
				.authorities(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList()))
				.language(user.getLanguage())
				.build();
	}

	public User userInputDtotoUser(UserInputDto userInputDto) {
		User user = new User();
		user.setFirstName(userInputDto.getFirstName());
		user.setLastName(userInputDto.getLastName());
		user.setEmail(userInputDto.getEmail());
		user.setActivated(true);
		List<Authority> authorities = this.authoritiesToAuthorities(userInputDto.getAuthorities());
		user.setAuthorities(authorities);
		return user;
	}

	private List<Authority> authoritiesToAuthorities(List<String> authoritiesAsString) {
		List<Authority> authorities = new ArrayList<>();
		if (authoritiesAsString != null) {
			authorities = authoritiesAsString.stream().map(string -> {
				Authority authority = new Authority();
				authority.setName(string);
				return authority;
			}).collect(Collectors.toList());
		}
		return authorities;
	}
}
