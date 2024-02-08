package ir.snapp.pay.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginDto {
	@NotNull(message = "email.must.not.be.null")
	@Email(message = "email.must.be.valid")
	@Size(min = 5, max = 254, message = "email.size.must.be.between.val1.and.val2")
	private String email;

	@NotNull(message = "password.must.not.be.null")
	@Size(min = 8, max = 100, message = "password.size.must.be.between.val1.and.val2")
	private String password;
}
