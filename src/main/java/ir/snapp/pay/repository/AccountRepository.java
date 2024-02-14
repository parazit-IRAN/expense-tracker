package ir.snapp.pay.repository;

import ir.snapp.pay.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
	Optional<Account> findByIdAndUserId(Long accountId, Long id);

	List<Account> findAllByUserId(Long id);

	Optional<Account> findByMainAndUserId(boolean isMain, Long userId);
}
