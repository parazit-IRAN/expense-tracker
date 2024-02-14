package ir.snapp.pay.service;


import ir.snapp.pay.domain.Budget;
import ir.snapp.pay.domain.User;
import ir.snapp.pay.dto.BudgetInputDto;
import ir.snapp.pay.dto.BudgetOutputDto;
import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.exception.ExpenseExceptionType;
import ir.snapp.pay.repository.BudgetRepository;
import ir.snapp.pay.repository.UserRepository;
import ir.snapp.pay.service.mapper.BudgetMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@AllArgsConstructor
public class BudgetService {
	private final BudgetRepository budgetRepository;
	private final UserRepository userRepository;
	private final BudgetMapper budgetMapper;

	@Transactional
	public BudgetOutputDto createBudget(BudgetInputDto budgetInputDto, String userEmail) {
		Budget budget = budgetMapper.budgetInputStoToBudget(budgetInputDto, userEmail);
		budgetRepository.save(budget);
		log.debug("Created An Budget: {}", budget);
		return budgetMapper.budgetToBudgetOutputDto(budget);
	}

	@Transactional
	public void deleteBudget(Long id, String userEmail) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		Budget budget = budgetRepository.findByIdAndUserId(id, currentUser.getId()).orElseThrow(new ExpenseException(ExpenseExceptionType.BUDGET_NOT_FOUND_EXCEPTION));
		budgetRepository.delete(budget);
		log.debug("Deleted Budget: {}", budget);
	}

	@Transactional(readOnly = true)
	public BudgetOutputDto getBudget(Long id, String userEmail) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		Budget budget = budgetRepository.findByIdAndUserId(id, currentUser.getId())
				.orElseThrow(new ExpenseException(ExpenseExceptionType.BUDGET_NOT_FOUND_EXCEPTION));
		return budgetMapper.budgetToBudgetOutputDto(budget);
	}


	@Transactional(readOnly = true)
	public Page<BudgetOutputDto> getAllBudget(String userEmail, PageRequest pageRequest) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.BUDGET_NOT_FOUND_EXCEPTION));
		Page<Budget> budgets = budgetRepository.findAllByUserId(currentUser.getId(), pageRequest);
		return new PageImpl(budgetMapper.budgetToBudgetOutputDto(
				budgets.getContent()),
				pageRequest,
				budgets.getTotalElements());
	}
}
