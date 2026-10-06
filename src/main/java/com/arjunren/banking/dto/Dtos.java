package com.arjunren.banking.dto;

import com.arjunren.banking.domain.*;
import com.arjunren.banking.entity.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public final class Dtos {
    private Dtos(){}
    public record RegisterRequest(@NotBlank @Size(min=2,max=120) String name,@Email @NotBlank String email,@NotBlank @Size(min=12,max=72) String password){}
    public record LoginRequest(@Email @NotBlank String email,@NotBlank @Size(max=72) String password){}
    public record MoneyRequest(@DecimalMin("0.01") @Digits(integer=17,fraction=2) BigDecimal amount,@Size(max=250) String description){}
    public record TransferRequest(@NotNull Long fromAccountId,@NotNull Long toAccountId,@DecimalMin("0.01") @Digits(integer=17,fraction=2) BigDecimal amount,@Size(max=250) String description){}
    public record UserResponse(Long id,String email,String name,Role role){public static UserResponse of(AppUser u){return new UserResponse(u.getId(),u.getEmail(),u.getName(),u.getRole());}}
    public record TokenResponse(String accessToken,String tokenType,Instant expiresAt,UserResponse user){}
    public record AccountResponse(Long id,String accountNumber,Long ownerId,String ownerName,BigDecimal balance,AccountStatus status,Instant createdAt){public static AccountResponse of(BankAccount a){return new AccountResponse(a.getId(),a.getAccountNumber(),a.getOwner().getId(),a.getOwner().getName(),a.getBalance(),a.getStatus(),a.getCreatedAt());}}
    public record TransactionResponse(Long id,Long accountId,TransactionType type,BigDecimal amount,BigDecimal balanceAfter,String reference,String description,Instant createdAt){public static TransactionResponse of(AccountTransaction t){return new TransactionResponse(t.getId(),t.getAccountId(),t.getType(),t.getAmount(),t.getBalanceAfter(),t.getReference(),t.getDescription(),t.getCreatedAt());}}
    public record PageResponse<T>(List<T> items,int page,int size,long totalElements,int totalPages){}
}

