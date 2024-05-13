INSERT INTO user_account (username, first_name, last_name, mail, password, role)
VALUES ('admin', 'Lukáš', 'Molan', 'admin@domain.cz', '$2a$12$OBep4.3e7dwsel9FJCmBheNyAH0PdQ3Q2oAEAwW84/mfEcn7HSCeq',
        'ADMIN'),
       ('sheroutek', 'Štěpán', 'Heroutek', 'sheroutek@mail.com',
        '$2a$12$pde9Hc4PNEGRwYlRioc0sOwzAXsBy92b.LmbF1b93/XchlVV9jhtG', 'USER'),
       ('mkutil', 'Milan', 'Kutil', 'mkutil@mail.com', '$2a$12$ITcnJ4d7S0fTpUGcvd9HpOfNpM80GAoGd3RjddvLAgiHvgb2MVmrq',
        'USER');

INSERT INTO skill (name, detail, owner_id)
VALUES ('Python', 'Zkušený Python Developer', 1),
       ('Java', 'Zkušený Java Developer', 2),
       ('HTML', 'Profesionální zkušenost', 1),
       ('JavaScript', 'Zkušený JavaScript Developer', 3),
       ('JavaScript', 'Průměrná zkušenost', 1),
       ('Spring Boot', 'Zkušený Web Developer', 2),
       ('CSS', 'Pokročilá zkušenost', 1);

INSERT INTO project (title, description, owner_id)
VALUES ('FaceNote', 'Projekt, který slouží jako sociální síť pro tvůrce blogů.', 1),
       ('InstaPound', 'Aplikace zaměřená na sdílení pokroků při nabírání svalů.', 2),
       ('LinkedOut', 'Myšlenka tohoto projektu vznikla z nápadu vytvoření místa pro seznamování cestovatelů.', 3),
       ('GinHub', 'Ráj pro nadšence z alkoholu', 2),
       ('Azuz', 'Aplikace zaměřená na sdílení svých poznatek o světě.', 3),
       ('LenofoGit', 'Nejlepším místem pro sbírání nových dovedností v oblasti matematiky.', 1);

INSERT INTO tag (name)
VALUES ('Python'),
       ('Java'),
       ('JavaScript'),
       ('C#'),
       ('HTML'),
       ('CSS'),
       ('C++'),
       ('Spring Boot'),
       ('Django');

INSERT INTO project_tag (project_id, tag_id)
VALUES (1, 1),
       (1, 9),
       (2, 3),
       (3, 2),
       (3, 8),
       (3, 3),
       (4, 7),
       (5, 5),
       (5, 6),
       (6, 4),
       (1, 3),
       (2, 1);

INSERT INTO comment (value, project_id, owner_id)
VALUES ('Toto je moc hezký projekt', 1, 2),
       ('To je paráda', 1, 3),
       ('Příště bych se více zaměřil na cílovou skupinu', 2, 1),
       ('Jsem fanoušek tohoto nápadu', 3, 1),
       ('Moc se mi to nelíbí', 3, 2),
       ('Tak jsem si to rozmyslel', 3, 2),
       ('Moc pěkný', 4, 1),
       ('Jde to', 4, 3),
       ('Myšlenka ujde, provedení nic moc', 5, 1),
       ('Nešlo by spíše udělat, aby to fungovalo lépe?', 6, 2),
       ('Toto se moc nepovedlo', 6, 3);

-- je potřeba si v /devconnect/config/WebConfig vydefinovat cestu k médiím. Média by dle toho, co jsem četl, neměly být ve složce s projektem, ale někde na lokálním zařízení. Zkusil jsem je mít ve složce s projektem a zlobí to, načítají se pomalu, až když o nich ví IDE atd.
-- pro test příkládám obrázky sem do složky db/images/. Prosím přenastavte si cestu k médiím v WebConfig a přesuňte tam obrázky.
INSERT INTO image (file_path, project_id)
VALUES ('image1.jpg', 1),
       ('image2.jpg', 1),
       ('image3.jpg', 2),
       ('image4.jpg', 3),
       ('image5.jpg', 3),
       ('image6.jpg', 3),
       ('image7.jpg', 4),
       ('image8.jpg', 5),
       ('image9.jpg', 5),
       ('image10.jpg', 3);


