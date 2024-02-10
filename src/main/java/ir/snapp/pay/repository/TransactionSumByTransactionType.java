package ir.snapp.pay.repository;

import ir.snapp.pay.constant.TransactionType;

import java.math.BigDecimal;

public interface TransactionSumByTransactionType {
	BigDecimal getTotalAmount();

	TransactionType getTransactionType();
}
