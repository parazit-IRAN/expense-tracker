package ir.snapp.pay.service;


import ir.snapp.pay.constant.TransactionType;
import ir.snapp.pay.domain.Category;
import ir.snapp.pay.domain.Transaction;
import ir.snapp.pay.domain.User;
import ir.snapp.pay.dto.TransactionInputDto;
import ir.snapp.pay.dto.TransactionOutputDto;
import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.exception.ExpenseExceptionType;
import ir.snapp.pay.repository.CategoryRepository;
import ir.snapp.pay.repository.TransactionRepository;
import ir.snapp.pay.repository.UserRepository;
import ir.snapp.pay.service.mapper.TransactionMapper;
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
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final TransactionMapper transactionMapper;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;


	@Transactional
	public TransactionOutputDto createTransaction(TransactionInputDto transactionInputDto, String userEmail) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		Transaction transaction = transactionMapper.transactionInputDtoToTransaction(transactionInputDto);
		isValidTransaction(transaction, currentUser);
		transactionRepository.save(transaction);
		log.debug("Created An Transaction: {}", transaction);
		return transactionMapper.transactionToTransactionOutputDto(transaction);
	}

	private void isValidTransaction(Transaction transaction, User user) {
		Category category = categoryRepository.findByNameAndUserId("Salary", user.getId())
				.orElseThrow(new ExpenseException(ExpenseExceptionType.CATEGORY_NOT_FOUND_EXCEPTION));
		if (transaction.getType().equals(TransactionType.INCOME) && transaction.getCategory().getId() != category.getId()) {
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

	public Page<TransactionOutputDto> getAllTransaction(String userEmail, PageRequest pageRequest) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		Page<Transaction> transactions = transactionRepository.findAllByUserId(currentUser.getId(), pageRequest);
		return new PageImpl(transactionMapper.transactionToTransactionOutputDto(
				transactions.getContent()),
				pageRequest,
				transactions.getTotalElements());
	}
}
