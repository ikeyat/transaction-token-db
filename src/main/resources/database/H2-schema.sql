create table transaction_token (
    token_name varchar(256) not null,
    token_key varchar(32) not null,
    token_value varchar(32) not null,
    created_at timestamp,
    constraint pk_transaction_token primary key (token_name, token_key)
);

create index transaction_token_index_delete_older on transaction_token(token_name, created_at);