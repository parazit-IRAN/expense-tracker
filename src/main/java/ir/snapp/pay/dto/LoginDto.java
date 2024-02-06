package ir.snapp.pay.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginDto {
	@NotNull
	@Email
	@Size(min = 5, max = 254)
	private String email;

	@NotNull
	@Size(min = 4, max = 100)
	private String password;
}
