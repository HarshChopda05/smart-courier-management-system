package com.example.courier.management.Services.Implementations;

import com.example.courier.management.Models.Location;
import com.example.courier.management.Models.Order;
import com.example.courier.management.Models.Package;
import com.example.courier.management.Models.Type.PackageStatus;
import com.example.courier.management.Models.User;
import com.example.courier.management.PayLoads.OrderPlaceRequestDTO;
import com.example.courier.management.PayLoads.OrderPlaceResponseDTO;
import com.example.courier.management.PayLoads.PackageRequestDTO;
import com.example.courier.management.PayLoads.PackageResponseDTO;
import com.example.courier.management.Repositories.LocationRepository;
import com.example.courier.management.Repositories.OrderRepository;
import com.example.courier.management.Repositories.UserRepository;
import com.example.courier.management.Services.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderPlaceResponseDTO placeOrder(OrderPlaceRequestDTO request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found!"));


        //Create location
        Location location = Location.builder()
                .address(request.getAddress())
                .city(request.getCity())
                .pincode(request.getPincode())
                .build();

        location = locationRepository.save(location);

        //Create an Order
        Order order = new Order();
        order.setUser(user);
        order.setLocation(location);

        //create a packages
        List<Package> packageList = new ArrayList<>();

        for (PackageRequestDTO dto : request.getPackages()){
            Package pkg = new Package();
            pkg.setPackageName(dto.getPackageName());
            pkg.setWeight(dto.getWeight());
            pkg.setOrder(order);
            pkg.setPackageStatus(PackageStatus.CREATED);

            packageList.add(pkg);
        }
        order.setPackages(packageList);

        //Calculate Price
        Double totalAmount = calculatePrice(request.getPackages());
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    //Price logic
    private Double calculatePrice(List<PackageRequestDTO> packageDTO){

        Double total = 0.0;

        for (PackageRequestDTO dto : packageDTO){
            Double basePrice = 50.0;
            Double perKGPrice = 20.0;

            Double weightCharge = (perKGPrice * dto.getWeight());

            total += basePrice + weightCharge;
        }
        return total;
    }

    private OrderPlaceResponseDTO mapToResponse(Order order){
        List<PackageResponseDTO> packageResponses = order.getPackages()
                .stream()
                .map(pkg -> PackageResponseDTO.builder()
                            .packageId(pkg.getPackageId())
                            .packageName(pkg.getPackageName())
                            .weight(pkg.getWeight())
                            .packageStatus(pkg.getPackageStatus().name())
                            .build()
                )
                .toList();

        return OrderPlaceResponseDTO.builder()
                .orderId(order.getOrderId())
                .message("Order placed successfully")
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus().name())
                .createdAt(order.getCreatedAt())
                .address(order.getLocation().getAddress())
                .city(order.getLocation().getCity())
                .pincode(order.getLocation().getPincode())
                .packages(packageResponses)
                .build();

    }
}
