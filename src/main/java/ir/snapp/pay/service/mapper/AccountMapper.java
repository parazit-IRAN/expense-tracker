package ir.snapp.pay.service.mapper;


import ir.snapp.pay.constant.AccountType;
import ir.snapp.pay.domain.Account;
import ir.snapp.pay.dto.AccountInputDto;
import ir.snapp.pay.dto.AccountOutputDto;
import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.exception.ExpenseExceptionType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountMapper {

	private final UserMapper userMapper;
	private final TransactionMapper transactionMapper;

	public AccountOutputDto accountToAccountOutputDto(Account account) {
		return AccountOutputDto.builder()
				.id(account.getId())
				.name(account.getName())
				.type(account.getType().name())
				.description(account.getDescription())
				.balance(account.getBalance())
				.currency(account.getCurrency())
				.user(userMapper.userToUserOutputDto(account.getUser()))
				.transactions(transactionMapper.transactionToTransactionOutputDto(account.getTransactions()))
				.build();
	}

	public Account accountInputDtoToAccount(AccountInputDto accountInputDto) {
		Account account = new Account();
		account.setName(accountInputDto.getName());
		account.setType(getAccountType(accountInputDto));
		account.setBalance(accountInputDto.getBalance());
		account.setCurrency(accountInputDto.getCurrency());
		return account;
	}

	private static AccountType getAccountType(AccountInputDto accountInputDto) {
		try {
			return AccountType.valueOf(accountInputDto.getType());
		} catch (Exception e) {
			throw new ExpenseException(ExpenseExceptionType.ACCOUNT_TYPE_NOT_FOUND_EXCEPTION);
		}
	}
}
