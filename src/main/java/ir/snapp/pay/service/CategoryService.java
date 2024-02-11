package ir.snapp.pay.service;


import ir.snapp.pay.domain.Category;
import ir.snapp.pay.domain.User;
import ir.snapp.pay.dto.CategoryInputDto;
import ir.snapp.pay.dto.CategoryOutputDto;
import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.exception.ExpenseExceptionType;
import ir.snapp.pay.repository.CategoryRepository;
import ir.snapp.pay.repository.TransactionSumByCategory;
import ir.snapp.pay.repository.TransactionSumByTransactionType;
import ir.snapp.pay.repository.UserRepository;
import ir.snapp.pay.service.mapper.CategoryMapper;
import ir.snapp.pay.util.GeneralUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;
	private final UserRepository userRepository;

	@Transactional
	public CategoryOutputDto createCategory(CategoryInputDto categoryInputDto, String userEmail) {
		Category category = categoryMapper.categoryInputDtoToCategory(categoryInputDto);
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		category.setUser(currentUser);
		categoryRepository.save(category);
		log.debug("Created An Category: {}", category);
		return categoryMapper.categoryToCategoryOutputDto(category);
	}

	@Transactional
	public void deleteCategory(Long categoryId, String userEmail) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		Category category = categoryRepository.findByIdAndUserId(categoryId, currentUser.getId())
				.orElseThrow(new ExpenseException(ExpenseExceptionType.CATEGORY_NOT_FOUND_EXCEPTION));
		categoryRepository.delete(category);
		log.debug("Deleted An Category: {}", category);
	}

	@Transactional(readOnly = true)
	public List<TransactionSumByCategory> createReportAllCategoriesWithTotalAmount(String userEmail,
																				   LocalDate startDate,
																				   LocalDate endDate) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		Category category = categoryRepository.findByNameAndUserId("Salary", currentUser.getId())
				.orElseThrow(new ExpenseException(ExpenseExceptionType.CATEGORY_NOT_FOUND_EXCEPTION));
		List<TransactionSumByCategory> transactionSumByCategory =
				categoryRepository.getTransactionSumByCategoryWithoutSalaryCategory(
						currentUser.getId(),
						category.getId(),
						GeneralUtil.convert(startDate),
						GeneralUtil.convert(endDate));
		return transactionSumByCategory;
	}

	@Transactional(readOnly = true)
	public List<TransactionSumByTransactionType> createReportTransactionSumByTransactionType(String userEmail,
																							 LocalDate startDate,
																							 LocalDate endDate) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		return categoryRepository.getTransactionSumByTransactionType(currentUser.getId(),
				GeneralUtil.convert(startDate),
				GeneralUtil.convert(endDate));
	}

	@Transactional
	public void createDefaultCategory(User user) {
		Category salary = createCategory("Salary", user);
		Category leisure = createCategory("Leisure", user);
		Category restaurant = createCategory("Restaurant", user);
		Category groceries = createCategory("Groceries", user);
		Category health = createCategory("Health", user);
		Category gift = createCategory("Gift", user);
		Category family = createCategory("Family", user);
		Category shipping = createCategory("Shipping", user);
		Category transport = createCategory("Transport", user);
		categoryRepository.saveAll(List.of(salary, leisure, restaurant, groceries, health, gift, family, shipping, transport));
	}

	private Category createCategory(String name, User user) {
		return Category.builder()
				.name(name)
				.user(user)
				.build();
	}

	@Transactional(readOnly = true)
	public List<CategoryOutputDto> getAllCategory(String userEmail) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		return categoryMapper.categoryToCategoryOutputDto(categoryRepository.findAllByUserId(currentUser.getId()));
	}

	@Transactional(readOnly = true)
	public CategoryOutputDto getCategory(Long id, String userEmail) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		Category category = categoryRepository.findByIdAndUserId(id, currentUser.getId())
				.orElseThrow(new ExpenseException(ExpenseExceptionType.CATEGORY_NOT_FOUND_EXCEPTION));
		return categoryMapper.categoryToCategoryOutputDto(category);
	}
}


