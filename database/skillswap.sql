-- =====================================================================
-- SkillSwap Database Schema & Initial Seed Data
-- Designed Exclusively for SRM UNIVERSITY AP
-- Database Name: skillswap_db
-- =====================================================================

CREATE DATABASE IF NOT EXISTS skillswap_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE skillswap_db;

-- 1. USERS TABLE
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(30) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    college VARCHAR(150) DEFAULT 'SRM UNIVERSITY AP',
    department VARCHAR(100) NOT NULL,
    year_of_study INT NOT NULL,
    bio TEXT,
    profile_image VARCHAR(255) DEFAULT 'default-avatar.png',
    role VARCHAR(20) DEFAULT 'STUDENT',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_email (email),
    INDEX idx_user_student_id (student_id),
    INDEX idx_user_status (status)
) ENGINE=InnoDB;

-- 2. SKILLS TABLE
CREATE TABLE IF NOT EXISTS skills (
    skill_id INT AUTO_INCREMENT PRIMARY KEY,
    skill_name VARCHAR(100) UNIQUE NOT NULL,
    category VARCHAR(100) NOT NULL,
    description TEXT,
    INDEX idx_skill_category (category)
) ENGINE=InnoDB;

-- 3. USER_SKILLS TABLE (Many-to-Many with OFFER / WANT)
CREATE TABLE IF NOT EXISTS user_skills (
    user_skill_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    skill_id INT NOT NULL,
    skill_type VARCHAR(20) NOT NULL, -- 'OFFER' or 'WANT'
    proficiency VARCHAR(30) NOT NULL, -- 'Beginner', 'Intermediate', 'Advanced', 'Expert'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_user_skill_type (user_id, skill_id, skill_type),
    CONSTRAINT fk_us_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_us_skill FOREIGN KEY (skill_id) REFERENCES skills(skill_id) ON DELETE CASCADE,
    INDEX idx_us_type (skill_type)
) ENGINE=InnoDB;

-- 4. USER_AVAILABILITY TABLE
CREATE TABLE IF NOT EXISTS user_availability (
    availability_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    CONSTRAINT fk_ua_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_ua_day (day_of_week)
) ENGINE=InnoDB;

-- 5. EXCHANGE_REQUESTS TABLE
CREATE TABLE IF NOT EXISTS exchange_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    offered_skill_id INT NOT NULL,
    requested_skill_id INT NOT NULL,
    message TEXT,
    status VARCHAR(20) DEFAULT 'PENDING', -- 'PENDING', 'ACCEPTED', 'REJECTED', 'CANCELLED'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_er_sender FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_er_receiver FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_er_offered_skill FOREIGN KEY (offered_skill_id) REFERENCES skills(skill_id),
    CONSTRAINT fk_er_requested_skill FOREIGN KEY (requested_skill_id) REFERENCES skills(skill_id),
    INDEX idx_er_status (status)
) ENGINE=InnoDB;

