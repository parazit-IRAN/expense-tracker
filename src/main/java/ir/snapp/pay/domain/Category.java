package ir.snapp.pay.domain;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Size(min = 1, max = 255)
	@Column(name = "name")
	private String name;

	@JsonManagedReference
	@OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
	private List<Transaction> transactions = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "user_id")
	@ToString.Exclude
	private User user;

}

