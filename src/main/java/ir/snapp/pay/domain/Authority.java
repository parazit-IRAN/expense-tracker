package ir.snapp.pay.domain;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Entity
@Table(name = "authority")
public class Authority implements Serializable {
	private static final long serialVersionUID = 1L;

	@Size(max = 50)
	@Id
	@Column(name = "name", length = 50, nullable = false)
	private String name;
}
