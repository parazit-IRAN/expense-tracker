package ir.snapp.pay.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class UserInputDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min = 3, max = 50)
	private String firstName;
	@NotNull
	@Size(min = 3, max = 50)
	private String lastName;
	@NotNull
	@Email
	@Size(min = 5, max = 254)
	private String email;
	@NotNull
	@Size(min = 8, max = 100)
	private String password;
	@NotNull
	@Size(min = 1)
	private List<String> authorities;
}
