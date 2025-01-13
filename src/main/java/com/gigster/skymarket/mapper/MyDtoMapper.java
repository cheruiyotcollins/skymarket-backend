package com.gigster.skymarket.mapper;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MyDtoMapper {
    public static <T> T mapDtoToClass(Object dto, Class<T> classValue){
        T t = null;
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Gson gson = new Gson();

        try{
            String data = gson.toJson(dto);
            t = jsonMapper.readValue(data, classValue);
        }catch(Exception e){
            log.warn(e.getLocalizedMessage());;
        }
        return t;
    }


    public static String convertToString(Object object) {
        Gson gson = new Gson();
        try {
            return gson.toJson(object);
        } catch (Exception e) {
            log.warn(e.getLocalizedMessage());
            ;
            return null;
        }
    }
}
