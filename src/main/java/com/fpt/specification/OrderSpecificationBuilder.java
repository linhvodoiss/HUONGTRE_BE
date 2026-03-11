package com.fpt.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.fpt.entity.Order;

public class OrderSpecificationBuilder {

	private final String search;

	public OrderSpecificationBuilder(String search) {
		this.search = search;

	}

	public Specification<Order> build() {
		Specification<Order> searchSpec = Specification.where(null); // bắt đầu từ null
		if (StringUtils.hasText(search)) {
			String[] fields = { "receiverName", "receiverPhone", "deliveryAddress" };
			for (String field : fields) {
				SearchCriteria criteria = new SearchCriteria(field, "Like", search);
				Specification<Order> spec = new OrderSpecification(criteria);
				searchSpec = searchSpec.or(spec);
			}
		}
		return searchSpec;
	}
}
