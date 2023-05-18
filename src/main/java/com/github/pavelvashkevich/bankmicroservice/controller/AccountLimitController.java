package com.github.pavelvashkevich.bankmicroservice.controller;

import com.github.pavelvashkevich.bankmicroservice.dto.limit.AccountLimitAddRequestDto;
import com.github.pavelvashkevich.bankmicroservice.dto.limit.AccountLimitAddResponseDto;
import com.github.pavelvashkevich.bankmicroservice.exception.NotValidRequestException;
import com.github.pavelvashkevich.bankmicroservice.service.AccountLimitService;
import com.github.pavelvashkevich.bankmicroservice.util.ValidationErrorResponseCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/account_limit")
@RequiredArgsConstructor
public class AccountLimitController {
    private final AccountLimitService accountLimitService;
    private final ValidationErrorResponseCreator validationErrorResponseCreator;


    @PostMapping
    public ResponseEntity<AccountLimitAddResponseDto> addNewAccountLimit(@Valid @RequestBody AccountLimitAddRequestDto addRequestDto,
                                                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String response = validationErrorResponseCreator.createResponse(bindingResult);
            throw new NotValidRequestException(response);
        }
        AccountLimitAddResponseDto addResponseDto = accountLimitService.save(addRequestDto);
        return ResponseEntity.created(URI.create("/api/v1/account_limit/" + addResponseDto.getId())).build();
    }


    // TODO get /api/v1/account_limit?account_number=<number>&expense_category=<produce/service/all>&current=true/false
    // TODO by default current = true
    // TODO by default expense_category=all
}
