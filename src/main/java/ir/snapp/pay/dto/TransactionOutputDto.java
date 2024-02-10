package ir.snapp.pay.dto;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class TransactionOutputDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Instant date;
	private BigDecimal amount;
	private String description;
	private String type;
	private String categoryName;
	private String userEmail;
	private String accountName;
}
