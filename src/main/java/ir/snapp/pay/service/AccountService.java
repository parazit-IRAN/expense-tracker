package ir.snapp.pay.service;


import ir.snapp.pay.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;
}
