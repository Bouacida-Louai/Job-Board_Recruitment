CREATE DATABASE IF NOT EXISTS auth_db;
CREATE DATABASE IF NOT EXISTS company_db;
CREATE DATABASE IF NOT EXISTS job_db;
CREATE DATABASE IF NOT EXISTS candidate_db;
CREATE DATABASE IF NOT EXISTS application_db;

GRANT ALL PRIVILEGES ON auth_db.* TO 'recruitment'@'%';
GRANT ALL PRIVILEGES ON company_db.* TO 'recruitment'@'%';
GRANT ALL PRIVILEGES ON job_db.* TO 'recruitment'@'%';
GRANT ALL PRIVILEGES ON candidate_db.* TO 'recruitment'@'%';
GRANT ALL PRIVILEGES ON application_db.* TO 'recruitment'@'%';

FLUSH PRIVILEGES;