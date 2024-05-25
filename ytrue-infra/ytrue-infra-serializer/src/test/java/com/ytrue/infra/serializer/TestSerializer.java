package com.ytrue.infra.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ytrue.infra.serializer.config.GsonConfig;
import com.ytrue.infra.serializer.config.JacksonConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = JacksonConfig.class)
public class TestSerializer {


    @Test
    public void test01() throws JsonProcessingException {
        ObjectMapper objectMapper = new JacksonConfig().objectMapper();
        Gson gson = new GsonConfig().gson();

        Cat cat = new Cat();
        cat.setName("tomcat");
        cat.setAge(Long.MAX_VALUE);
        cat.setAge1(Long.MAX_VALUE);
        cat.setAgeIds(Integer.MAX_VALUE);
        cat.setId(1L);
        //cat.setId(null);
        cat.setDate1(LocalDateTime.now());
        cat.setDate2(LocalDate.now());
        cat.setDate3(LocalTime.now());
        cat.setDate4(new Date());
       // cat.setData1(new BigDecimal("1111111122323232444"));
        cat.setData1(11223.44334D);
        cat.setData2(1111111122323232444L);
        cat.setData3(new BigDecimal("1111111122323232444"));

        List<Long> maxValue1 = List.of(1L, Long.MAX_VALUE - 1, Long.MAX_VALUE);
        List<Long> maxValue2 = List.of(2L, Long.MAX_VALUE - 2, Long.MAX_VALUE);
        List<Integer> maxValue3 = List.of(2, Integer.MAX_VALUE - 2, Integer.MAX_VALUE);
        // List<Long> maxValue = List.of(1L);

        cat.setIds(maxValue1);
        cat.setXxx(maxValue3);


        cat.setZzzIds(maxValue3.toArray(new Integer[0]));


        String catStr = objectMapper.writeValueAsString(cat);
        Cat cat1 = objectMapper.readValue(catStr, Cat.class);

       // cat.setCat(cat1);


       // System.out.println(objectMapper.writeValueAsString(cat));

         System.out.println(gson.toJson(cat));
        //System.out.println(gson.fromJson(gson.toJson(cat), Cat.class));
    }


    @Test
    public void test02() throws JsonProcessingException {
        ObjectMapper objectMapper = new JacksonConfig().objectMapper();


        String jsonString = """
                {
                  "id" : 12,
                  "name" : "John Doe",
                  "age" : 9007199254740996,
                  "address" : {
                    "street" : "123 Main St",
                    "city" : "Springfield",
                    "zipCode" : 9007199254740996
                  },
                  "scores" : [ 9007199254740996, 9007199254740996, 88, 92 ],
                  "ids" : [ 9007199254740996, 9007199254740996, 88, 92 ],
                  "grades" : {
                    "math" : 9007199254740996,
                    "science" : 9007199254740996,
                    "history" : 9007199254740996
                  }
                }
                """;
        Map<String, Object> map = objectMapper.readValue(jsonString, new TypeReference<>() {
        });
        System.out.println(objectMapper.writeValueAsString(map));
    }
}
