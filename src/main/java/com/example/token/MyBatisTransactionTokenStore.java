package com.example.token;

import com.example.domain.model.StoredTransactionToken;
import com.example.domain.repository.StoredTransactionTokenRepository;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;
import org.terasoluna.gfw.web.token.TokenStringGenerator;
import org.terasoluna.gfw.web.token.transaction.TransactionToken;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenStore;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
        } catch (PessimisticLockingFailureException e) {
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
        String sessionId = getSession().getId();
        tokenRepository.deleteOlderThanNLatest(tokenName, sessionId, transactionTokenSizePerTokenName - 1);
        return generator.generate(UUID.randomUUID().toString());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void store(TransactionToken transactionToken) {
        StoredTransactionToken token = new StoredTransactionToken();
        token.setTokenName(transactionToken.getTokenName());
        token.setTokenKey(transactionToken.getTokenKey());
        token.setTokenValue(transactionToken.getTokenValue());
        token.setSessionId(getSession().getId());
        token.setCreatedAt(dateFactory.newDate());
        tokenRepository.insert(token);

        // To remind to clean the stored token when the session is invalidated
        getSession();
    }

    /**
     * Returns {@link HttpSession} from request context<br>
     *
     * @return http session object
     */
    HttpSession getSession() {
        return getRequest().getSession(true);
    }

    /**
     * Returns {@link HttpServletRequest} from request context<br>
     *
     * @return http request in this context
     */
    HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
    }
}
