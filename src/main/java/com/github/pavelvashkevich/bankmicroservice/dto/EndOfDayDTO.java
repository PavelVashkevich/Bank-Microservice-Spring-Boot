package com.github.pavelvashkevich.bankmicroservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class EndOfDayDTO {
    private String symbol;
    private String exchange;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate datetime;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Instant timestamp;
    private BigDecimal close;
}