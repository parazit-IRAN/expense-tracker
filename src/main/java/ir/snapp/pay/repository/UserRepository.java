package ir.snapp.pay.repository;

import ir.snapp.pay.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findOneByEmailIgnoreCase(String email);
	Optional<User> findOneByEmailIgnoreCaseAndActivated(String email, boolean disable);
}
