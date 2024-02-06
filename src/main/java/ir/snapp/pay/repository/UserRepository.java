package ir.snapp.pay.repository;

import ir.snapp.pay.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
