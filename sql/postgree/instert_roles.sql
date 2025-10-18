BEGIN;

INSERT INTO roles (name, description)
VALUES
    ('admin', 'Quản trị hệ thống'),
    ('doctor', 'Bác sĩ nha khoa'),
    ('patient', 'Người dùng bệnh nhân')
    ON CONFLICT (name) DO NOTHING;

UPDATE roles
SET description = 'Quản trị hệ thống'
WHERE name = 'admin';

UPDATE roles
SET description = 'Bác sĩ nha khoa'
WHERE name = 'doctor';

UPDATE roles
SET description = 'Người dùng bệnh nhân'
WHERE name = 'patient';

COMMIT;

SELECT * FROM roles;
