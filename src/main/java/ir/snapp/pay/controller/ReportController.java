package ir.snapp.pay.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.snapp.pay.constant.Constants;
import ir.snapp.pay.repository.TransactionSumByCategory;
import ir.snapp.pay.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/reports")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Report", description = "The Report API. Contains all the operations that show report.")
public class ReportController extends BaseController {

	private final CategoryService categoryService;

	@GetMapping(value = "/per-category")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@Operation(summary = "get report total amount per category")
	public ResponseEntity<List<TransactionSumByCategory>> createReportAllCategoriesWithTotalAmount(Authentication authentication
			, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
			, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		log.debug("REST request to createReportAllCategoriesWithTotalAmount User email: {}, start :{}, end :{} of date",
				authentication.getName(), startDate, endDate);

		try {
			return success(categoryService.createReportAllCategoriesWithTotalAmount(authentication.getName(), startDate, endDate));
		} catch (Exception e) {
			return failure(e);
		}
	}

	@GetMapping(value = "/expense-income")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@Operation(summary = "get report total amount per transaction type")
	public ResponseEntity<List<TransactionSumByCategory>> createReportTransactionSumByTransactionType(Authentication authentication
			, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
			, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		log.debug("REST request to createReportTransactionSumByTransactionType User Email: {}, start :{}, end :{} of date",
				authentication.getName(), startDate, endDate);
		try {
			return success(categoryService.createReportTransactionSumByTransactionType(authentication.getName(), startDate, endDate));
		} catch (Exception e) {
			return failure(e);
		}
	}

}
