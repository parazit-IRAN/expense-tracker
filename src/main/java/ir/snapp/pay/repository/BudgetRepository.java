package ir.snapp.pay.repository;

import ir.snapp.pay.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
	Optional<Budget> findByIdAndUserId(Long id, Long userId);

	List<Budget> findAllByUserId(Long userId);

	Optional<Budget> findBudgetByCategoryIdAndUserId(Long id, Long userId);
}
