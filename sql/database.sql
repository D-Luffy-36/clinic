-- Thiết lập mặc định cho database (chạy 1 lần)
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- Bảng Roles
CREATE TABLE IF NOT EXISTS Roles (
                                     id INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                     name VARCHAR(50) NOT NULL,
    description VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY ux_roles_name (name)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Users
CREATE TABLE IF NOT EXISTS Users (
                                     id INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                     role_id INT UNSIGNED NOT NULL,
                                     email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(30) DEFAULT NULL,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY ux_users_email (email),
    INDEX idx_users_role_id (role_id),
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES Roles(id)
                                                            ON DELETE RESTRICT ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Services
CREATE TABLE IF NOT EXISTS Services (
                                        id INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                        code VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT DEFAULT NULL,
    price DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    duration_minutes INT UNSIGNED DEFAULT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY ux_services_code (code)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Appointments
CREATE TABLE IF NOT EXISTS Appointments (
                                            id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                            patient_id INT UNSIGNED NOT NULL,
                                            dentist_id INT UNSIGNED NOT NULL,
                                            service_id INT UNSIGNED NOT NULL,
                                            scheduled_at DATETIME NOT NULL,
                                            status ENUM('pending','confirmed','completed','cancelled') NOT NULL DEFAULT 'pending',
    notes TEXT DEFAULT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_appointments_patient (patient_id),
    INDEX idx_appointments_dentist (dentist_id),
    INDEX idx_appointments_service (service_id),
    CONSTRAINT fk_appointments_patient FOREIGN KEY (patient_id) REFERENCES Users(id)
                                                            ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_appointments_dentist FOREIGN KEY (dentist_id) REFERENCES Users(id)
                                                            ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_appointments_service FOREIGN KEY (service_id) REFERENCES Services(id)
                                                            ON DELETE RESTRICT ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Dentist_Feedback (một appointment có thể có nhiều feedback từ dentist)
CREATE TABLE IF NOT EXISTS Dentist_Feedback (
                                                id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                                appointment_id BIGINT UNSIGNED NOT NULL,
                                                dentist_id INT UNSIGNED NOT NULL,
                                                rating TINYINT UNSIGNED NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT DEFAULT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_dfb_appointment (appointment_id),
    INDEX idx_dfb_dentist (dentist_id),
    CONSTRAINT fk_dfb_appointment FOREIGN KEY (appointment_id) REFERENCES Appointments(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_dfb_dentist FOREIGN KEY (dentist_id) REFERENCES Users(id)
    ON DELETE RESTRICT ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Service_Feedback (một appointment có thể có nhiều feedback cho service)
CREATE TABLE IF NOT EXISTS Service_Feedback (
                                                id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                                appointment_id BIGINT UNSIGNED NOT NULL,
                                                service_id INT UNSIGNED NOT NULL,
                                                patient_id INT UNSIGNED NOT NULL,
                                                rating TINYINT UNSIGNED NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT DEFAULT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_sfb_appointment (appointment_id),
    INDEX idx_sfb_service (service_id),
    INDEX idx_sfb_patient (patient_id),
    CONSTRAINT fk_sfb_appointment FOREIGN KEY (appointment_id) REFERENCES Appointments(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_sfb_service FOREIGN KEY (service_id) REFERENCES Services(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_sfb_patient FOREIGN KEY (patient_id) REFERENCES Users(id)
    ON DELETE RESTRICT ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Treatment_Courses
CREATE TABLE IF NOT EXISTS Treatment_Courses (
                                                 id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                                 patient_id INT UNSIGNED NOT NULL,
                                                 dentist_id INT UNSIGNED NOT NULL,
                                                 title VARCHAR(255) NOT NULL,
    description TEXT DEFAULT NULL,
    started_at DATE DEFAULT NULL,
    finished_at DATE DEFAULT NULL,
    status ENUM('planned','in_progress','completed','stopped') NOT NULL DEFAULT 'planned',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_tc_patient (patient_id),
    INDEX idx_tc_dentist (dentist_id),
    CONSTRAINT fk_tc_patient FOREIGN KEY (patient_id) REFERENCES Users(id)
                                                            ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_tc_dentist FOREIGN KEY (dentist_id) REFERENCES Users(id)
                                                            ON DELETE RESTRICT ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Treatment_Details (chi tiết mỗi course)
CREATE TABLE IF NOT EXISTS Treatment_Details (
                                                 id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                                 course_id BIGINT UNSIGNED NOT NULL,
                                                 appointment_id BIGINT UNSIGNED DEFAULT NULL,
                                                 service_id INT UNSIGNED DEFAULT NULL,
                                                 performed_at DATETIME DEFAULT NULL,
                                                 notes TEXT DEFAULT NULL,
                                                 cost DECIMAL(12,2) DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_td_course (course_id),
    INDEX idx_td_appointment (appointment_id),
    INDEX idx_td_service (service_id),
    CONSTRAINT fk_td_course FOREIGN KEY (course_id) REFERENCES Treatment_Courses(id)
                                                            ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_td_appointment FOREIGN KEY (appointment_id) REFERENCES Appointments(id)
                                                            ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_td_service FOREIGN KEY (service_id) REFERENCES Services(id)
                                                            ON DELETE SET NULL ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;


RENAME TABLE `Users` TO `tmp_users`;
RENAME TABLE `tmp_users` TO `users`;

RENAME TABLE `Roles` TO `tmp_roles`;
RENAME TABLE `tmp_roles` TO `roles`;

RENAME TABLE `Services` TO `tmp_services`;
RENAME TABLE `tmp_services` TO `services`;

RENAME TABLE `Appointments` TO `tmp_appointments`;
RENAME TABLE `tmp_appointments` TO `appointments`;

RENAME TABLE `Dentist_Feedback` TO `tmp_dentist_feedback`;
RENAME TABLE `tmp_dentist_feedback` TO `dentist_feedback`;

RENAME TABLE `Service_Feedback` TO `tmp_service_feedback`;
RENAME TABLE `tmp_service_feedback` TO `service_feedback`;

RENAME TABLE `Treatment_Courses` TO `tmp_treatment_courses`;
RENAME TABLE `tmp_treatment_courses` TO `treatment_courses`;

RENAME TABLE `Treatment_Details` TO `tmp_treatment_details`;
RENAME TABLE `tmp_treatment_details` TO `treatment_details`;

ALTER TABLE roles CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
