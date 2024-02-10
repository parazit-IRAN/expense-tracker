package ir.snapp.pay.repository;

import java.math.BigDecimal;

public interface TransactionSumByCategory {
	BigDecimal getTotalAmount();
	Long getCategoryId();
	String getCategoryName();
}
