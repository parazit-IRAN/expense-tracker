package ir.snapp.pay.repository;

import ir.snapp.pay.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	List<Transaction> findAllByUserId(Long userId);

	Optional<Transaction> findByIdAndUserId(Long transactionId, Long userId);
}
