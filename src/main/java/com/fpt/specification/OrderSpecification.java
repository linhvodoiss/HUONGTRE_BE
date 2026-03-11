package com.fpt.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.fpt.entity.Order;

public class OrderSpecification implements Specification<Order> {

	private static final long serialVersionUID = 1L;
	private final SearchCriteria criteria;

	public OrderSpecification(SearchCriteria criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		if (criteria.getOperator().equalsIgnoreCase("Like")) {
			// lowercase and uppercase
			return cb.like(cb.lower(root.get(criteria.getKey())),
					"%" + criteria.getValue().toString().toLowerCase() + "%");
		}

		if (criteria.getOperator().equalsIgnoreCase("Equal")) {
			return cb.equal(root.get(criteria.getKey()), criteria.getValue());
		}

		return null;
	}
}
