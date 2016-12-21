package com.example.domain.service.sample;

import com.example.domain.model.StoredTransactionToken;
import com.example.domain.repository.sample.SampleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by ikeya on 16/12/22.
 */
@Service
@Transactional
public class SampleServiceImpl implements SampleService {
    @Inject
    SampleRepository sampleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<StoredTransactionToken> getAllTransactionTokens() {
        return sampleRepository.findAll();
    }
}

