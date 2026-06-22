package com.taskflowdev.nubankclone.cache;

import com.taskflowdev.nubankclone.account.Account;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

@Service
public class AccountCacheService {
    private static final Duration TTL = Duration.ofMinutes(5);
    private final StringRedisTemplate redisTemplate;

    public AccountCacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheSummary(String email, Account account) {
        try {
            redisTemplate.opsForValue().set(key(email), account.getBalance().toPlainString(), TTL);
        } catch (RuntimeException ignored) {
        }
    }

    public Optional<BigDecimal> getCachedBalance(String email) {
        try {
            String value = redisTemplate.opsForValue().get(key(email));
            return value == null ? Optional.empty() : Optional.of(new BigDecimal(value));
        } catch (RuntimeException ex) {
            return Optional.empty();
        }
    }

    public void evict(String email) {
        try {
            redisTemplate.delete(key(email));
        } catch (RuntimeException ignored) {
        }
    }

    private String key(String email) {
        return "account:summary:" + email;
    }
}
