package ir.snapp.pay.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserSettingDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String language;
	private String defaultCurrency;
	private String dateFormat;
	private String firstDayOfWeek;
	private String firstDayOfMonth;
}
