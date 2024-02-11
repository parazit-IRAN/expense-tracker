package ir.snapp.pay.dto;

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
public class BudgetOutputDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private BigDecimal amount;
	private UserOutputDto user;
	private CategoryOutputDto category;
}
