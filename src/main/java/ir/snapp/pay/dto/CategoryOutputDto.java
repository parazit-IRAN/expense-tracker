package ir.snapp.pay.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryOutputDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private UserOutputDto userOutputDto;
	private List<TransactionOutputDto> transactions;
}
