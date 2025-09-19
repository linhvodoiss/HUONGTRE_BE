package com.fpt.specification;

import com.fpt.entity.Category;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CategorySpecification implements Specification<Category> {

    private static final long serialVersionUID = 1L;
    private final SearchCriteria criteria;

    public CategorySpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        if (criteria.getOperator().equalsIgnoreCase("Like")) {
            // lowercase and uppercase
            return cb.like(cb.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
        }

        if (criteria.getOperator().equalsIgnoreCase("Equal")) {
            return cb.equal(root.get(criteria.getKey()), criteria.getValue());
        }

        return null;
    }
}
