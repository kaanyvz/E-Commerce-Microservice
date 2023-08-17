package com.ky.cartservice.controller;

import com.ky.cartservice.dto.cartItem.CreateCartItemRequest;
import com.ky.cartservice.dto.cartItem.DeleteCartItemRequest;
import com.ky.cartservice.dto.cartItem.UpdateCartItemRequest;
import com.ky.cartservice.model.Cart;
import com.ky.cartservice.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<String> addProductToCart(@RequestParam UUID customerId,
                                                   @RequestBody CreateCartItemRequest createCartItemRequest){

        cartService.save(customerId, createCartItemRequest);
        return new ResponseEntity<>("Product is added with id: " + createCartItemRequest.getProductId(), HttpStatus.CREATED);
    }
    @PutMapping
    public ResponseEntity<String> updateQuantity(@RequestParam UUID customerId,
                                                 @RequestBody UpdateCartItemRequest updateCartItemRequest){

        cartService.updateQuantity(customerId,updateCartItemRequest);
        return new ResponseEntity<>("Product is updated with id" + updateCartItemRequest.getProductId(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Cart>> getCarts() {
        return ResponseEntity.ok(cartService.getCarts());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCartItemFromCartByProductId(@RequestParam UUID customerId,
                                                                    @RequestBody DeleteCartItemRequest request){
        cartService.deleteCartItemFromCartByProductId(customerId, request);
        return new ResponseEntity<>("Cart item has been deleted", HttpStatus.OK);
    }
}
