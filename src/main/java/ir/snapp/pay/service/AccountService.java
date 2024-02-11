package ir.snapp.pay.service;


import ir.snapp.pay.domain.Account;
import ir.snapp.pay.domain.User;
import ir.snapp.pay.dto.AccountInputDto;
import ir.snapp.pay.dto.AccountOutputDto;
import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.exception.ExpenseExceptionType;
import ir.snapp.pay.repository.AccountRepository;
import ir.snapp.pay.repository.UserRepository;
import ir.snapp.pay.service.mapper.AccountMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;
	private final AccountMapper accountMapper;
	private final UserRepository userRepository;

	@Transactional
	public AccountOutputDto createAccount(AccountInputDto accountInputDto, String userEmail) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		Account account = accountMapper.accountInputDtoToAccount(accountInputDto);
		account.setUser(currentUser);
		accountRepository.save(account);
		return accountMapper.accountToAccountOutputDto(account);
	}

	@Transactional
	public void deleteAccount(Long accountId, String userEmail) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		Account account = accountRepository.findByIdAndUserId(accountId, currentUser.getId())
				.orElseThrow(new ExpenseException(ExpenseExceptionType.TRANSACTION_NOT_FOUND_EXCEPTION));
		accountRepository.delete(account);
		log.debug("Deleted Account: {}", account);
	}

	public List<AccountOutputDto> getAllAccount(String userEmail) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		List<Account> accounts = accountRepository.findAllByUserId(currentUser.getId());
		return accountMapper.accountToAccountOutputDto(accounts);
	}
}
