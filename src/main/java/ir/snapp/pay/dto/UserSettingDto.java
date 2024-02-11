package ir.snapp.pay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingDto implements Serializable {
	private static final long serialVersionUID = 1L;
	@NotNull(message = "language.must.not.be.null")
	@NotEmpty(message = "language.must.not.be.empty")
	private String language;
}
