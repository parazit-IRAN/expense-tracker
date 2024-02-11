package ir.snapp.pay.dto;


import ir.snapp.pay.constant.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionOutputDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Instant date;
	private BigDecimal amount;
	private String description;
	private TransactionType type;
	private CategoryOutputDto category;
	private UserOutputDto user;
	private AccountOutputDto account;
}
