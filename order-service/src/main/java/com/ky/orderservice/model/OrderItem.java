package com.ky.orderservice.model;

import com.ky.common.model.BaseModel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "orderItems")
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OrderItem extends BaseModel  {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    private UUID productId;
    private Integer quantity;

}