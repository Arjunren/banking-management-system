package com.arjunren.banking.entity;

import com.arjunren.banking.domain.AccountStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name="bank_accounts", indexes=@Index(name="bank_accounts_owner_idx", columnList="owner_id"))
public class BankAccount {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="account_number", nullable=false, unique=true, length=20) private String accountNumber;
    @ManyToOne(optional=false, fetch=FetchType.LAZY) @JoinColumn(name="owner_id") private AppUser owner;
    @Column(nullable=false, precision=19, scale=2) private BigDecimal balance=BigDecimal.ZERO.setScale(2);
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private AccountStatus status=AccountStatus.ACTIVE;
    @Version private long version;
    @Column(name="created_at", nullable=false, updatable=false) private Instant createdAt=Instant.now();
    @Column(name="updated_at", nullable=false) private Instant updatedAt=Instant.now();
    protected BankAccount() {}
    public BankAccount(String number, AppUser owner){this.accountNumber=number;this.owner=owner;}
    @PreUpdate void updateTimestamp(){updatedAt=Instant.now();}
    public void credit(BigDecimal amount){balance=balance.add(amount);} public void debit(BigDecimal amount){balance=balance.subtract(amount);}
    public Long getId(){return id;} public String getAccountNumber(){return accountNumber;} public AppUser getOwner(){return owner;} public BigDecimal getBalance(){return balance;} public AccountStatus getStatus(){return status;} public Instant getCreatedAt(){return createdAt;}
}

