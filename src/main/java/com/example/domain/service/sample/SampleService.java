package com.example.domain.service.sample;

import com.example.domain.model.StoredTransactionToken;

import java.util.List;

/**
 * Created by ikeya on 16/12/22.
 */
public interface SampleService {
    List<StoredTransactionToken> getAllTransactionTokens();
}
