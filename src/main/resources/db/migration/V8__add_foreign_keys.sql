ALTER TABLE users
    ADD CONSTRAINT fk_users_role
        FOREIGN KEY (role_id) REFERENCES roles(id);

ALTER TABLE student_profiles
    ADD CONSTRAINT fk_student_profiles_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE client_profiles
    ADD CONSTRAINT fk_client_profiles_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE jobs
    ADD CONSTRAINT fk_jobs_client
        FOREIGN KEY (client_id) REFERENCES users(id) ON DELETE RESTRICT;

ALTER TABLE applications
    ADD CONSTRAINT fk_applications_job
        FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE RESTRICT;

ALTER TABLE applications
    ADD CONSTRAINT fk_applications_student
        FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE;