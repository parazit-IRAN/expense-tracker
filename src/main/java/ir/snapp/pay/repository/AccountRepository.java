package ir.snapp.pay.repository;

import ir.snapp.pay.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
	Optional<Account> findByIdAndUserId(Long accountId, Long id);
}
