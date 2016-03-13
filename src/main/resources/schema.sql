CREATE TABLE IF NOT EXISTS company (
  company_id   INT PRIMARY KEY AUTO_INCREMENT,
  company_name VARCHAR(256)
)
  DEFAULT CHARACTER SET utf8;

CREATE TABLE IF NOT EXISTS meishi (
  meishi_id        INT PRIMARY KEY AUTO_INCREMENT,
  first_name       VARCHAR(128) NOT NULL,
  last_name        VARCHAR(128) NOT NULL,
  first_name_kanji VARCHAR(128),
  last_name_kanji  VARCHAR(128),
  email            VARCHAR(128) NOT NULL,
  company_id       INT          NOT NULL,
  meishi_front     VARCHAR(256) NOT NULL,
  meishi_back      VARCHAR(256),
  login_user       VARCHAR(128) NOT NULL,
  INDEX (first_name),
  INDEX (last_name),
  INDEX (first_name_kanji),
  INDEX (last_name_kanji),
  INDEX (email),
  INDEX (login_user),
  FOREIGN KEY (company_id) REFERENCES company (company_id)
)
  DEFAULT CHARACTER SET utf8;
