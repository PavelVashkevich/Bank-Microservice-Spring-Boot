package com.github.pavelvashkevich.bankmicroservice.mapper;

import com.github.pavelvashkevich.bankmicroservice.dto.limit.AccountLimitAddResponseDto;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.AccountLimit;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountLimitModelMapper {
    private final ModelMapper modelMapper;

    public AccountLimitAddResponseDto mapAccountLimitToAddResponseDto(AccountLimit accountLimit) {
        TypeMap<AccountLimit, AccountLimitAddResponseDto> propertyMapper =
                this.modelMapper.createTypeMap(AccountLimit.class, AccountLimitAddResponseDto.class);
        propertyMapper.addMapping(AccountLimit::getSum, AccountLimitAddResponseDto::setLimitSum);
        propertyMapper.addMapping(AccountLimit::getDatetime, AccountLimitAddResponseDto::setLimitDatetime);
        propertyMapper.addMapping(AccountLimit::getCurrencyShortname, AccountLimitAddResponseDto::setLimitCurrencyShortname);

        return this.modelMapper.map(accountLimit, AccountLimitAddResponseDto.class);

    }

}
