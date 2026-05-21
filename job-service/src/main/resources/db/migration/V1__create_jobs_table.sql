CREATE TABLE jobs (
                      id            BIGINT AUTO_INCREMENT PRIMARY KEY,
                      company_id    BIGINT NOT NULL,
                      title         VARCHAR(255) NOT NULL,
                      description   TEXT NOT NULL,
                      location      VARCHAR(255),
                      job_type      ENUM('FULL_TIME','PART_TIME','CONTRACT','INTERNSHIP') NOT NULL,
                      status        ENUM('OPEN','CLOSED','DRAFT') NOT NULL DEFAULT 'OPEN',
                      salary_min    DECIMAL(10,2),
                      salary_max    DECIMAL(10,2),
                      skills        TEXT,
                      created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);