package ir.snapp.pay.domain;

import ir.snapp.pay.constant.AccountType;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "account")
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Size(min = 1, max = 255)
	@Column(name = "name")
	private String name;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private AccountType type = AccountType.CARD;

	@Size(min = 2, max = 10)
	@Column(name = "currency", length = 10)
	private String currency;

	@Size(min = 1, max = 255)
	@Column(name = "description")
	private String description;

	@Column(name = "balance", scale = 4, precision = 10)
	private BigDecimal balance;

	@ManyToOne
	@JoinColumn(name = "user_id", updatable = false, insertable = false)
	private User user;

	@OneToMany(mappedBy = "account")
	private List<Transaction> transactions;
}
