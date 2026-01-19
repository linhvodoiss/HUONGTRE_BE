package com.fpt.specification;

import com.fpt.entity.Customer;
import com.fpt.entity.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class CustomerSpecificationBuilder {

	private final String search;


	public CustomerSpecificationBuilder(String search) {
		this.search = search;


	}

	public Specification<Customer> build() {
		Specification<Customer> searchSpec = Specification.where(null);
		if (StringUtils.hasText(search)) {
			String[] fields = {"phone"};
			for (String field : fields) {
				SearchCriteria criteria = new SearchCriteria(field, "Like", search);
				Specification<Customer> spec = new CustomerSpecification(criteria);
				searchSpec = searchSpec.or(spec);
			}
		}
		return searchSpec;
	}
}
