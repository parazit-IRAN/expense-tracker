package ir.snapp.pay.dto;


import ir.snapp.pay.constant.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionInputDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "amount.must.not.be.null")
	@Digits(integer = 10, fraction = 4, message = "amount.size.must.be.between.val1.and.val2")
	private BigDecimal amount;

	private String description;

	@NotNull(message = "type.must.not.be.null")
	private TransactionType type;

	@NotNull(message = "categoryId.must.not.be.null")
	@Range(min = 1, max = Integer.MAX_VALUE - 1, message = "categoryId.size.must.be.between.val1.and.val2")
	private Long categoryId;

	@NotNull(message = "userId.must.not.be.null")
	@Range(min = 1, max = Integer.MAX_VALUE - 1, message = "userId.size.must.be.between.val1.and.val2")
	private Long userId;

	@NotNull(message = "accountId.must.not.be.null")
	@Range(min = 1, max = Integer.MAX_VALUE - 1, message = "accountId.size.must.be.between.val1.and.val2")
	private Long accountId;
}
