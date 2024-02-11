package ir.snapp.pay.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.snapp.pay.constant.Constants;
import ir.snapp.pay.dto.TransactionInputDto;
import ir.snapp.pay.dto.TransactionOutputDto;
import ir.snapp.pay.filter.PageRequestBuilder;
import ir.snapp.pay.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/transactions")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Transaction", description = "The Transaction API. Contains all the operations that can be performed on a transaction.")
public class TransactionController extends BaseController {

	private final TransactionService transactionService;

	@PostMapping
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@Operation(summary = "create a transaction")
	public ResponseEntity<TransactionOutputDto> createTransaction(@Valid @RequestBody TransactionInputDto transactionInputDto,
																  Authentication authentication) {
		log.debug("REST request to save Transaction : {}, User Email: {}", transactionInputDto, authentication.getName());
		try {
			TransactionOutputDto transaction = transactionService.createTransaction(transactionInputDto, authentication.getName());
			return success(transaction);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@Operation(summary = "delete a transaction")
	public ResponseEntity<String> deleteTransaction(@Valid @PathVariable("id") Long transactionId, Authentication authentication) {
		log.debug("REST request to delete Transaction id :{}, user email: {}", transactionId, authentication.getName());
		try {
			transactionService.deleteTransaction(transactionId, authentication.getName());
			return success("Deleted Transaction id: " + transactionId);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@GetMapping(value = "/all")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@Operation(summary = "get all transaction for current user")
	public ResponseEntity<Page<TransactionOutputDto>> getAllTransactionByUserId(Authentication authentication,
																				@RequestParam(required = false) Integer size,
																				@RequestParam(required = false) Integer page,
																				@RequestParam(required = false) String sort) {
		log.debug("REST request to getAllTransactionByUserId for user email: {}", authentication.getName());
		try {
			PageRequest pageRequest = PageRequestBuilder.getPageRequest(size, page, sort);
			Page<TransactionOutputDto> transactions = transactionService.getAllTransaction(authentication.getName(), pageRequest);
			return success(transactions);
		} catch (Exception e) {
			return failure(e);
		}
	}
}
