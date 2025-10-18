-- ===============================================
-- 🐳 BOILERPLATE MYSQL COMMANDS (MySQL in Docker)
-- Container: mysql_dental
-- File: mysql_commands.sql
-- ===============================================

-- 🔹 TRUY CẬP CONTAINER MYSQL
-- docker exec -it mysql_dental bash
-- mysql -u root -p

-- ===============================================
-- 🗃️ KIỂM TRA & TRUY XUẤT DỮ LIỆU
-- ===============================================
SHOW DATABASES;
USE dental_db;
SHOW TABLES;
DESCRIBE table_name;
SHOW COLUMNS FROM table_name;
SELECT * FROM table_name LIMIT 10;
SELECT COUNT(*) FROM table_name;

-- ===============================================
-- 🛠️ CHỈNH SỬA DỮ LIỆU
-- ===============================================
INSERT INTO table_name (column1, column2)
VALUES ('value1', 'value2');

UPDATE table_name
SET column1 = 'new_value'
WHERE condition;

DELETE FROM table_name
WHERE condition;

-- ===============================================
-- 🧠 TRUY VẤN NÂNG CAO
-- ===============================================
SELECT * FROM table_name WHERE column1 = 'value';
SELECT * FROM table_name ORDER BY column1 DESC;
SELECT column1, COUNT(*) FROM table_name GROUP BY column1;
