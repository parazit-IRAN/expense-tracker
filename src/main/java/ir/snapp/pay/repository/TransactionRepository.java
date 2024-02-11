package ir.snapp.pay.repository;

import ir.snapp.pay.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	Page<Transaction> findAllByUserId(Long userId, Pageable pageable);

	Optional<Transaction> findByIdAndUserId(Long transactionId, Long userId);
}
