package com.ky.orderservice.model;

import com.ky.common.model.AdvanceBaseModal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "orders")
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Order extends AdvanceBaseModal {

    private UUID customerId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderAddress address;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<OrderItem> items;
}