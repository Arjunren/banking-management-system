package com.arjunren.banking.repository;
import com.arjunren.banking.entity.BankAccount;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param;
public interface BankAccountRepository extends JpaRepository<BankAccount,Long>{
    Page<BankAccount> findByOwnerId(Long ownerId, Pageable pageable);
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select a from BankAccount a join fetch a.owner where a.id=:id") Optional<BankAccount> findByIdForUpdate(@Param("id") Long id);
}

