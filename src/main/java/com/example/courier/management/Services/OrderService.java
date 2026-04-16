package com.example.courier.management.Services;

import com.example.courier.management.PayLoads.OrderPlaceRequestDTO;
import com.example.courier.management.PayLoads.OrderPlaceResponseDTO;
import jakarta.validation.Valid;

public interface OrderService {

    OrderPlaceResponseDTO placeOrder(@Valid OrderPlaceRequestDTO orderPlaceRequestDTO);
}
