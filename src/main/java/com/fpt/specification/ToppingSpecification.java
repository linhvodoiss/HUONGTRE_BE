package com.fpt.specification;

import com.fpt.entity.Product;
import com.fpt.entity.Topping;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ToppingSpecification implements Specification<Topping> {

	private static final long serialVersionUID = 1L;
	private final SearchCriteria criteria;

	public ToppingSpecification(SearchCriteria criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Topping> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		if (criteria.getOperator().equalsIgnoreCase("Like")) {
			return cb.like(cb.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
		}

		if (criteria.getOperator().equalsIgnoreCase(">=")) {
			return cb.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
		}

		if (criteria.getOperator().equalsIgnoreCase("<=")) {
			return cb.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
		}

		if (criteria.getOperator().equalsIgnoreCase("Equal")) {
			return cb.equal(root.get(criteria.getKey()), criteria.getValue());
		}

		return null;
	}
}
