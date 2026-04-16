package com.example.courier.management.Exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException{

//    String resourceName;
//    String fieldName;
//    long fieldValue;
//
//    public ResourceNotFoundException(String fieldName, long fieldValue, String resourceName) {
//        super(resourceName + "not found with " + fieldName + " : " + fieldValue);
//        this.fieldName = fieldName;
//        this.fieldValue = fieldValue;
//        this.resourceName = resourceName;
//    }

    public ResourceNotFoundException(String message){
        super(message);
    }
}
