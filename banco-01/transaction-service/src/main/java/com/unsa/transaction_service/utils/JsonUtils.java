package com.unsa.transaction_service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    public static <T> T fromString(String json, Class<T> clazz){
        try{
            return objectMapper.readValue(json,clazz);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }
}
