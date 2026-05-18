CREATE TABLE companies (
                           id            BIGINT AUTO_INCREMENT PRIMARY KEY,
                           user_id       BIGINT NOT NULL UNIQUE,
                           name          VARCHAR(255) NOT NULL,
                           description   TEXT,
                           industry      VARCHAR(100),
                           website       VARCHAR(255),
                           location      VARCHAR(255),
                           created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);