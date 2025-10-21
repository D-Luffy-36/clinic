START TRANSACTION;

INSERT INTO services (code, name, description, price, duration_minute, created_at, updated_at)
VALUES
    ('T001', 'Khám tổng quát', 'Kiểm tra tổng thể răng miệng, tư vấn điều trị nếu cần.', 150000, 30, NOW(), NOW()),
    ('T002', 'Cạo vôi răng', 'Loại bỏ mảng bám và cao răng giúp ngăn ngừa viêm nướu.', 250000, 45, NOW(), NOW()),
    ('T003', 'Trám răng thẩm mỹ', 'Trám răng bằng vật liệu composite màu răng thật.', 400000, 60, NOW(), NOW()),
    ('T004', 'Tẩy trắng răng', 'Sử dụng công nghệ laser để làm trắng răng nhanh chóng.', 1200000, 90, NOW(), NOW()),
    ('T005', 'Nhổ răng khôn', 'Tiểu phẫu nhổ răng khôn mọc lệch hoặc gây đau.', 800000, 75, NOW(), NOW()),
    ('T006', 'Niềng răng mắc cài kim loại', 'Chỉnh nha bằng mắc cài kim loại truyền thống.', 25000000, 120, NOW(), NOW()),
    ('T007', 'Niềng răng trong suốt', 'Chỉnh nha bằng khay trong suốt Invisalign.', 45000000, 120, NOW(), NOW()),
    ('T008', 'Cấy ghép Implant', 'Thay thế răng mất bằng trụ Implant và mão sứ.', 18000000, 90, NOW(), NOW()),
    ('T009', 'Bọc răng sứ thẩm mỹ', 'Phục hình răng bằng mão sứ để cải thiện thẩm mỹ.', 1500000, 60, NOW(), NOW()),
    ('T010', 'Khám & điều trị nha chu', 'Điều trị viêm nướu, viêm nha chu chuyên sâu.', 600000, 60, NOW(), NOW());

COMMIT;
