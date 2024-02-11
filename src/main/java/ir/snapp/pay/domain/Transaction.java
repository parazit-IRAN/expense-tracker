package ir.snapp.pay.domain;


import ir.snapp.pay.constant.TransactionType;
import lombok.Data;
import lombok.ToString;

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
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "date")
	private Instant date = Instant.now();

	@Column(name = "amount", scale = 10, precision = 4)
	private BigDecimal amount;

	@Size(max = 255)
	@Column(name = "description")
	private String description;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private TransactionType type = TransactionType.EXPENSE;

	@ManyToOne
	@JoinColumn(name = "category_id")
	@ToString.Exclude
	private Category category;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@ToString.Exclude
	private User user;

	@ManyToOne
	@JoinColumn(name = "account_id")
	@ToString.Exclude
	private Account account;

}
