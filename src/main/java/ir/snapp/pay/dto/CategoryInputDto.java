package ir.snapp.pay.dto;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CategoryInputDto {

	@NotNull(message = "name.must.not.be.null")
	@NotEmpty(message = "name.must.not.be.empty")
	private String name;
}

