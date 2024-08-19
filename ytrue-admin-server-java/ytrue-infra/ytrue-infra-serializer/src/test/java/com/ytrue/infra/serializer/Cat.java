package com.ytrue.infra.serializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ytrue.infra.serializer.ser.NumberCollToStrSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Cat {

    private String name;

    private Long age;

    private long age1;
    private int ageIds;

    private Long id;

    private LocalDateTime date1;

    private LocalDate date2;

    private LocalTime date3;

    private Date date4;

    private List<Long> ids;

    @JsonSerialize(using = NumberCollToStrSerializer.class)
    private List<Integer> xxx;

    private Integer[] zzzIds;


    private Double data1;
    private Long data2;
    private BigDecimal data3;


    private Cat cat;
}
