package com.example.domain.repository;

import com.example.domain.model.StoredTransactionToken;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ikeya on 16/12/16.
 */
public interface StoredTransactionTokenRepository {
    StoredTransactionToken findOneForUpdate(@Param("tokenName") String tokenName, @Param("tokenKey") String tokenKey);
    void delete(@Param("tokenName") String tokenName, @Param("tokenKey") String tokenKey);
    void insert(StoredTransactionToken token);
    void deleteOlderThanNLatest(@Param("tokenName") String tokenName, @Param("num") int num);
}
