package com.ky.orderservice.dto.orderAddress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderAddressDto {
    private String city;
    private String district;
    private String addressDetail;
}
