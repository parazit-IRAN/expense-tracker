package ir.snapp.pay.domain;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column(name = "date")
	private Instant date;

	@Column(name = "amount", scale = 4, precision = 10)
	private BigDecimal amount;

	@Size(min = 1, max = 255)
	@Column(name = "description")
	private String description;

	@ManyToOne
	@JoinColumn(name = "category_id", updatable = false, insertable = false)
	private Category category;

	@ManyToOne
	@JoinColumn(name = "user_id", updatable = false, insertable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "account_id", updatable = false, insertable = false)
	private Account account;

}
