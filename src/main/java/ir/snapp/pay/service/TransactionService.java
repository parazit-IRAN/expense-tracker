package ir.snapp.pay.service;


import ir.snapp.pay.domain.Transaction;
import ir.snapp.pay.dto.TransactionInputDto;
import ir.snapp.pay.dto.TransactionOutputDto;
import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.exception.ExpenseExceptionType;
import ir.snapp.pay.repository.TransactionRepository;
import ir.snapp.pay.service.mapper.TransactionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final TransactionMapper transactionMapper;

	@Transactional
	public TransactionOutputDto createTransaction(TransactionInputDto transactionInputDto) {
		Transaction transaction = transactionMapper.transactionInputDtoToTransaction(transactionInputDto);
		return transactionMapper.transactionToTransactionOutputDto(transactionRepository.save(transaction));
	}

	@Transactional
	public void deleteTransaction(Long transactionId) {
		Transaction transaction = transactionRepository.findById(transactionId)
				.orElseThrow(new ExpenseException(ExpenseExceptionType.TRANSACTION_NOT_FOUND_EXCEPTION));
		transactionRepository.delete(transaction);
		log.debug("Deleted Transaction: {}", transaction);
	}
}
