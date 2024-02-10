package ir.snapp.pay.domain;

import ir.snapp.pay.constant.AccountType;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
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

	@ManyToOne
	@JoinColumn(name = "user_id", updatable = false, insertable = false)
	private User user;

	@OneToMany(mappedBy = "account")
	private List<Transaction> transactions;
}
