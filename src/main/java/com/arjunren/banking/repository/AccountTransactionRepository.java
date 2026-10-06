package com.arjunren.banking.repository;
import com.arjunren.banking.entity.AccountTransaction;
import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable; import org.springframework.data.jpa.repository.JpaRepository;
public interface AccountTransactionRepository extends JpaRepository<AccountTransaction,Long>{Page<AccountTransaction> findByAccountId(Long accountId, Pageable pageable);}

