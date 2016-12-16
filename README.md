# transaction-token-db

## Motivation
http://terasolunaorg.github.io/guideline/5.2.0.RELEASE/ja/ArchitectureInDetail/WebApplicationDetail/DoubleSubmitProtection.html#double-submit-transactiontokencheck

It provides transaction token check to provide duplicated receiving.
The default implementation uses ``HttpSession`` to store tokens.
In the way, duplicated receiving cannot be avoided completely when ``HttpSession`` is distributedly managed e.g. [Spring Session](http://docs.spring.io/spring-session/docs/current/reference/html5/).
To cover such cases, one of the most general way is to store tokens in RDB that keeps consistency strictly.

## Implementation
See ``MyBatisTransactionTokenStore`` class.

## Try it
```
mvn clean install cargo:run
```

Access http://localhost:8080/transaction-token/
