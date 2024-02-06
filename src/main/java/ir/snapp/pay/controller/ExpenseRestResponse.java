package ir.snapp.pay.controller;


import lombok.Data;

import java.util.List;

@Data
public class ExpenseRestResponse<T> {
	private Integer errorCode;
	private List<String> errorMessage;
	private T data;
}

