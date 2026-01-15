package com.fpt.specification;

import com.fpt.entity.Category;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class CategorySpecificationBuilder {

	private final String search;
	private final Boolean isActive;

	public CategorySpecificationBuilder(String search, Boolean isActive) {
		this.search = search;
		this.isActive = isActive;

	}


	public Specification<Category> build() {
		Specification<Category> searchSpec = Specification.where(null); // bắt đầu từ null

		// Search theo tên
		if (StringUtils.hasText(search)) {
			String[] fields = {"name"};
			for (String field : fields) {
				SearchCriteria criteria = new SearchCriteria(field, "Like", search);
				Specification<Category> spec = new CategorySpecification(criteria);
				searchSpec = searchSpec.or(spec);
			}
		}


		if (isActive != null) {
			searchSpec = searchSpec.and((root, query, cb) -> cb.equal(root.get("isActive"), isActive));
		}


		return searchSpec;
	}
}
