package com.ky.cartservice.service;

import com.ky.cartservice.client.ProductServiceClient;
import com.ky.cartservice.dto.cartItem.CartItemMapper;
import com.ky.cartservice.dto.cartItem.CreateCartItemRequest;
import com.ky.cartservice.dto.cartItem.DeleteCartItemRequest;
import com.ky.cartservice.dto.cartItem.UpdateCartItemRequest;
import com.ky.cartservice.model.Cart;
import com.ky.cartservice.model.CartItem;
import com.ky.cartservice.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemMapper cartItemMapper;

    public CartService(CartRepository cartRepository, CartItemMapper cartItemMapper, ProductServiceClient productServiceClient) {
        this.cartRepository = cartRepository;
        this.cartItemMapper = cartItemMapper;
    }

    public void save(UUID customerId, CreateCartItemRequest createCartItemRequest){
        CartItem cartItem = cartItemMapper.createCartItemRequestToCartItem(createCartItemRequest);
        Optional<Cart> cart = cartRepository.findCartByCustomerId(customerId);
        System.out.println("CUSTOMER ID: " + customerId);


        if(cart.isPresent()){
            Cart presentCart = cart.get();
            presentCart.getCartItems().add(cartItem);
            presentCart.setTotalPrice(getTotalPrice(presentCart));
            cartRepository.save(presentCart);
        }else{
            Cart newCart = Cart.builder()
                    .customerId(customerId)
                    .cartItems(List.of(cartItem))
                    .build();
            newCart.setTotalPrice(getTotalPrice(newCart));
            cartRepository.save(newCart);
            System.out.println("CUSTOMER ID: " + customerId);

        }
    }

    public void updateQuantity(UUID customerId, UpdateCartItemRequest updateCartItemRequest) {
        Cart cart = cartRepository.findCartByCustomerId(customerId)
                .orElseThrow(()-> {
                    log.error("Cart with id: {} could not be found!", customerId);
                    return new RuntimeException("Cart is not found with id :" + customerId);
                });

        CartItem cartItem = cart
                .getCartItems()
                .stream()
                .filter((eachCart)-> eachCart.getProductId().equals(updateCartItemRequest.getProductId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product could not updated with id: " + updateCartItemRequest.getProductId()));

        cartItem.setQuantity(updateCartItemRequest.getQuantity());
        cart.setTotalPrice(getTotalPrice(cart));
        cartRepository.save(cart);
    }

    public List<Cart> getCarts(){
        return cartRepository.findAll();
    }

    public void deleteCartItemFromCartByProductId(UUID customerId, DeleteCartItemRequest request){
        Cart cart = cartRepository.findCartByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Cart could not found by customer id: " + customerId));

        CartItem cartItemToRemove = cart.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product could not found by customer id: " + request.getProductId()));

        cart.getCartItems().remove(cartItemToRemove);

        if(cart.getCartItems().isEmpty()){
            cartRepository.delete(cart);
        }else{
            cart.setTotalPrice(getTotalPrice(cart));
            cartRepository.save(cart);
        }
    }


    private BigDecimal getTotalPrice(Cart cart){
        BigDecimal totalPrice = BigDecimal.ZERO;

        for(CartItem cartItem : cart.getCartItems()){
            BigDecimal subTotal = cartItem.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalPrice = totalPrice.add(subTotal);
        }
        return totalPrice;
    }
}
