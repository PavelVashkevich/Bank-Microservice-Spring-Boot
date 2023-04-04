package com.github.pavelvashkevich.bankmicroservice.controller;

import com.github.pavelvashkevich.bankmicroservice.controller.dto.limit.AccountLimitRequestDto;
import com.github.pavelvashkevich.bankmicroservice.controller.dto.limit.AccountLimitResponseDto;
import com.github.pavelvashkevich.bankmicroservice.service.impl.AccountLimitServiceIml;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/limits")
@RequiredArgsConstructor
public class AccountLimitController {

    private final AccountLimitServiceIml accountLimitServiceIml;

    @PostMapping
    public ResponseEntity<AccountLimitResponseDto> addLimit(@Valid @RequestBody AccountLimitRequestDto accountLimitRequestDto) {
        System.out.println(accountLimitRequestDto);
        return ResponseEntity.ok(new AccountLimitResponseDto());
    }
}
