package ir.snapp.pay.service.mapper;


import ir.snapp.pay.domain.Category;
import ir.snapp.pay.domain.Transaction;
import ir.snapp.pay.domain.User;
import ir.snapp.pay.dto.CategoryInputDto;
import ir.snapp.pay.dto.CategoryOutputDto;
import ir.snapp.pay.dto.TransactionOutputDto;
import ir.snapp.pay.dto.UserOutputDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryMapper {
	private final TransactionMapper transactionMapper;

	public CategoryOutputDto categoryToCategoryOutputDto(Category category) {
		return CategoryOutputDto.builder()
				.id(category.getId())
				.name(category.getName())
				.userOutputDto(createUserOutputDto(category.getUser()))
				.transactions(createTransactionOutputDtos(category.getTransactions()))
				.build();
	}

	private UserOutputDto createUserOutputDto(User user) {
		return UserOutputDto.builder()
				.id(user.getId())
				.email(user.getEmail())
				.build();
	}

	private List<TransactionOutputDto> createTransactionOutputDtos(List<Transaction> transactions) {
		return transactionMapper.transactionToTransactionOutputDto(transactions);
	}

	public Category categoryInputDtoToCategory(CategoryInputDto categoryInputDto) {
		Category category = new Category();
		category.setName(categoryInputDto.getName());
		return category;
	}
}
