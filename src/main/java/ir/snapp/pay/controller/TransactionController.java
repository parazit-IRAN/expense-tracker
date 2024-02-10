package ir.snapp.pay.controller;


import ir.snapp.pay.constant.Constants;
import ir.snapp.pay.dto.TransactionInputDto;
import ir.snapp.pay.dto.TransactionOutputDto;
import ir.snapp.pay.dto.UserOutputDto;
import ir.snapp.pay.service.TransactionService;
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
@RequestMapping("/transactions")
public class TransactionController extends BaseController {

	private final TransactionService transactionService;

	@PostMapping
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	public ResponseEntity<UserOutputDto> createTransaction(@Valid @RequestBody TransactionInputDto transactionInputDto,
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
	public ResponseEntity<String> deleteUser(@Valid @PathVariable("id") Long transactionId, Authentication authentication) {
		log.debug("REST request to delete Transaction id :{}, user email: {}", transactionId, authentication.getName());
		try {
			transactionService.deleteTransaction(transactionId, authentication.getName());
			return success("Deleted Transaction id: " + transactionId);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@GetMapping(value = "/reports/all-transaction-by-user")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	public ResponseEntity<List<TransactionOutputDto>> getAllTransactionByUserId(Authentication authentication) {
		log.debug("REST request to getAllTransactionByUserId for user email: {}", authentication.getName());
		try {
			List<TransactionOutputDto> transactions = transactionService.getAllTransaction(authentication.getName());
			return success(transactions);
		} catch (Exception e) {
			return failure(e);
		}
	}
}
