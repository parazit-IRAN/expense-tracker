package ir.snapp.pay.repository;

import ir.snapp.pay.domain.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
	Optional<Account> findByIdAndUserId(Long accountId, Long id);

	Page<Account> findAllByUserId(Long id, Pageable pageable);

	Optional<Account> findByMainAndUserId(boolean isMain, Long userId);
}
