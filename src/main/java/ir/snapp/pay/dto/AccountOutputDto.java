package ir.snapp.pay.dto;


import ir.snapp.pay.constant.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountOutputDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private AccountType type;
	private String currency;
	private String description;
	private BigDecimal balance;
}
