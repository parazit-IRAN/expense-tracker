package ir.snapp.pay.dto;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
public class CategoryInputDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "name.must.not.be.null")
	@NotEmpty(message = "name.must.not.be.empty")
	private String name;
}

