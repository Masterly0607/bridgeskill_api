CREATE TABLE applications (
                              id BIGSERIAL PRIMARY KEY,
                              job_id BIGINT NOT NULL,
                              student_id BIGINT NOT NULL,
                              cover_letter TEXT,
                              status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              UNIQUE (job_id, student_id)
);