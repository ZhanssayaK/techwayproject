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
                                    from_currency UUID NOT NULL,
                                    to_currency UUID NOT NULL,
                                    amount DECIMAL(19, 4) NOT NULL,
                                    conversion_date_time TIMESTAMP NOT NULL,
                                    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id),
                                    CONSTRAINT fk_from_currency FOREIGN KEY (from_currency) REFERENCES currency (id),
                                    CONSTRAINT fk_to_currency FOREIGN KEY (to_currency) REFERENCES currency (id)
);
