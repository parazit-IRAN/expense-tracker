package ir.snapp.pay.domain;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "budget")
public class Budget implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Size(min = 1, max = 255)
	@Column(name = "name")
	private String name;

	@Column(name = "amount", scale = 4, precision = 10)
	private BigDecimal amount;

	@ManyToOne
	@JoinColumn(name = "user_id", updatable = false, insertable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "category_id", updatable = false, insertable = false)
	private Category category;


}
