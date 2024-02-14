package ir.snapp.pay.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.snapp.pay.constant.Constants;
import ir.snapp.pay.dto.UserInputDto;
import ir.snapp.pay.dto.UserOutputDto;
import ir.snapp.pay.dto.UserSettingDto;
import ir.snapp.pay.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@Slf4j
@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/users")
@Tag(name = "User", description = "The User API. Contains all the operations that can be performed on a user.")
public class UserController extends BaseController {

	private final UserService userService;

	@PostMapping
	@Operation(summary = "create a user")
	public ResponseEntity<UserOutputDto> createUser(@Valid @RequestBody UserInputDto userInputDto) {
		log.debug("REST request to save User : {}", userInputDto);
		try {
			UserOutputDto userOutputDto = userService.createUser(userInputDto);
			return success(userOutputDto);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@GetMapping(value = "/{email}")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "get an user")
	public ResponseEntity<UserOutputDto> getUser(@Email(message = "email.must.be.valid") @Valid @PathVariable("email") String email) {
		log.debug("REST request to get User: {}", email);
		try {
			return success(userService.getUser(email));
		} catch (Exception e) {
			return failure(e);
		}
	}

	@PutMapping(value = "/{email}")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "update user fields")
	public ResponseEntity<UserOutputDto> updateUser(@Valid @RequestBody UserInputDto userInputDto,
													@Email(message = "email.must.be.valid") @Valid @PathVariable("email") String email) {
		log.debug("REST request to update User email {} to {}", email, userInputDto);
		try {
			UserOutputDto userOutputDto = userService.updateUser(email, userInputDto);
			return success(userOutputDto);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@DeleteMapping(value = "/{email}")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\") and (!authentication.principal.equals(#email))")
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "delete user, you can not remove yourself")
	public ResponseEntity<String> deleteUser(@Email(message = "email.must.be.valid") @Valid @PathVariable("email") String email) {
		log.debug("REST request to delete User: {}", email);
		try {
			userService.deleteUser(email);
			return success("Deleted User: " + email);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@GetMapping(value = "/deactivate/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority(\"" + Constants.ADMIN + "\")")
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "de active user by admin permission")
	public ResponseEntity<UserOutputDto> deactivateUser(@Email(message = "email.must.be.valid") @Valid @PathVariable("email") String email) {
		log.debug("REST request to disable User: {}", email);
		try {
			return success(userService.disableUser(email));
		} catch (Exception e) {
			return failure(e);
		}
	}

	@GetMapping(value = "/activate/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority(\"" + Constants.ADMIN + "\")")
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "active user by admin permission")
	public ResponseEntity<UserOutputDto> activeUser(@Email(message = "email.must.be.valid") @Valid @PathVariable("email") String email) {
		log.debug("REST request to disable User: {}", email);
		try {
			return success(userService.activeUser(email));
		} catch (Exception e) {
			return failure(e);
		}
	}

	@PutMapping(value = "/settings/{email}")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "update user settings")
	public ResponseEntity<UserOutputDto> updateUserSettings(@RequestBody UserSettingDto userSettingDto,
															Authentication authentication) {
		log.debug("REST request to update User settings by email {} to {}", authentication.getName(), userSettingDto);
		try {
			UserOutputDto userOutputDto = userService.updateUserSettings(authentication.getName(), userSettingDto);
			return success(userOutputDto);
		} catch (Exception e) {
			return failure(e);
		}
	}
}
