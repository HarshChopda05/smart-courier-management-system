package com.example.courier.management.Exceptions;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message){
        super(message);
    }
}
