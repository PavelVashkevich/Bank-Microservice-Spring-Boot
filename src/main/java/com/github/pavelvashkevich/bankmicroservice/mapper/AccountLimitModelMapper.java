package com.github.pavelvashkevich.bankmicroservice.mapper;

import com.github.pavelvashkevich.bankmicroservice.dto.limit.AccountLimitAddResponseDto;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.AccountLimit;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class AccountLimitModelMapper {
    private final ModelMapper modelMapper;

    public AccountLimitAddResponseDto mapAccountLimitToAddResponseDto(AccountLimit accountLimit) {
        return this.modelMapper.map(accountLimit, AccountLimitAddResponseDto.class);
    }

    @PostConstruct
    private void initAccountLimitTypeMap() {
        TypeMap<AccountLimit, AccountLimitAddResponseDto> propertyMapAccountLimitToAccountLimitAddResponseDto =
                this.modelMapper.createTypeMap(AccountLimit.class, AccountLimitAddResponseDto.class);
        propertyMapAccountLimitToAccountLimitAddResponseDto
                .addMapping(AccountLimit::getSum, AccountLimitAddResponseDto::setLimitSum);
        propertyMapAccountLimitToAccountLimitAddResponseDto
                .addMapping(AccountLimit::getDatetime, AccountLimitAddResponseDto::setLimitDatetime);
        propertyMapAccountLimitToAccountLimitAddResponseDto
                .addMapping(AccountLimit::getCurrencyShortname, AccountLimitAddResponseDto::setLimitCurrencyShortname);
    }

}
