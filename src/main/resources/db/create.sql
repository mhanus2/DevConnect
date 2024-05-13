CREATE TABLE user_account
(
    id           SERIAL PRIMARY KEY,
    username     VARCHAR(50) UNIQUE  NOT NULL,
    first_name   VARCHAR(100)        NOT NULL,
    last_name    VARCHAR(100)        NOT NULL,
    mail         VARCHAR(255) UNIQUE NOT NULL,
    password     VARCHAR(255)        NOT NULL,
    role         VARCHAR(5)          NOT NULL,
    phone_number VARCHAR(21),
    linkedin     VARCHAR(100),
    github       VARCHAR(100)
);

CREATE TABLE skill
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    detail   VARCHAR(255),
    owner_id INT          NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES user_account (id)
);

CREATE TABLE project
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    owner_id    INT         NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES user_account (id)
);

CREATE TABLE tag
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE project_tag
(
    id         SERIAL PRIMARY KEY,
    project_id INT NOT NULL,
    tag_id     INT NOT NULL,
    FOREIGN KEY (project_id) REFERENCES project (id),
    FOREIGN KEY (tag_id) REFERENCES tag (id)
);

CREATE TABLE comment
(
    id         SERIAL PRIMARY KEY,
    value      VARCHAR(255) NOT NULL,
    project_id INT          NOT NULL,
    owner_id   INT          NOT NULL,
    FOREIGN KEY (project_id) REFERENCES project (id),
    FOREIGN KEY (owner_id) REFERENCES user_account (id)
);

CREATE TABLE image
(
    id         SERIAL PRIMARY KEY,
    file_path  VARCHAR(255) NOT NULL,
    project_id INT          NOT NULL,
    FOREIGN KEY (project_id) REFERENCES project (id)
);

CREATE OR REPLACE FUNCTION delete_project_tags()
    RETURNS TRIGGER AS
$$
BEGIN
    DELETE FROM project_tag WHERE tag_id = OLD.id;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER delete_project_tags_trigger
    BEFORE DELETE
    ON tag
    FOR EACH ROW
EXECUTE PROCEDURE delete_project_tags();

CREATE VIEW popular_tags AS
SELECT t.name, count(*) as tag_count
FROM tag t
         JOIN project_tag pt ON t.id = pt.tag_id
GROUP BY t.name
ORDER BY tag_count DESC
LIMIT 5;

CREATE OR REPLACE PROCEDURE add_comment(
    p_project_id INTEGER,
    p_owner_id INTEGER,
    p_value TEXT
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO comment (project_id, owner_id, value)
    VALUES (p_project_id, p_owner_id, p_value);
END;
$$;

create procedure delete_user(IN p_user_id integer)
    language plpgsql
as
$$
BEGIN
    DELETE FROM comment WHERE project_id IN (SELECT id FROM project WHERE owner_id = p_user_id);
    DELETE FROM comment WHERE owner_id = p_user_id;
    DELETE FROM image WHERE project_id IN (SELECT id FROM project WHERE owner_id = p_user_id);
    DELETE FROM project_tag WHERE project_id IN (SELECT id FROM project WHERE owner_id = p_user_id);
    DELETE FROM project WHERE owner_id = p_user_id;
    DELETE FROM skill WHERE owner_id = p_user_id;
    DELETE FROM user_account WHERE id = p_user_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'An error occurred: %', SQLERRM;
END;
$$;

