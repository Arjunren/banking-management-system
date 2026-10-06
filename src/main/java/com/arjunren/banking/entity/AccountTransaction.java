package com.arjunren.banking.entity;

import com.arjunren.banking.domain.TransactionType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name="account_transactions", indexes={@Index(name="transactions_account_created_idx", columnList="account_id,created_at"),@Index(name="transactions_reference_idx", columnList="reference")})
public class AccountTransaction {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false, fetch=FetchType.LAZY) @JoinColumn(name="account_id") private BankAccount account;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=30) private TransactionType type;
    @Column(nullable=false, precision=19, scale=2) private BigDecimal amount;
    @Column(name="balance_after", nullable=false, precision=19, scale=2) private BigDecimal balanceAfter;
    @Column(nullable=false, length=50) private String reference;
    @Column(length=250) private String description;
    @ManyToOne(optional=false, fetch=FetchType.LAZY) @JoinColumn(name="performed_by") private AppUser performedBy;
    @Column(name="created_at", nullable=false, updatable=false) private Instant createdAt=Instant.now();
    protected AccountTransaction() {}
    public AccountTransaction(BankAccount account, TransactionType type, BigDecimal amount, String reference, String description, AppUser actor){this.account=account;this.type=type;this.amount=amount;this.balanceAfter=account.getBalance();this.reference=reference;this.description=description;this.performedBy=actor;}
    public Long getId(){return id;} public Long getAccountId(){return account.getId();} public TransactionType getType(){return type;} public BigDecimal getAmount(){return amount;} public BigDecimal getBalanceAfter(){return balanceAfter;} public String getReference(){return reference;} public String getDescription(){return description;} public Instant getCreatedAt(){return createdAt;}
}

