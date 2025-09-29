-- 创建数据库
create database ins_talk
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

use ins_talk;


-- 用户表
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) UNIQUE NOT NULL,
                                     password VARCHAR(255) NOT NULL,
                                     email VARCHAR(100) UNIQUE,
                                     nickname VARCHAR(50),
                                     avatar VARCHAR(255),
                                     status ENUM('ONLINE', 'OFFLINE', 'BUSY') DEFAULT 'OFFLINE',
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=100001;

-- 好友关系表
CREATE TABLE IF NOT EXISTS friendships (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           user_id BIGINT NOT NULL,
                                           friend_id BIGINT NOT NULL,
                                           status ENUM('PENDING', 'ACCEPTED', 'BLOCKED') DEFAULT 'PENDING',
                                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                           FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE,
                                           UNIQUE KEY unique_friendship (user_id, friend_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=100001;

-- 群组表
CREATE TABLE IF NOT EXISTS chat_groups (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           name VARCHAR(100) NOT NULL,
                                           description TEXT,
                                           owner_id BIGINT NOT NULL,
                                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                           FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=100001;

-- 群组成员表
CREATE TABLE IF NOT EXISTS group_members (
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             group_id BIGINT NOT NULL,
                                             user_id BIGINT NOT NULL,
                                             role ENUM('OWNER', 'ADMIN', 'MEMBER') DEFAULT 'MEMBER',
                                             joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                             FOREIGN KEY (group_id) REFERENCES chat_groups(id) ON DELETE CASCADE,
                                             FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                             UNIQUE KEY unique_group_member (group_id, user_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=100001;

-- 聊天消息表
CREATE TABLE IF NOT EXISTS messages (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        sender_id BIGINT NOT NULL,
                                        receiver_id BIGINT,
                                        group_id BIGINT,
                                        content TEXT NOT NULL,
                                        message_type ENUM('TEXT', 'IMAGE', 'FILE') DEFAULT 'TEXT',
                                        sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
                                        FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE,
                                        FOREIGN KEY (group_id) REFERENCES chat_groups(id) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=100001;

-- 消息状态表(已读/未读)
CREATE TABLE IF NOT EXISTS message_status (
                                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                              message_id BIGINT NOT NULL,
                                              user_id BIGINT NOT NULL,
                                              is_read BOOLEAN DEFAULT FALSE,
                                              read_at TIMESTAMP,
                                              FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE,
                                              FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                              UNIQUE KEY unique_message_status (message_id, user_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=100001;