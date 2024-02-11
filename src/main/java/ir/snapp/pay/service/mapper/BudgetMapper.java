package ir.snapp.pay.service.mapper;


import ir.snapp.pay.domain.Budget;
import ir.snapp.pay.domain.Category;
import ir.snapp.pay.domain.User;
import ir.snapp.pay.dto.BudgetInputDto;
import ir.snapp.pay.dto.BudgetOutputDto;
import ir.snapp.pay.dto.CategoryOutputDto;
import ir.snapp.pay.dto.UserOutputDto;
import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.exception.ExpenseExceptionType;
import ir.snapp.pay.repository.CategoryRepository;
import ir.snapp.pay.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BudgetMapper {

	private UserRepository userRepository;
	private CategoryRepository categoryRepository;

	public Budget budgetInputStoToBudget(BudgetInputDto budgetInputDto, String userEmail) {
		Budget budget = new Budget();
		budget.setAmount(budgetInputDto.getAmount());
		User user = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		budget.setUser(user);
		Category category = categoryRepository.findById(budgetInputDto.getCategoryId())
				.orElseThrow(new ExpenseException(ExpenseExceptionType.CATEGORY_NOT_FOUND_EXCEPTION));
		budget.setCategory(category);
		return budget;
	}

	public BudgetOutputDto budgetToBudgetOutputDto(Budget budget) {
		return BudgetOutputDto.builder()
				.id(budget.getId())
				.amount(budget.getAmount())
				.category(getCategoryOutputDto(budget.getCategory()))
				.user(getUserOutputDto(budget.getUser()))
				.build();
	}

	public List<BudgetOutputDto> budgetToBudgetOutputDto(List<Budget> budgets) {
		return budgets.stream().map(this::budgetToBudgetOutputDto).collect(Collectors.toList());
	}

	private CategoryOutputDto getCategoryOutputDto(Category category) {
		return CategoryOutputDto.builder()
				.id(category.getId())
				.name(category.getName())
				.build();
	}

	private UserOutputDto getUserOutputDto(User user) {
		return UserOutputDto.builder()
				.id(user.getId())
				.email(user.getEmail())
				.build();
	}
}
