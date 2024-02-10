package ir.snapp.pay.controller;


import ir.snapp.pay.constant.Constants;
import ir.snapp.pay.dto.CategoryInputDto;
import ir.snapp.pay.dto.CategoryOutputDto;
import ir.snapp.pay.repository.TransactionSumByCategory;
import ir.snapp.pay.dto.UserOutputDto;
import ir.snapp.pay.service.CategoryService;
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
@RequestMapping("/categories")
public class CategoryController extends BaseController {

	private final CategoryService categoryService;


	@PostMapping
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	public ResponseEntity<UserOutputDto> createCategory(@Valid @RequestBody CategoryInputDto categoryInputDto,
														Authentication authentication) {
		log.debug("REST request to save Category : {}, User Email: {}", categoryInputDto, authentication.getName());
		try {
			CategoryOutputDto categoryOutputDto = categoryService.createCategory(categoryInputDto, authentication.getName());
			return success(categoryOutputDto);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	public ResponseEntity<String> deleteCategory(@Valid @PathVariable("id") Long categoryId, Authentication authentication) {
		log.debug("REST request to delete Category id: {}, User Email :{}", categoryId, authentication.getName());
		try {
			categoryService.deleteCategory(categoryId, authentication.getName());
			return success("Deleted Category id: " + categoryId);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@GetMapping(value = "/reports/per-category")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	public ResponseEntity<List<TransactionSumByCategory>> createReportAllCategoriesWithTotalAmount(Authentication authentication) {
		log.debug("REST request to createReportAllCategoriesWithTotalAmount User email: {}",
				authentication.getName());
		try {
			return success(categoryService.createReportAllCategoriesWithTotalAmount(authentication.getName()));
		} catch (Exception e) {
			return failure(e);
		}
	}

	@GetMapping(value = "/reports/expense-income")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	public ResponseEntity<List<TransactionSumByCategory>> createReportTransactionSumByTransactionType(Authentication authentication) {
		log.debug("REST request to createReportTransactionSumByTransactionType User Email: {}", authentication.getName());
		try {
			return success(categoryService.createReportTransactionSumByTransactionType(authentication.getName()));
		} catch (Exception e) {
			return failure(e);
		}
	}

}
