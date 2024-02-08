package ir.snapp.pay.controller;


import ir.snapp.pay.constant.Constants;
import ir.snapp.pay.dto.UserInputDto;
import ir.snapp.pay.dto.UserOutputDto;
import ir.snapp.pay.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController extends BaseController {

	private final UserService userService;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority(\"" + Constants.ADMIN + "\")")
	public ResponseEntity<UserOutputDto> createUser(@Valid @RequestBody UserInputDto userInputDto) {
		log.debug("REST request to save User : {}", userInputDto);
		UserOutputDto userOutputDto = userService.createUser(userInputDto);
		return success(userOutputDto);
	}

	@DeleteMapping(value = "/{email}")
	@PreAuthorize("hasAuthority(\"" + Constants.ADMIN + "\")")
	public ResponseEntity<String> deleteUser(@PathVariable("email") @Email String email) {
		log.debug("REST request to delete User: {}", email);
		userService.deleteUser(email);
		return success("Deleted User: " + email);
	}
}
