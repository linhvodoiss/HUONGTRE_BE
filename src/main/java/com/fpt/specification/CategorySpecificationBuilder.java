package com.fpt.specification;

import com.fpt.entity.Category;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class CategorySpecificationBuilder {

    private final String search;
    private final Boolean isActive;
    private final Long versionId;

    public CategorySpecificationBuilder(String search,  Boolean isActive,Long versionId) {
        this.search = search;
        this.isActive=isActive;
        this.versionId=versionId;
    }

    public Specification<Category> build() {
        Specification<Category> spec = Specification.where(null);

        // search field free text
        if (StringUtils.hasText(search)) {
            String[] fields = {"name"};
            for (String field : fields) {
                SearchCriteria criteria = new SearchCriteria(field, "Like", search);
                spec = spec.or(new CategorySpecification(criteria));
            }
        }

        // filter by active
        if (isActive != null) {
            SearchCriteria statusCriteria = new SearchCriteria("isActive", "Equal", isActive);
            spec = spec.and(new CategorySpecification(statusCriteria));
        }


        if (versionId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("version").get("id"), versionId));
        }

        return spec;
    }
}
