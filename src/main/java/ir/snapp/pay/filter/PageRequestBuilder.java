package ir.snapp.pay.filter;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class PageRequestBuilder {
	private PageRequestBuilder() {
	}

	public static PageRequest getPageRequest(Integer pageSize, Integer pageNumber, String sortingCriteria) {
		List<String> list = Arrays.asList(StringUtils.split(StringUtils.defaultIfEmpty(sortingCriteria, ""), ","));
		LinkedHashSet<String> sortingFields = new LinkedHashSet<>(list);

		List<Sort.Order> sortingOrders = sortingFields.stream().map(PageRequestBuilder::getOrder).collect(Collectors.toList());

		Sort sort = sortingOrders.isEmpty() ? null : Sort.by(sortingOrders);

		if (sort != null) {
			return PageRequest.of(ObjectUtils.defaultIfNull(pageNumber, 1), ObjectUtils.defaultIfNull(pageSize, 20), sort);
		} else {
			return PageRequest.of(ObjectUtils.defaultIfNull(pageNumber, 1), ObjectUtils.defaultIfNull(pageSize, 20));
		}
	}

	private static Sort.Order getOrder(String value) {
		if (StringUtils.startsWith(value, "-")) {
			return new Sort.Order(Sort.Direction.DESC, StringUtils.substringAfter(value, "-"));
		} else if (StringUtils.startsWith(value, "+")) {
			return new Sort.Order(Sort.Direction.ASC, StringUtils.substringAfter(value, "+"));
		} else {
			return new Sort.Order(Sort.Direction.ASC, StringUtils.trim(value));
		}
	}

}
