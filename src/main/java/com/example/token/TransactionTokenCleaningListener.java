package com.example.token;

import com.example.domain.repository.StoredTransactionTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by ikeya on 16/12/22.
 */
public class TransactionTokenCleaningListener {

    private static final Logger logger = LoggerFactory.getLogger(TransactionTokenCleaningListener.class);

    @Inject
    StoredTransactionTokenRepository tokenRepository;

    @EventListener
    @Transactional
    public void sessionDestroyed(HttpSessionDestroyedEvent event) {
        String sessionId = event.getSession().getId();
        try {
            tokenRepository.deleteBySessionId(sessionId);
            logger.info("Transaction tokens created by sessionId={} have been cleaned.", sessionId);
        } catch (DataAccessException e) {
            logger.warn("Failed to clean abandoned transaction tokens created by sessionId={}.", sessionId, e);
            // ignore
        }
    }
}