-- 6. LEARNING_SESSIONS TABLE
CREATE TABLE IF NOT EXISTS learning_sessions (
    session_id INT AUTO_INCREMENT PRIMARY KEY,
    request_id INT NOT NULL,
    teacher_id INT NOT NULL,
    learner_id INT NOT NULL,
    skill_id INT NOT NULL,
    session_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    meeting_mode VARCHAR(20) DEFAULT 'ONLINE', -- 'ONLINE' or 'OFFLINE'
    meeting_details VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'SCHEDULED', -- 'SCHEDULED', 'COMPLETED', 'CANCELLED'
    teacher_confirmed BOOLEAN DEFAULT FALSE,
    learner_confirmed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ls_request FOREIGN KEY (request_id) REFERENCES exchange_requests(request_id) ON DELETE CASCADE,
    CONSTRAINT fk_ls_teacher FOREIGN KEY (teacher_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_ls_learner FOREIGN KEY (learner_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_ls_skill FOREIGN KEY (skill_id) REFERENCES skills(skill_id),
    INDEX idx_ls_status (status),
    INDEX idx_ls_date (session_date)
) ENGINE=InnoDB;

-- 7. CREDIT_ACCOUNTS TABLE
CREATE TABLE IF NOT EXISTS credit_accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE NOT NULL,
    balance INT NOT NULL DEFAULT 5, -- 5 welcome credits
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_ca_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 8. CREDIT_TRANSACTIONS TABLE
CREATE TABLE IF NOT EXISTS credit_transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    session_id INT NULL,
    amount INT NOT NULL,
    transaction_type VARCHAR(20) NOT NULL, -- 'WELCOME', 'EARN', 'SPEND'
    description VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ct_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_ct_session FOREIGN KEY (session_id) REFERENCES learning_sessions(session_id) ON DELETE SET NULL,
    INDEX idx_ct_user (user_id)
) ENGINE=InnoDB;

-- 9. REVIEWS TABLE
CREATE TABLE IF NOT EXISTS reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    session_id INT NOT NULL,
    reviewer_id INT NOT NULL,
    reviewed_user_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_session_reviewer (session_id, reviewer_id),
    CONSTRAINT fk_rev_session FOREIGN KEY (session_id) REFERENCES learning_sessions(session_id) ON DELETE CASCADE,
    CONSTRAINT fk_rev_reviewer FOREIGN KEY (reviewer_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_rev_reviewed FOREIGN KEY (reviewed_user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 10. NOTIFICATIONS TABLE
CREATE TABLE IF NOT EXISTS notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50) DEFAULT 'GENERAL',
    link VARCHAR(255) DEFAULT '#',
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_notif_user_read (user_id, is_read)
) ENGINE=InnoDB;


-- =====================================================================
-- SEED DATA FOR SRM UNIVERSITY AP
-- =====================================================================

-- Standard Skills Catalog
INSERT INTO skills (skill_name, category, description) VALUES
('Java Programming', 'Programming', 'Core Java, OOPs, Collections, Exception Handling, Multithreading, and JDBC'),
('Python Programming', 'Programming', 'Python fundamentals, data structures, scripting, and automation'),
('Web Development', 'Web Development', 'HTML5, CSS3, modern JavaScript, responsive design, and DOM manipulation'),
('React.js', 'Web Development', 'Component architecture, Hooks, State management, and SPA development'),
('Data Science', 'Data Science', 'Data exploration, Pandas, NumPy, data cleaning, and statistical analysis'),
('Machine Learning', 'Artificial Intelligence', 'Supervised & unsupervised learning, scikit-learn, regression, and classification'),
('Graphic Designing', 'Graphic Designing', 'Figma, Adobe Photoshop, UI layouts, color theory, and digital graphics'),
('Video Editing', 'Video Editing', 'Adobe Premiere Pro, DaVinci Resolve, timeline cutting, color grading, and audio syncing'),
('Photography', 'Photography', 'Camera exposure triangle, framing, portraiture, and Lightroom editing'),
('Public Speaking', 'Communication', 'Toastmasters speaking frameworks, stage presence, speech delivery, and pacing'),
('Communication Skills', 'Communication', 'Professional business email etiquette, interview mastery, and active listening'),
('Music & Acoustic Guitar', 'Music', 'Chords, rhythm, strumming patterns, and music theory basics'),
('Financial Literacy', 'Financial Literacy', 'Personal budgeting, student investments, mutual funds, and smart financial habits'),
('Competitive Programming', 'Programming', 'Data Structures & Algorithms, C++, dynamic programming, and LeetCode mastery'),
('3D Modeling & CAD', 'Engineering Design', 'SolidWorks, AutoCAD, parametric 3D modeling, and 3D printing prep')
ON DUPLICATE KEY UPDATE skill_name = VALUES(skill_name);

-- Seed Users (Password for all sample students is 'Password@123', admin is 'Admin@123')
-- SHA-256 hash with salt will be managed by PasswordUtil
-- Pre-computed hashes for salt "srmap_skillswap_salt":
-- Demo password hashes below match PasswordUtil.hashPassword().

INSERT INTO users (user_id, student_id, full_name, email, password_hash, college, department, year_of_study, bio, profile_image, role, status) VALUES
(1, 'APADMIN00001', 'Administrator', 'admin@srmap.edu.in', '437a1b461e849066d70c2b4419051934a01d5624f4e11c7ce0471cd7ddce0374', 'SRM UNIVERSITY AP', 'Computer Science and Engineering', 4, 'System Administrator for SkillSwap SRM AP Platform.', 'admin-avatar.png', 'ADMIN', 'ACTIVE'),
(2, 'AP21110010001', 'Aarav Sharma', 'aarav_sharma@srmap.edu.in', '462bae25fd2d9b547fb261c8c2e1e9a4117f4ab3a8bbc1779b084e7cff5036ca', 'SRM UNIVERSITY AP', 'Computer Science and Engineering', 3, 'Full-stack developer passionate about Java and Web technologies. Seeking creative design peers!', 'avatar-1.png', 'STUDENT', 'ACTIVE'),
(3, 'AP21110010002', 'Diya Reddy', 'diya_reddy@srmap.edu.in', '462bae25fd2d9b547fb261c8c2e1e9a4117f4ab3a8bbc1779b084e7cff5036ca', 'SRM UNIVERSITY AP', 'Electronics and Communication Engineering', 3, 'Lead Graphic Designer at SRM AP Media Club. Eager to master backend Java coding!', 'avatar-2.png', 'STUDENT', 'ACTIVE'),
(4, 'AP22110010045', 'Karthik Varma', 'karthik_varma@srmap.edu.in', '462bae25fd2d9b547fb261c8c2e1e9a4117f4ab3a8bbc1779b084e7cff5036ca', 'SRM UNIVERSITY AP', 'Artificial Intelligence and Data Science', 2, 'AI researcher exploring computer vision. Excited to improve speech delivery and public speaking.', 'avatar-3.png', 'STUDENT', 'ACTIVE'),
(5, 'AP22110010088', 'Sneha Patel', 'sneha_patel@srmap.edu.in', '462bae25fd2d9b547fb261c8c2e1e9a4117f4ab3a8bbc1779b084e7cff5036ca', 'SRM UNIVERSITY AP', 'School of Entrepreneurship and Management Studies', 2, 'Toastmasters finalist & debater. Passionate about helping peers speak with confidence while learning Python.', 'avatar-4.png', 'STUDENT', 'ACTIVE'),
(6, 'AP23110010112', 'Rohan Gupta', 'rohan_gupta@srmap.edu.in', '462bae25fd2d9b547fb261c8c2e1e9a4117f4ab3a8bbc1779b084e7cff5036ca', 'SRM UNIVERSITY AP', 'Mechanical Engineering', 1, 'Robotics enthusiast, 3D printing hobbyist, seeking personal financial management skills.', 'avatar-5.png', 'STUDENT', 'ACTIVE')
ON DUPLICATE KEY UPDATE full_name = VALUES(full_name), password_hash = VALUES(password_hash);

-- Credit Accounts (5 Welcome credits each)
INSERT INTO credit_accounts (user_id, balance) VALUES
(1, 100),
(2, 7),
(3, 5),
(4, 5),
(5, 5),
(6, 5)
ON DUPLICATE KEY UPDATE balance = VALUES(balance);

-- Initial Welcome Credit Transactions
INSERT INTO credit_transactions (user_id, session_id, amount, transaction_type, description) VALUES
(2, NULL, 5, 'WELCOME', 'Welcome bonus credits for joining SkillSwap SRM AP'),
(3, NULL, 5, 'WELCOME', 'Welcome bonus credits for joining SkillSwap SRM AP'),
(4, NULL, 5, 'WELCOME', 'Welcome bonus credits for joining SkillSwap SRM AP'),
(5, NULL, 5, 'WELCOME', 'Welcome bonus credits for joining SkillSwap SRM AP'),
(6, NULL, 5, 'WELCOME', 'Welcome bonus credits for joining SkillSwap SRM AP'),
(2, NULL, 2, 'EARN', 'Taught Java Programming session to Diya Reddy')
ON DUPLICATE KEY UPDATE description = VALUES(description);

-- User Skills (SHOWCASING MUTUAL MATCHES)
-- Aarav (User 2) OFFERS Java (1) and Web Dev (3), WANTS Graphic Designing (7)
-- Diya (User 3) OFFERS Graphic Designing (7) and Photography (9), WANTS Java (1)
INSERT INTO user_skills (user_id, skill_id, skill_type, proficiency) VALUES
(2, 1, 'OFFER', 'Expert'),
(2, 3, 'OFFER', 'Advanced'),
(2, 7, 'WANT', 'Beginner'),

(3, 7, 'OFFER', 'Expert'),
(3, 9, 'OFFER', 'Advanced'),
(3, 1, 'WANT', 'Beginner'),

-- Karthik (User 4) OFFERS Machine Learning (6) and Python (2), WANTS Public Speaking (10)
-- Sneha (User 5) OFFERS Public Speaking (10) and Communication (11), WANTS Python (2)
(4, 6, 'OFFER', 'Advanced'),
(4, 2, 'OFFER', 'Expert'),
(4, 10, 'WANT', 'Beginner'),

(5, 10, 'OFFER', 'Expert'),
(5, 11, 'OFFER', 'Expert'),
(5, 2, 'WANT', 'Intermediate'),

-- Rohan (User 6) OFFERS 3D Modeling (15), WANTS Financial Literacy (13)
(6, 15, 'OFFER', 'Advanced'),
(6, 13, 'WANT', 'Beginner')
ON DUPLICATE KEY UPDATE proficiency = VALUES(proficiency);

-- User Availability
INSERT INTO user_availability (user_id, day_of_week, start_time, end_time) VALUES
(2, 'Monday', '17:00:00', '19:00:00'),
(2, 'Wednesday', '17:00:00', '19:00:00'),
(2, 'Saturday', '10:00:00', '13:00:00'),

(3, 'Monday', '17:00:00', '19:00:00'),
(3, 'Thursday', '16:00:00', '18:00:00'),
(3, 'Saturday', '10:00:00', '12:00:00'),

(4, 'Tuesday', '18:00:00', '20:00:00'),
(4, 'Sunday', '11:00:00', '14:00:00'),

(5, 'Tuesday', '18:00:00', '20:00:00'),
(5, 'Friday', '17:00:00', '19:00:00')
ON DUPLICATE KEY UPDATE day_of_week = VALUES(day_of_week);

-- Exchange Request Sample (Aarav and Diya accepted exchange)
INSERT INTO exchange_requests (request_id, sender_id, receiver_id, offered_skill_id, requested_skill_id, message, status) VALUES
(1, 2, 3, 1, 7, 'Hi Diya! I saw your amazing graphic design portfolio at the SRM AP Tech Fest. I would love to learn UI design in exchange for teaching you Core Java and OOPs!', 'ACCEPTED')
ON DUPLICATE KEY UPDATE status = VALUES(status);

-- Learning Session Sample (Aarav taught Diya Java OOPs)
INSERT INTO learning_sessions (session_id, request_id, teacher_id, learner_id, skill_id, session_date, start_time, end_time, meeting_mode, meeting_details, status, teacher_confirmed, learner_confirmed) VALUES
(1, 1, 2, 3, 1, '2026-11-01', '17:00:00', '18:30:00', 'OFFLINE', 'SRM AP Central Library 2nd Floor Study Pod 3', 'COMPLETED', TRUE, TRUE)
ON DUPLICATE KEY UPDATE status = VALUES(status);

-- Review for Completed Session
INSERT INTO reviews (review_id, session_id, reviewer_id, reviewed_user_id, rating, comment) VALUES
(1, 1, 3, 2, 5, 'Aarav is an exceptional peer mentor! He broke down complex Java concepts like polymorphism and collections using intuitive real-world examples. Highly recommended!')
ON DUPLICATE KEY UPDATE rating = VALUES(rating);

-- Sample Notification
INSERT INTO notifications (user_id, message, type, link, is_read) VALUES
(2, 'Welcome to SkillSwap SRM AP! 5 welcome credits have been credited to your wallet.', 'WALLET', 'credits', TRUE),
(2, 'Diya Reddy accepted your exchange request for Java Programming and Graphic Designing!', 'REQUEST', 'requests', TRUE),
(2, 'Session on Java Programming has been marked completed by both participants. +2 credits earned!', 'SESSION', 'credits', FALSE),
(3, 'Welcome to SkillSwap SRM AP! 5 welcome credits have been credited to your wallet.', 'WALLET', 'credits', TRUE)
ON DUPLICATE KEY UPDATE is_read = VALUES(is_read);

