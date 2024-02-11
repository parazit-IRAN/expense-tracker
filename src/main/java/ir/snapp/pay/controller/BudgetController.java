package ir.snapp.pay.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.snapp.pay.constant.Constants;
import ir.snapp.pay.dto.BudgetInputDto;
import ir.snapp.pay.dto.BudgetOutputDto;
import ir.snapp.pay.service.BudgetService;
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
@RequestMapping("/budgets")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Budget", description = "The Budget API. Contains all the operations that can be performed on a budget.")
public class BudgetController extends BaseController {
	private final BudgetService budgetService;

	@PostMapping
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@Operation(summary = "create an budget")
	public ResponseEntity<BudgetOutputDto> createBudget(@Valid @RequestBody BudgetInputDto budgetInputDto,
														Authentication authentication) {
		log.debug("REST request to save BudgetInputDto : {}, User Email: {}", budgetInputDto, authentication.getName());
		try {
			BudgetOutputDto budgetOutputDto = budgetService.createBudget(budgetInputDto, authentication.getName());
			return success(budgetOutputDto);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@Operation(summary = "get a budget by id")
	public ResponseEntity<BudgetOutputDto> getBudget(@PathVariable("id") Long id, Authentication authentication) {
		log.debug("REST request to get a Budget id : {}, User Email: {}", id, authentication.getName());
		try {
			BudgetOutputDto budgetOutputDto = budgetService.getBudget(id, authentication.getName());
			return success(budgetOutputDto);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@Operation(summary = "delete a budget")
	public ResponseEntity<String> deleteBudget(@Valid @PathVariable("id") Long id, Authentication authentication) {
		log.debug("REST request to delete Budget id: {}, User Email :{}", id, authentication.getName());
		try {
			budgetService.deleteBudget(id, authentication.getName());
			return success("Deleted Budget id: " + id);
		} catch (Exception e) {
			return failure(e);
		}
	}

	@GetMapping(value = "/all")
	@PreAuthorize("hasAuthority(\"" + Constants.USER + "\")")
	@Operation(summary = "get all budget for current user")
	public ResponseEntity<List<BudgetOutputDto>> getAllBudget(Authentication authentication) {
		log.debug("REST request to get all Budget for User Email: {}", authentication.getName());
		try {
			List<BudgetOutputDto> budgetOutputDtos = budgetService.getAllBudget(authentication.getName());
			return success(budgetOutputDtos);
		} catch (Exception e) {
			return failure(e);
		}
	}


}
