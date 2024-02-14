package ir.snapp.pay.repository;

import ir.snapp.pay.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	Page<Transaction> findAllByUserId(Long userId, Pageable pageable);

	Optional<Transaction> findByIdAndUserId(Long transactionId, Long userId);

	@Query("SELECT COALESCE(SUM(t.amount), 0.0) " +
			"FROM Transaction t " +
			"WHERE t.user.id = :userId AND t.account.id= :accountId AND t.type = 'EXPENSE' " +
			"GROUP BY t.user.id, t.account.id")
	BigDecimal sumExpenseTotalAmountByAccountAndUserId(Long accountId, Long userId);

	@Query("SELECT COALESCE(SUM(t.amount), 0.0) " +
			"FROM Transaction t " +
			"WHERE t.user.id = :userId AND t.account.id= :accountId AND t.type = 'INCOME' " +
			"GROUP BY t.user.id, t.account.id")
	BigDecimal sumIncomeTotalAmountByAccountAndUserId(Long accountId, Long userId);
}
