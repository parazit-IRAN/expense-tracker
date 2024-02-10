package ir.snapp.pay.domain;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "category")
public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Size(min = 1, max = 255)
	@Column(name = "name")
	private String name;

	@OneToMany(mappedBy = "category")
	private List<Budget> budgets;
}
