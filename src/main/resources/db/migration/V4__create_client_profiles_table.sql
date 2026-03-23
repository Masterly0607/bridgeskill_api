CREATE TABLE client_profiles (
                                 id BIGSERIAL PRIMARY KEY,
                                 user_id BIGINT NOT NULL UNIQUE,
                                 company_name VARCHAR(150) NOT NULL,
                                 company_description TEXT,
                                 phone VARCHAR(20),
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);