package com.ky.productservice.model;

import com.ky.common.model.AdvanceBaseModal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "products")
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class Product extends AdvanceBaseModal{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String name;
    private BigDecimal unitPrice;

    @Column(columnDefinition="TEXT")
    private String description;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable=false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deleted;

    private String imageUrl;

    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();


}