package ir.snapp.pay.service.mapper;

import ir.snapp.pay.constant.TransactionType;
import ir.snapp.pay.domain.Account;
import ir.snapp.pay.domain.Category;
import ir.snapp.pay.domain.Transaction;
import ir.snapp.pay.domain.User;
import ir.snapp.pay.dto.*;
import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.exception.ExpenseExceptionType;
import ir.snapp.pay.repository.AccountRepository;
import ir.snapp.pay.repository.CategoryRepository;
import ir.snapp.pay.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionMapper {

	private UserRepository userRepository;
	private AccountRepository accountRepository;
	private CategoryRepository categoryRepository;

	public Transaction transactionInputDtoToTransaction(TransactionInputDto transactionInputDto) {
		Transaction transaction = new Transaction();
		transaction.setAmount(transactionInputDto.getAmount());
		User user = userRepository.findById(transactionInputDto.getUserId())
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		transaction.setUser(user);
		Account account = accountRepository.findById(transactionInputDto.getAccountId())
				.orElseThrow(new ExpenseException(ExpenseExceptionType.ACCOUNT_NOT_FOUND_EXCEPTION));
		transaction.setAccount(account);
		Category category = categoryRepository.findById(transactionInputDto.getCategoryId()).orElseThrow(new ExpenseException(ExpenseExceptionType.CATEGORY_NOT_FOUND_EXCEPTION));
		transaction.setCategory(category);
		transaction.setDescription(transactionInputDto.getDescription());
		transaction.setType(TransactionType.valueOf(transactionInputDto.getType()));
		return transaction;
	}

	public TransactionOutputDto transactionToTransactionOutputDto(Transaction transaction) {
		return TransactionOutputDto.builder()
				.id(transaction.getId())
				.date(transaction.getDate())
				.amount(transaction.getAmount())
				.description(transaction.getDescription())
				.category(getCategoryOutputDto(transaction.getCategory()))
				.user(createUserOutputDto(transaction.getUser()))
				.account(createAccountOutputDto(transaction.getAccount()))
				.type(transaction.getType().name())
				.build();
	}

	private AccountOutputDto createAccountOutputDto(Account account) {
		return AccountOutputDto.builder()
				.id(account.getId())
				.name(account.getName())
				.type(account.getType().name())
				.build();
	}

	private UserOutputDto createUserOutputDto(User user) {
		return UserOutputDto.builder()
				.id(user.getId())
				.email(user.getEmail())
				.build();
	}

	private CategoryOutputDto getCategoryOutputDto(Category category) {
		return CategoryOutputDto.builder()
				.id(category.getId())
				.name(category.getName())
				.build();
	}

	public List<TransactionOutputDto> transactionToTransactionOutputDto(List<Transaction> transactions) {
		return transactions.stream().map(this::transactionToTransactionOutputDto).collect(Collectors.toList());
	}


}
