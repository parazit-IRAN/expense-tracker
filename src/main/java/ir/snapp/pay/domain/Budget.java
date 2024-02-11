package ir.snapp.pay.domain;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "budget")
public class Budget implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "amount", scale = 10, precision = 4)
	private BigDecimal amount;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@ToString.Exclude
	private User user;

	@ManyToOne
	@JoinColumn(name = "category_id")
	@ToString.Exclude
	private Category category;

}
