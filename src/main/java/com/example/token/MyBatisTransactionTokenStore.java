package com.example.token;

import com.example.domain.model.StoredTransactionToken;
import com.example.domain.repository.StoredTransactionTokenRepository;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;
import org.terasoluna.gfw.web.token.TokenStringGenerator;
import org.terasoluna.gfw.web.token.transaction.TransactionToken;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenStore;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Created by ikeya on 16/12/16.
 */
public class MyBatisTransactionTokenStore implements TransactionTokenStore {

    @Inject
    StoredTransactionTokenRepository tokenRepository;

    @Inject
    JodaTimeDateFactory dateFactory;

    private final int transactionTokenSizePerTokenName;

    /**
     * generator for token string
     */
    private final TokenStringGenerator generator;

    public MyBatisTransactionTokenStore(int transactionTokenSizePerTokenName, TokenStringGenerator generator) {
        this.transactionTokenSizePerTokenName = transactionTokenSizePerTokenName;
        this.generator = generator;
    }

    public MyBatisTransactionTokenStore(int transactionTokenSizePerTokenName) {
        this(transactionTokenSizePerTokenName, new TokenStringGenerator());
    }

    public MyBatisTransactionTokenStore() {
        this(10, new TokenStringGenerator());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String getAndClear(TransactionToken transactionToken) {
        String name = transactionToken.getTokenName();
        String key = transactionToken.getTokenKey();

        try {
            StoredTransactionToken token = tokenRepository.findOneForUpdate(name, key);
            if (token == null) {
                return null;
            }

            // FIXME for trial
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            tokenRepository.delete(name, key);
            return token.getTokenValue();
        } catch (CannotAcquireLockException e) {
            // FIXME
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void remove(TransactionToken transactionToken) {
        String name = transactionToken.getTokenName();
        String key = transactionToken.getTokenKey();
        tokenRepository.delete(name, key);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String createAndReserveTokenKey(String tokenName) {
        String newTokenKey = generator.generate(UUID.randomUUID().toString());
        tokenRepository.deleteOlderThanNLatest(tokenName, transactionTokenSizePerTokenName - 1);
        return newTokenKey;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void store(TransactionToken transactionToken) {
        StoredTransactionToken token = new StoredTransactionToken();
        token.setTokenName(transactionToken.getTokenName());
        token.setTokenKey(transactionToken.getTokenKey());
        token.setTokenValue(transactionToken.getTokenValue());
        token.setCreatedAt(dateFactory.newDate());
        tokenRepository.insert(token);
    }

}
