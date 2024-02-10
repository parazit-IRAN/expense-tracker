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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/transactions")
public class TransactionController extends BaseController {

	private final TransactionService transactionService;

	@PostMapping
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	public ResponseEntity<UserOutputDto> createTransaction(@Valid @RequestBody TransactionInputDto transactionInputDto) {
		log.debug("REST request to save Transaction : {}", transactionInputDto);
		try {
			TransactionOutputDto transaction = transactionService.createTransaction(transactionInputDto);
			return success(transaction);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	public ResponseEntity<String> deleteUser(@Valid @PathVariable("id") Long transactionId) {
		log.debug("REST request to delete Transaction id: {}", transactionId);
		try {
			transactionService.deleteTransaction(transactionId);
			return success("Deleted Transaction id: " + transactionId);
		} catch (Exception e) {
			return failure(e);
		}
	}
}
