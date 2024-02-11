package ir.snapp.pay.service.mapper;


import ir.snapp.pay.domain.Account;
import ir.snapp.pay.dto.AccountInputDto;
import ir.snapp.pay.dto.AccountOutputDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountMapper {

	private final UserMapper userMapper;
	private final TransactionMapper transactionMapper;

	public AccountOutputDto accountToAccountOutputDto(Account account) {
		return AccountOutputDto.builder()
				.id(account.getId())
				.name(account.getName())
				.type(account.getType())
				.description(account.getDescription())
				.balance(account.getBalance())
				.currency(account.getCurrency())
				.build();
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
