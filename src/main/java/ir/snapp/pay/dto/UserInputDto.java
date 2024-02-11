package ir.snapp.pay.dto;

import ir.snapp.pay.annotation.NoEmptyString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInputDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "firstname.must.not.be.null")
	@Size(min = 3, max = 50, message = "firstname.size.must.be.between.val1.and.val2")
	private String firstName;
	@NotNull(message = "lastname.must.not.be.null")
	@Size(min = 3, max = 50, message = "lastname.size.must.be.between.val1.and.val2")
	private String lastName;
	@NotNull(message = "email.must.not.be.null")
	@Email(message = "email.must.be.valid")
	@Size(min = 5, max = 254, message = "email.size.must.be.between.val1.and.val2")
	private String email;
	@NotNull(message = "password.must.not.be.null")
	@Size(min = 8, max = 100, message = "password.size.must.be.between.val1.and.val2")
	private String password;
	@NotNull(message = "authorities.must.not.be.null")
	@Size(min = 1, message = "authorities.size.must.be.greater.than.val")
	@NoEmptyString(message = "authorities.must.not.be.empty")
	private List<String> authorities;
}
