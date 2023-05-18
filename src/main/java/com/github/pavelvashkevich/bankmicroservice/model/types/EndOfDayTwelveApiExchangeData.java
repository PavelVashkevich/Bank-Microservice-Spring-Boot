package com.github.pavelvashkevich.bankmicroservice.model.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter
@Setter
public class EndOfDayTwelveApiExchangeData {
    private String symbol;
    private String exchange;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate datetime;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private ZonedDateTime timestamp;
    private BigDecimal close;
}