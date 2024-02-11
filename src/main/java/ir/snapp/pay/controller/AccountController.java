package ir.snapp.pay.controller;


import ir.snapp.pay.constant.Constants;
import ir.snapp.pay.dto.*;
import ir.snapp.pay.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/accounts")
public class AccountController extends BaseController {

	private final AccountService accountService;

	@PostMapping
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
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

}
