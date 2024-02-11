package ir.snapp.pay.repository;

import ir.snapp.pay.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByNameAndUserId(String name, Long userId);

	@Query("SELECT COALESCE(SUM(t.amount), 0.0) as totalAmount, c.id as categoryId, c.name as categoryName " +
			"FROM Category c " +
			"LEFT JOIN Transaction t ON c = t.category AND t.user.id = :userId AND t.date BETWEEN :startDate AND :endDate " +
			"WHERE c.id <> :salaryCategoryId " +
			"GROUP BY c.id, c.name")
	List<TransactionSumByCategory> getTransactionSumByCategoryWithoutSalaryCategory(Long userId, Long salaryCategoryId,
																					Instant startDate, Instant endDate);

	@Query(value = "SELECT COALESCE(SUM(CASE WHEN t.type = tt.type THEN t.amount ELSE 0 END), 0) AS totalAmount, " +
			"       tt.type AS transactionType " +
			"FROM (SELECT 'EXPENSE' AS type UNION ALL SELECT 'INCOME') AS tt " +
			"LEFT JOIN pay.transaction t ON tt.type = t.type AND t.user_id = :userId AND t.date BETWEEN :startDate AND :endDate " +
			"GROUP BY tt.type "
			, nativeQuery = true)
	List<TransactionSumByTransactionType> getTransactionSumByTransactionType(Long userId, Instant startDate, Instant endDate);

	Optional<Category> findByIdAndUserId(Long categoryId, Long id);

	List<Category> findAllByUserId(Long userId);
}
