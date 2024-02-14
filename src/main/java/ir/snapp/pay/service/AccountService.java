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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
		isMainChecking(currentUser, account);
		accountRepository.save(account);
		return accountMapper.accountToAccountOutputDto(account);
	}

	private void isMainChecking(User currentUser, Account account) {
		if (accountRepository.findByMainAndUserId(true, currentUser.getId()).isPresent()) {
			account.setMain(false);
		}
	}

	@Transactional
	public void deleteAccount(Long accountId, String userEmail) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		Account account = accountRepository.findByIdAndUserId(accountId, currentUser.getId())
				.orElseThrow(new ExpenseException(ExpenseExceptionType.ACCOUNT_NOT_FOUND_EXCEPTION));
		accountRepository.delete(account);
		log.debug("Deleted Account: {}", account);
	}

	@Transactional(readOnly = true)
	public Page<AccountOutputDto> getAllAccount(String userEmail, PageRequest pageRequest) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		Page<Account> accounts = accountRepository.findAllByUserId(currentUser.getId(), pageRequest);
		return new PageImpl(accountMapper.accountToAccountOutputDto(
				accounts.getContent()),
				pageRequest,
				accounts.getTotalElements());
	}

	@Transactional(readOnly = true)
	public AccountOutputDto getAccount(Long id, String userEmail) {
		User currentUser = userRepository.findOneByEmailIgnoreCase(userEmail)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION));
		Account account = accountRepository.findByIdAndUserId(id, currentUser.getId())
				.orElseThrow(new ExpenseException(ExpenseExceptionType.ACCOUNT_NOT_FOUND_EXCEPTION));
		return accountMapper.accountToAccountOutputDto(account);
	}
}
