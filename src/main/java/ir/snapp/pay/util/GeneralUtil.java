package ir.snapp.pay.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class GeneralUtil {

	public static List<String> buildErrorReasonInCaseOfBindingFailure(BindingResult bindingResult) {
		if (log.isDebugEnabled())
			log.debug("binding failure with reason [{}]", bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.joining(", ")));
		return bindingResult.getAllErrors().stream()
				.map(br -> {
					List<Object> args = Arrays.stream(Objects.requireNonNull(br.getArguments())).collect(Collectors.toList());
					args.remove(0);
					return MessageTranslatorUtil.getText(br.getDefaultMessage(), args.toArray(new Object[0]));
				})
				.collect(Collectors.toList());
	}

	public static Instant convert(LocalDate localDate) {
		return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
	}
}
