package ir.snapp.pay.domain;


import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Entity
@Table(name = "authority")
public class Authority implements GrantedAuthority, Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(max = 50)
	@Id
	@Column(name = "name", length = 50)
	private String name;

	@Override
	public String getAuthority() {
		return this.name;
	}
}
