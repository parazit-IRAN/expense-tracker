package ir.snapp.pay.dto;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class AccountOutputDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String type;
	private String currency;
	private String description;
	private BigDecimal balance;
	private UserOutputDto user;
	private List<TransactionOutputDto> transactions;
}
