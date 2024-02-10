package ir.snapp.pay.repository;

import ir.snapp.pay.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
