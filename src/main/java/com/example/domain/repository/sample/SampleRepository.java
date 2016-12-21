package com.example.domain.repository.sample;

import com.example.domain.model.StoredTransactionToken;

import java.util.List;

/**
 * Created by ikeya on 16/12/22.
 */
public interface SampleRepository {
    List<StoredTransactionToken> findAll();
}
