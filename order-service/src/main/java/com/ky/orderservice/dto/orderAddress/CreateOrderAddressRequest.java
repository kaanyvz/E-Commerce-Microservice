package com.ky.orderservice.dto.orderAddress;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateOrderAddressRequest {
    @NotNull
    private String city;

    @NotNull
    private String district;

    @NotNull
    private String addressDetail;
}
