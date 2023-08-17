package com.ky.orderservice.dto.orderAddress;

import com.ky.orderservice.model.OrderAddress;
import org.springframework.stereotype.Component;

@Component
public class OrderAddressMapper {

    public OrderAddressDto orderAddressToOrderAddressDto(OrderAddress orderAddress){
        return OrderAddressDto.builder()
                .city(orderAddress.getCity())
                .district(orderAddress.getDistrict())
                .addressDetail(orderAddress.getAddressDetail())
                .build();
    }

    public OrderAddress orderAddressRequestToOrderAddress(CreateOrderAddressRequest createOrderAddressRequest){
        return OrderAddress.builder()
                .city(createOrderAddressRequest.getCity())
                .addressDetail(createOrderAddressRequest.getAddressDetail())
                .district(createOrderAddressRequest.getDistrict())
                .build();
    }

}
