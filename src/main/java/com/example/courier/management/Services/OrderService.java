package com.example.courier.management.Services;

import com.example.courier.management.PayLoads.RequestDTO.OrderPlaceRequestDTO;
import com.example.courier.management.PayLoads.ResponseDTO.OrderPlaceResponseDTO;
import jakarta.validation.Valid;

public interface OrderService {

    OrderPlaceResponseDTO placeOrder(@Valid OrderPlaceRequestDTO orderPlaceRequestDTO);
}
