CREATE TABLE users (
                       id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email       VARCHAR(255) NOT NULL UNIQUE,
                       password    VARCHAR(255) NOT NULL,
                       role        ENUM('ROLE_ADMIN', 'ROLE_COMPANY', 'ROLE_CANDIDATE') NOT NULL,
                       created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);