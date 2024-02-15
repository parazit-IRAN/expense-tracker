package ir.snapp.pay.service;


import ir.snapp.pay.constant.CategoryConstants;
import ir.snapp.pay.constant.TransactionType;
import ir.snapp.pay.domain.Budget;
import ir.snapp.pay.domain.Category;
import ir.snapp.pay.domain.Transaction;
import ir.snapp.pay.domain.User;
import ir.snapp.pay.dto.TransactionInputDto;
import ir.snapp.pay.dto.TransactionOutputDto;
import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.exception.ExpenseExceptionType;
import ir.snapp.pay.provider.sms.NotificationSender;
import ir.snapp.pay.repository.BudgetRepository;
import ir.snapp.pay.repository.CategoryRepository;
import ir.snapp.pay.repository.TransactionRepository;
import ir.snapp.pay.repository.UserRepository;
import ir.snapp.pay.service.mapper.TransactionMapper;
import ir.snapp.pay.util.MessageTranslatorUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;


@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final TransactionMapper transactionMapper;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;
	private final BudgetRepository budgetRepository;
	private final NotificationSender notificationSender;


	@Transactional
	public TransactionOutputDto createTransaction(TransactionInputDto transactionInputDto, String userEmail) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		Transaction transaction = transactionMapper.transactionInputDtoToTransaction(transactionInputDto);
		transaction.setUser(currentUser);
		isValidTransaction(transaction, currentUser);
		sendAlarmByBudget(transaction.getCategory(), transaction.getUser(), transaction.getAmount());
		transactionRepository.save(transaction);
		log.debug("Created An Transaction: {}", transaction);
		return transactionMapper.transactionToTransactionOutputDto(transaction);
	}

	private void sendAlarmByBudget(Category category, User user, BigDecimal amount) {
		Optional<Budget> budgetOptional = budgetRepository.findBudgetByCategoryIdAndUserId(category.getId(), user.getId());

		if (budgetOptional.isPresent() && budgetOptional.get().getAmount().compareTo(amount) <= 0) {
			String text = MessageTranslatorUtil.getText("app.notification.text", category.getName());
			notificationSender.sendNotification(user.getEmail(), text);
		}
	}

	private void isValidTransaction(Transaction transaction, User user) {
		Category category = categoryRepository.findByNameAndUserId(CategoryConstants.SALARY.getName(), user.getId())
				.orElseThrow(new ExpenseException(ExpenseExceptionType.CATEGORY_NOT_FOUND_EXCEPTION));
		if ((transaction.getType() == TransactionType.EXPENSE && transaction.getCategory().getId().equals(category.getId())) ||
				(transaction.getType() == TransactionType.INCOME && !transaction.getCategory().getId().equals(category.getId()))) {
			throw new ExpenseException(ExpenseExceptionType.TRANSACTION_IS_NOT_VALID_EXCEPTION);
		}
	}

	@Transactional
	public void deleteTransaction(Long transactionId, String userEmail) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, currentUser.getId())
				.orElseThrow(new ExpenseException(ExpenseExceptionType.TRANSACTION_NOT_FOUND_EXCEPTION));
		transactionRepository.delete(transaction);
		log.debug("Deleted Transaction: {}", transaction);
	}

	@Transactional(readOnly = true)
	public Page<TransactionOutputDto> getAllTransaction(String userEmail, PageRequest pageRequest) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		Page<Transaction> transactions = transactionRepository.findAllByUserId(currentUser.getId(), pageRequest);
		return new PageImpl(transactionMapper.transactionToTransactionOutputDto(
				transactions.getContent()),
				pageRequest,
				transactions.getTotalElements());
	}

	@Transactional(readOnly = true)
	public TransactionOutputDto getTransaction(Long id, String userEmail) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		Transaction transaction = transactionRepository.findByIdAndUserId(id, currentUser.getId())
				.orElseThrow(new ExpenseException(ExpenseExceptionType.TRANSACTION_NOT_FOUND_EXCEPTION));
		return transactionMapper.transactionToTransactionOutputDto(transaction);
	}
}
