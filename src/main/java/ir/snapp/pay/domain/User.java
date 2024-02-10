package ir.snapp.pay.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Data
@Entity
@Table(name = "my_user")
public class User extends AbstractAuditingEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
	@SequenceGenerator(name = "sequence_generator", sequenceName = "sequence_generator", allocationSize = 1)
	private Long id;

	@JsonIgnore
	@Size(min = 60, max = 60)
	@Column(name = "password_hash", length = 60, nullable = false)
	private String password;

	@Size(max = 50)
	@Column(name = "first_name", length = 50)
	private String firstName;

	@Size(max = 50)
	@Column(name = "last_name", length = 50)
	private String lastName;

	@Email
	@Size(min = 5, max = 254)
	@Column(length = 254, unique = true, nullable = false)
	private String email;

	@NotNull
	@Column(nullable = false)
	private boolean activated = true;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "user_authority",
			joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")}
	)
	private List<Authority> authorities = new ArrayList<>();

	@Size(min = 2, max = 10)
	@Column(length = 10)
	private String language = "en";
	@Size(min = 2, max = 10)
	@Column(length = 10)
	private String defaultCurrency = "USD";
	@Size(min = 8, max = 50)
	@Column(length = 50)
	private String dateFormat = "dd/MM/yyyy";
	@Size(min = 5, max = 10)
	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private DayOfWeek firstDayOfWeek = DayOfWeek.SATURDAY;
	@Size(min = 1, max = 3)
	@Column(length = 3)
	private Integer firstDayOfMonth = 1 ;
}
