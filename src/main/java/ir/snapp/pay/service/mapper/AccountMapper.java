package ir.snapp.pay.service.mapper;


import ir.snapp.pay.domain.Account;
import ir.snapp.pay.dto.AccountOutputDto;
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
}
