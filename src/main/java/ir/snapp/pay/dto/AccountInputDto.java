package ir.snapp.pay.dto;


import ir.snapp.pay.constant.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInputDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "name.must.not.be.null")
	@NotEmpty(message = "name.must.not.be.empty")
	private String name;
	@NotNull(message = "type.must.not.be.null")
	private AccountType type;
	@NotNull(message = "currency.must.not.be.null")
	@NotEmpty(message = "currency.must.not.be.empty")
	private String currency;
	private String description;
	@NotNull(message = "balance.must.not.be.null")
	@Digits(integer = 10, fraction = 4, message = "balance.size.must.be.between.val1.and.val2")
	private BigDecimal balance;
}
