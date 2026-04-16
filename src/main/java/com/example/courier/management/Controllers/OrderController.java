package com.example.courier.management.Controllers;

import com.example.courier.management.PayLoads.OrderPlaceRequestDTO;
import com.example.courier.management.PayLoads.OrderPlaceResponseDTO;
import com.example.courier.management.Services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("courier/customer/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @PostMapping("/")
    public ResponseEntity<OrderPlaceResponseDTO> placeOrder(@Valid @RequestBody OrderPlaceRequestDTO orderPlaceRequestDTO){

        return new ResponseEntity<>(orderService.placeOrder(orderPlaceRequestDTO),HttpStatus.CREATED);
    }
}
