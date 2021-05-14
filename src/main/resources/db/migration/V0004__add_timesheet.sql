-- Schema sb-test --

-- Table timereport --
CREATE TABLE timereport (
    id   SERIAL      NOT NULL PRIMARY KEY,
    user_name  VARCHAR(10) NOT NULL,
    department_name VARCHAR(40) NOT NULL,
    project_name VARCHAR(100) NOT NULL,
    nb_hours INTEGER NOT NULL,
    reported_time TIMESTAMP NOT NULL
);

-- Create Function for Having No change on user_name column BEFORE UPDATE --
CREATE FUNCTION noupdateonusernamecolumn() RETURNS trigger
    LANGUAGE plpgsql AS
$$BEGIN
   IF NEW.user_name <> OLD.user_name THEN
      RAISE EXCEPTION 'not allowed';
END IF;
RETURN NEW;
END;$$;

-- Create A trigger in employee table --
CREATE TRIGGER noupdateonusernamecolumn
    BEFORE UPDATE ON employee FOR EACH ROW
    EXECUTE PROCEDURE noupdateonusernamecolumn();