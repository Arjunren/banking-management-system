package com.arjunren.banking;

import com.arjunren.banking.domain.Role; import com.arjunren.banking.dto.Dtos.TransferRequest; import com.arjunren.banking.entity.*; import com.arjunren.banking.exception.DomainException; import com.arjunren.banking.repository.*; import com.arjunren.banking.service.BankingService;
import java.math.BigDecimal; import org.junit.jupiter.api.*; import org.springframework.beans.factory.annotation.Autowired; import org.springframework.boot.test.context.SpringBootTest; import org.springframework.test.context.ActiveProfiles; import static org.assertj.core.api.Assertions.*;

@SpringBootTest @ActiveProfiles("test") class BankingServiceTests {
    @Autowired UserRepository users; @Autowired BankAccountRepository accounts; @Autowired AccountTransactionRepository transactions; @Autowired BankingService service;
    AppUser owner,other; BankAccount source,target;
    @BeforeEach void setup(){transactions.deleteAll();accounts.deleteAll();users.deleteAll();owner=users.save(new AppUser("owner@example.com","x","Owner",Role.CUSTOMER));other=users.save(new AppUser("other@example.com","x","Other",Role.CUSTOMER));source=accounts.save(new BankAccount("AC000000000000000001",owner));target=accounts.save(new BankAccount("AC000000000000000002",other));service.deposit(source.getId(),new BigDecimal("100.00"),"fund",owner);}
    @Test void transferIsAtomicAndBalanced(){service.transfer(new TransferRequest(source.getId(),target.getId(),new BigDecimal("40.00"),"rent"),owner);assertThat(accounts.findById(source.getId()).orElseThrow().getBalance()).isEqualByComparingTo("60.00");assertThat(accounts.findById(target.getId()).orElseThrow().getBalance()).isEqualByComparingTo("40.00");assertThat(transactions.count()).isEqualTo(3);}
    @Test void preventsOverdraft(){assertThatThrownBy(()->service.withdraw(source.getId(),new BigDecimal("101.00"),null,owner)).isInstanceOf(DomainException.class).hasMessage("Insufficient funds");}
    @Test void enforcesOwnership(){assertThatThrownBy(()->service.withdraw(source.getId(),BigDecimal.ONE,null,other)).isInstanceOf(DomainException.class).hasMessageContaining("another customer's");}
    @Test void rejectsSelfTransfer(){assertThatThrownBy(()->service.transfer(new TransferRequest(source.getId(),source.getId(),BigDecimal.ONE,null),owner)).isInstanceOf(DomainException.class);}
}
