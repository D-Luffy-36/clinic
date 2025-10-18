START TRANSACTION;

INSERT INTO roles (name, description)
VALUES
    ('admin', 'Quản trị hệ thống'),
    ('doctor', 'Bác sĩ nha khoa'),
    ('patient', 'Người dùng bệnh nhân');

UPDATE roles
SET description = 'Quản trị hệ thống'
WHERE id = 1;

UPDATE roles
SET description = 'Bác sĩ nha khoa'
WHERE id = 2;

UPDATE roles
SET description = 'Người dùng bệnh nhân'
WHERE id = 3;

COMMIT;

SELECT * FROM roles;
