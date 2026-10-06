CREATE TABLE users (id BIGSERIAL PRIMARY KEY,email VARCHAR(255) NOT NULL UNIQUE,password_hash VARCHAR(255) NOT NULL,name VARCHAR(120) NOT NULL,role VARCHAR(20) NOT NULL CHECK(role IN ('ADMIN','CUSTOMER')),active BOOLEAN NOT NULL DEFAULT TRUE,created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW());
CREATE TABLE auth_tokens (id BIGSERIAL PRIMARY KEY,token_hash VARCHAR(64) NOT NULL UNIQUE,user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,expires_at TIMESTAMPTZ NOT NULL,revoked_at TIMESTAMPTZ,created_at TIMESTAMPTZ NOT NULL DEFAULT NOW());
CREATE INDEX auth_tokens_user_idx ON auth_tokens(user_id);
CREATE INDEX auth_tokens_expires_idx ON auth_tokens(expires_at);
CREATE TABLE bank_accounts (id BIGSERIAL PRIMARY KEY,account_number VARCHAR(20) NOT NULL UNIQUE,owner_id BIGINT NOT NULL REFERENCES users(id),balance NUMERIC(19,2) NOT NULL DEFAULT 0 CHECK(balance>=0),status VARCHAR(20) NOT NULL CHECK(status IN ('ACTIVE','FROZEN','CLOSED')),version BIGINT NOT NULL DEFAULT 0,created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW());
CREATE INDEX bank_accounts_owner_idx ON bank_accounts(owner_id);
CREATE TABLE account_transactions (id BIGSERIAL PRIMARY KEY,account_id BIGINT NOT NULL REFERENCES bank_accounts(id),type VARCHAR(30) NOT NULL CHECK(type IN ('DEPOSIT','WITHDRAWAL','TRANSFER_IN','TRANSFER_OUT')),amount NUMERIC(19,2) NOT NULL CHECK(amount>0),balance_after NUMERIC(19,2) NOT NULL,reference VARCHAR(50) NOT NULL,description VARCHAR(250),performed_by BIGINT NOT NULL REFERENCES users(id),created_at TIMESTAMPTZ NOT NULL DEFAULT NOW());
CREATE INDEX transactions_account_created_idx ON account_transactions(account_id,created_at DESC);
CREATE INDEX transactions_reference_idx ON account_transactions(reference);

