package com.taskflowdev.nubankclone.cache;

import com.taskflowdev.nubankclone.account.Account;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

@Service
public class AccountCacheService {
    private static final Duration TTL = Duration.ofMinutes(5);
    private final ObjectProvider<StringRedisTemplate> redisTemplateProvider;

    public AccountCacheService(ObjectProvider<StringRedisTemplate> redisTemplateProvider) {
        this.redisTemplateProvider = redisTemplateProvider;
    }

    public void cacheSummary(String email, Account account) {
        try {
            StringRedisTemplate redisTemplate = redisTemplateProvider.getIfAvailable();
            if (redisTemplate != null) {
                redisTemplate.opsForValue().set(key(email), account.getBalance().toPlainString(), TTL);
            }
        } catch (RuntimeException ignored) {
        }
    }

    public Optional<BigDecimal> getCachedBalance(String email) {
        try {
            StringRedisTemplate redisTemplate = redisTemplateProvider.getIfAvailable();
            if (redisTemplate == null) {
                return Optional.empty();
            }
            String value = redisTemplate.opsForValue().get(key(email));
            return value == null ? Optional.empty() : Optional.of(new BigDecimal(value));
        } catch (RuntimeException ex) {
            return Optional.empty();
        }
    }

    public void evict(String email) {
        try {
            StringRedisTemplate redisTemplate = redisTemplateProvider.getIfAvailable();
            if (redisTemplate != null) {
                redisTemplate.delete(key(email));
            }
        } catch (RuntimeException ignored) {
        }
    }

    private String key(String email) {
        return "account:summary:" + email;
    }
}
