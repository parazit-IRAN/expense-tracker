package ir.snapp.pay.repository;

import ir.snapp.pay.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByNameAndUserId(String name, Long userId);

	@Query("SELECT SUM(t.amount) as totalAmount, t.category.id as categoryId, t.category.name as categoryName " +
			"FROM Transaction t " +
			"JOIN t.category c " +
			"WHERE t.user.id = :userId AND t.category.id <> :salaryCategoryId " +
			"GROUP BY t.category.id, t.category.name")
	List<TransactionSumByCategory> getTransactionSumByCategoryWithoutSalaryCategory(Long userId, Long salaryCategoryId);

	@Query("SELECT SUM(t.amount) as totalAmount, t.type as transactionType " +
			"FROM Transaction t " +
			"WHERE t.user.id = :userId " +
			"GROUP BY t.type")
	List<TransactionSumByTransactionType> getTransactionSumByTransactionType(Long userId);

	Optional<Category> findByIdAndUserId(Long categoryId, Long id);
}
