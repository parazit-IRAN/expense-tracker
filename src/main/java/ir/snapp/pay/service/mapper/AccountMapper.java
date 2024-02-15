package ir.snapp.pay.service.mapper;


import ir.snapp.pay.domain.Account;
import ir.snapp.pay.dto.AccountInputDto;
import ir.snapp.pay.dto.AccountOutputDto;
import ir.snapp.pay.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountMapper {

	private final TransactionRepository transactionRepository;

	public AccountOutputDto accountToAccountOutputDto(Account account) {

		BigDecimal balance = getTotalAmountCalculate(account);

		return AccountOutputDto.builder()
				.id(account.getId())
				.name(account.getName())
				.type(account.getType())
				.description(account.getDescription())
				.balance(balance)
				.currency(account.getCurrency())
				.build();
	}

	private BigDecimal getTotalAmountCalculate(Account account) {
		BigDecimal totalExpenseAmount = transactionRepository.sumExpenseTotalAmountByAccountAndUserId(account.getId(), account.getUser().getId());
		BigDecimal totalIncomeAmount = transactionRepository.sumIncomeTotalAmountByAccountAndUserId(account.getId(), account.getUser().getId());

		if (totalIncomeAmount == null) {
			totalIncomeAmount = BigDecimal.ZERO;
		}

		if (totalExpenseAmount == null) {
			totalExpenseAmount = BigDecimal.ZERO;
		}
		BigDecimal balance = account.getBalance();

		balance = balance.subtract(totalExpenseAmount);
		balance = balance.add(totalIncomeAmount);
		return balance;
	}

	public List<AccountOutputDto> accountToAccountOutputDto(List<Account> accounts) {
		return accounts.stream().map(this::accountToAccountOutputDto).collect(Collectors.toList());
	}

	public Account accountInputDtoToAccount(AccountInputDto accountInputDto) {
		Account account = new Account();
		account.setName(accountInputDto.getName());
		account.setType(accountInputDto.getType());
		account.setBalance(accountInputDto.getBalance());
		account.setCurrency(accountInputDto.getCurrency());
		return account;
	}

}
