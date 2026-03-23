CREATE TABLE jobs (
                      id BIGSERIAL PRIMARY KEY,
                      client_id BIGINT NOT NULL,
                      title VARCHAR(150) NOT NULL,
                      description TEXT NOT NULL,
                      category VARCHAR(100),
                      location VARCHAR(100),
                      salary NUMERIC(12,2),
                      status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);