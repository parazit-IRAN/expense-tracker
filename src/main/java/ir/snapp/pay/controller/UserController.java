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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@Slf4j
@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/users")
public class UserController extends BaseController {

	private final UserService userService;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority(\"" + Constants.ADMIN + "\")")
	public ResponseEntity<UserOutputDto> createUser(@Valid @RequestBody UserInputDto userInputDto) {
		log.debug("REST request to save User : {}", userInputDto);
		try {
			UserOutputDto userOutputDto = userService.createUser(userInputDto);
			return success(userOutputDto);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority(\"" + Constants.ADMIN + "\") and (!authentication.principal.equals(#email))")
	public ResponseEntity<String> deleteUser(@Email(message = "email.must.be.valid") @Valid @RequestParam("email") String email) {
		log.debug("REST request to delete User: {}", email);
		try {
			userService.deleteUser(email);
			return success("Deleted User: " + email);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@GetMapping( value = "/{email}",produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	public ResponseEntity<UserOutputDto> getUser(@Email(message = "email.must.be.valid") @Valid @PathVariable("email") String email) {
		log.debug("REST request to get User: {}", email);
		try {
			return success(userService.getUser(email));
		} catch (Exception e) {
			return failure(e);
		}
	}

	@GetMapping(value = "/deactivate/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority(\"" + Constants.ADMIN + "\")")
	public ResponseEntity<UserOutputDto> disableUser(@Email(message = "email.must.be.valid") @Valid @PathVariable("email") String email) {
		log.debug("REST request to disable User: {}", email);
		try {
			return success(userService.disableUser(email));
		} catch (Exception e) {
			return failure(e);
		}
	}

	@GetMapping(value = "/activate/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority(\"" + Constants.ADMIN + "\")")
	public ResponseEntity<UserOutputDto> activeUser(@Email(message = "email.must.be.valid") @Valid @PathVariable("email") String email) {
		log.debug("REST request to disable User: {}", email);
		try {
			return success(userService.activeUser(email));
		} catch (Exception e) {
			return failure(e);
		}
	}
}
