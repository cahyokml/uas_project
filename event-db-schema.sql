-- Database Setup untuk Event Management System
-- Jalankan script ini untuk membuat database dan tabel

-- 1. Buat Database
CREATE DATABASE IF NOT EXISTS event_management 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE event_management;

-- 2. Buat Tabel Events
CREATE TABLE IF NOT EXISTS events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    location VARCHAR(255) NOT NULL,
    description TEXT,
    capacity INT DEFAULT NULL,
    status ENUM('upcoming', 'ongoing', 'completed', 'cancelled') NOT NULL DEFAULT 'upcoming',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_date (date),
    INDEX idx_status (status),
    INDEX idx_date_status (date, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Insert Data Sample (Optional)
INSERT INTO events (title, date, time, location, description, capacity, status) VALUES
('Workshop Laravel', '2025-11-15', '09:00:00', 'Jakarta Convention Center', 'Belajar Laravel dari dasar hingga mahir', 100, 'upcoming'),
('Seminar Digital Marketing', '2025-11-20', '13:00:00', 'Hotel Santika Yogyakarta', 'Strategi pemasaran digital era modern', 150, 'upcoming'),
('Tech Conference 2025', '2025-12-01', '08:00:00', 'Balai Sidang Jakarta', 'Konferensi teknologi terbesar tahun ini', 500, 'upcoming'),
('Webinar React JS', '2025-10-25', '19:00:00', 'Online - Zoom', 'Membuat aplikasi modern dengan React', 200, 'ongoing'),
('Workshop UI/UX Design', '2025-10-15', '10:00:00', 'Design Hub Bandung', 'Fundamental desain antarmuka pengguna', 50, 'completed');

-- 4. View untuk Statistik Event
CREATE OR REPLACE VIEW event_statistics AS
SELECT 
    COUNT(*) as total_events,
    SUM(CASE WHEN status = 'upcoming' THEN 1 ELSE 0 END) as upcoming_events,
    SUM(CASE WHEN status = 'ongoing' THEN 1 ELSE 0 END) as ongoing_events,
    SUM(CASE WHEN status = 'completed' THEN 1 ELSE 0 END) as completed_events,
    SUM(CASE WHEN status = 'cancelled' THEN 1 ELSE 0 END) as cancelled_events,
    SUM(capacity) as total_capacity
FROM events;

-- 5. Stored Procedure untuk Update Status Otomatis
DELIMITER $$

CREATE PROCEDURE update_event_status()
BEGIN
    -- Update status ke 'completed' untuk event yang sudah lewat
    UPDATE events 
    SET status = 'completed' 
    WHERE date < CURDATE() 
    AND status IN ('upcoming', 'ongoing');
    
    -- Update status ke 'ongoing' untuk event hari ini
    UPDATE events 
    SET status = 'ongoing' 
    WHERE date = CURDATE() 
    AND status = 'upcoming';
END$$

DELIMITER ;

-- 6. Event Scheduler untuk Auto-update Status (Opsional)
-- Aktifkan event scheduler di MySQL
SET GLOBAL event_scheduler = ON;

CREATE EVENT IF NOT EXISTS auto_update_event_status
ON SCHEDULE EVERY 1 HOUR
DO CALL update_event_status();

-- 7. Query Berguna untuk Development

-- Lihat semua event yang akan datang
-- SELECT * FROM events WHERE status = 'upcoming' ORDER BY date ASC;

-- Lihat event berdasarkan bulan
-- SELECT * FROM events WHERE YEAR(date) = 2025 AND MONTH(date) = 11;

-- Hitung total kapasitas per bulan
-- SELECT 
--     DATE_FORMAT(date, '%Y-%m') as month,
--     COUNT(*) as event_count,
--     SUM(capacity) as total_capacity
-- FROM events
-- GROUP BY month
-- ORDER BY month;

-- Lihat statistik
-- SELECT * FROM event_statistics;