package ir.snapp.pay.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.snapp.pay.constant.Constants;
import ir.snapp.pay.dto.AccountInputDto;
import ir.snapp.pay.dto.AccountOutputDto;
import ir.snapp.pay.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/accounts")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Account", description = "The Account API. Contains all the operations that can be performed on a account.")
public class AccountController extends BaseController {

	private final AccountService accountService;

	@PostMapping
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@Operation(summary = "create an account")
	public ResponseEntity<AccountOutputDto> createAccount(@Valid @RequestBody AccountInputDto accountInputDto,
														  Authentication authentication) {
		log.debug("REST request to save AccountInputDto : {}, User Email: {}", accountInputDto, authentication.getName());
		try {
			AccountOutputDto accountOutputDto = accountService.createAccount(accountInputDto, authentication.getName());
			return success(accountOutputDto);
		} catch (Exception e) {
			return failure(e);
		}
	}


	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@Operation(summary = "delete an account")
	public ResponseEntity<String> deleteAccount(@Valid @PathVariable("id") Long accountId,
												Authentication authentication) {
		log.debug("REST request to delete Account Id : {}, User Email: {}", accountId, authentication.getName());
		try {
			accountService.deleteAccount(accountId, authentication.getName());
			return success("Deleted Account id: " + accountId);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@GetMapping(value = "/all")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@Operation(summary = "get all accounts for current user")
	public ResponseEntity<List<AccountOutputDto>> getAllAccount(Authentication authentication) {
		log.debug("REST request to get all Account for User Email: {}", authentication.getName());
		try {
			List<AccountOutputDto> accountOutputDtos = accountService.getAllAccount(authentication.getName());
			return success(accountOutputDtos);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@Operation(summary = "get a account by id")
	public ResponseEntity<AccountOutputDto> getAccount(@PathVariable("id") Long id, Authentication authentication) {
		log.debug("REST request to get a account id : {}, User Email: {}", id, authentication.getName());
		try {
			AccountOutputDto accountOutputDto = accountService.getAccount(id, authentication.getName());
			return success(accountOutputDto);
		} catch (Exception e) {
			return failure(e);
		}
	}

}
