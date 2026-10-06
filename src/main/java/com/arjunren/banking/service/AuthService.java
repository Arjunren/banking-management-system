package com.arjunren.banking.service;

import com.arjunren.banking.domain.Role; import com.arjunren.banking.dto.Dtos.*; import com.arjunren.banking.entity.*; import com.arjunren.banking.exception.DomainException; import com.arjunren.banking.repository.*; import com.arjunren.banking.security.TokenHash;
import java.security.SecureRandom; import java.time.*; import java.util.*;
import org.springframework.beans.factory.annotation.Value; import org.springframework.http.HttpStatus; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository users; private final AuthTokenRepository tokens; private final BankAccountRepository accounts; private final PasswordEncoder encoder; private final long ttlMinutes; private final SecureRandom random=new SecureRandom();
    public AuthService(UserRepository users,AuthTokenRepository tokens,BankAccountRepository accounts,PasswordEncoder encoder,@Value("${app.security.token-ttl-minutes}") long ttlMinutes){this.users=users;this.tokens=tokens;this.accounts=accounts;this.encoder=encoder;this.ttlMinutes=ttlMinutes;}
    @Transactional public UserResponse register(RegisterRequest r){String email=r.email().trim().toLowerCase(Locale.ROOT);if(users.findByEmailIgnoreCase(email).isPresent())throw new DomainException(HttpStatus.CONFLICT,"Email already exists");var user=users.save(new AppUser(email,encoder.encode(r.password()),r.name().trim(),Role.CUSTOMER));accounts.save(new BankAccount(newAccountNumber(),user));return UserResponse.of(user);}
    @Transactional public TokenResponse login(LoginRequest r){var user=users.findByEmailIgnoreCase(r.email().trim()).orElse(null);String dummy="$2a$12$7ix4QV/fLm5YCYc8FjmwUOe0wIQFkwSoDvgOL1z9uQkC7B/LBkP3q";boolean valid=encoder.matches(r.password(),user==null?dummy:user.getPasswordHash());if(user==null||!user.isActive()||!valid)throw new DomainException(HttpStatus.UNAUTHORIZED,"Email or password is incorrect");byte[] bytes=new byte[32];random.nextBytes(bytes);String raw=Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);Instant expires=Instant.now().plusSeconds(ttlMinutes*60);tokens.save(new AuthToken(TokenHash.sha256(raw),user,expires));return new TokenResponse(raw,"Bearer",expires,UserResponse.of(user));}
    @Transactional public void logout(String raw){tokens.findByTokenHash(TokenHash.sha256(raw)).ifPresent(t->{if(t.getRevokedAt()==null)t.revoke();});}
    private String newAccountNumber(){return "AC"+UUID.randomUUID().toString().replace("-","").substring(0,18).toUpperCase(Locale.ROOT);}
}

