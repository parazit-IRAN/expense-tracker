package ir.snapp.pay.dto;


import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class TransactionInputDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "amount.must.not.be.null")
	@Digits(integer = 10, fraction = 4, message = "amount.size.must.be.between.val1.and.val2")
	private BigDecimal amount;

	private String description;

	@NotNull(message = "type.must.not.be.null")
	@NotEmpty(message = "type.must.not.be.empty")
	private String type;

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
