-- Thiết lập mặc định cho database (chạy 1 lần)
SET client_encoding = 'UTF8';

-- Bảng roles
CREATE TABLE IF NOT EXISTS roles (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Bảng users
CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     role_id INT NOT NULL REFERENCES roles(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(30),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Bảng services
CREATE TABLE IF NOT EXISTS services (
                                        id SERIAL PRIMARY KEY,
                                        code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    duration_minutes INT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Bảng appointments
CREATE TABLE IF NOT EXISTS appointments (
                                            id BIGSERIAL PRIMARY KEY,
                                            patient_id INT NOT NULL REFERENCES users(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
    dentist_id INT NOT NULL REFERENCES users(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
    service_id INT NOT NULL REFERENCES services(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
    scheduled_at TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending','confirmed','completed','cancelled')),
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Bảng dentist_feedback
CREATE TABLE IF NOT EXISTS dentist_feedback (
                                                id BIGSERIAL PRIMARY KEY,
                                                appointment_id BIGINT NOT NULL REFERENCES appointments(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    dentist_id INT NOT NULL REFERENCES users(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
    rating SMALLINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Bảng service_feedback
CREATE TABLE IF NOT EXISTS service_feedback (
                                                id BIGSERIAL PRIMARY KEY,
                                                appointment_id BIGINT NOT NULL REFERENCES appointments(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    service_id INT NOT NULL REFERENCES services(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
    patient_id INT NOT NULL REFERENCES users(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
    rating SMALLINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Bảng treatment_courses
CREATE TABLE IF NOT EXISTS treatment_courses (
                                                 id BIGSERIAL PRIMARY KEY,
                                                 patient_id INT NOT NULL REFERENCES users(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
    dentist_id INT NOT NULL REFERENCES users(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    started_at DATE,
    finished_at DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'planned' CHECK (status IN ('planned','in_progress','completed','stopped')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

ALTER TABLE treatment_courses
ADD COLUMN total_cost DECIMAL(12,2) DEFAULT 0.00;


-- Bảng treatment_details
CREATE TABLE IF NOT EXISTS treatment_details (
                                                 id BIGSERIAL PRIMARY KEY,
                                                 course_id BIGINT NOT NULL REFERENCES treatment_courses(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    appointment_id BIGINT REFERENCES appointments(id)
    ON DELETE SET NULL ON UPDATE CASCADE,
    service_id INT REFERENCES services(id)
    ON DELETE SET NULL ON UPDATE CASCADE,
    performed_at TIMESTAMP,
    notes TEXT,
    cost DECIMAL(12,2) DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );


CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Gắn trigger cho tất cả bảng có updated_at
CREATE TRIGGER trg_update_roles BEFORE UPDATE ON roles
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_update_users BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_update_services BEFORE UPDATE ON services
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_update_appointments BEFORE UPDATE ON appointments
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_update_treatment_courses BEFORE UPDATE ON treatment_courses
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_update_treatment_details BEFORE UPDATE ON treatment_details
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
