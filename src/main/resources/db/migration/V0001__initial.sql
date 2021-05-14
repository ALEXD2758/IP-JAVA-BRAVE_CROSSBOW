-- Schema sb-test --

-- Table employee --
CREATE TABLE employee (
  id         SERIAL      NOT NULL PRIMARY KEY,
  user_name  VARCHAR(10) UNIQUE NOT NULL,
  birth_date DATE         NOT NULL,
  first_name VARCHAR(20) NOT NULL,
  last_name  VARCHAR(20) NOT NULL,
  gender     CHAR(1)      NOT NULL,
  hire_date  DATE         NOT NULL,
  password   VARCHAR(150) NOT NULL,
  role       VARCHAR(4)   NOT NULL
);

