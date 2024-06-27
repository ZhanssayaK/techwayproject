CREATE TABLE users (
                       id           bigserial
                           primary key,
                       username VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(255) NOT NULL,
                       UNIQUE (username),
                       UNIQUE (email)
);

CREATE TABLE currency (
                          id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
                          created_date TIMESTAMP NOT NULL,
                          updated_date TIMESTAMP,
                          code VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE currency_rate (
                               id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
                               created_date TIMESTAMP NOT NULL,
                               updated_date TIMESTAMP,
                               date_at TIMESTAMP NOT NULL,
                               value DECIMAL(19, 4) NOT NULL,
                               currency_id UUID NOT NULL,
                               CONSTRAINT fk_currency FOREIGN KEY (currency_id) REFERENCES currency (id)
);

CREATE TABLE conversion_history (
                                    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
                                    created_date TIMESTAMP NOT NULL,
                                    updated_date TIMESTAMP,
                                    user_id BIGINT NOT NULL,
                                    from_currency VARCHAR(3) NOT NULL,
                                    to_currency VARCHAR(3) NOT NULL,
                                    amount DECIMAL(19, 4) NOT NULL,
                                    conversion_date_time TIMESTAMP NOT NULL,
                                    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id)
);