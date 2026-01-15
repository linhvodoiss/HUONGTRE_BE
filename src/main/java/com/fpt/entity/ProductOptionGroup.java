package com.fpt.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(
        name = "product_option_group",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"product_id", "option_group_id"}
        )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_id", nullable = false)
    private OptionGroup optionGroup;
}
