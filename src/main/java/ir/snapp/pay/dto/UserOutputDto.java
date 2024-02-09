package ir.snapp.pay.dto;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class UserOutputDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private Boolean isActivated;
	private List<String> authorities;
}

