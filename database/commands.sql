-- Active: 1710413786764@@127.0.0.1@5432@postgres
CREATE TABLE IF NOT EXISTS language ( 
    id SERIAL,
    name VARCHAR(255) UNIQUE NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    file_extension VARCHAR(63) NOT NULL,
    web_compiler_url VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS converter ( 
    id SERIAL,
    name VARCHAR(255) UNIQUE NOT NULL,
    avaliable BOOLEAN NOT NULL,
    lang_in_id INT NOT NULL,
    lang_out_id INT NOT NULL,
    file_name VARCHAR(255),
    package VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (lang_in_id) REFERENCES language(id),
    FOREIGN KEY (lang_out_id) REFERENCES language(id)
);

SELECT * FROM language;
SELECT * FROM converter;

SELECT id, name, lang_in_name, lang_out_name, file_name, package FROM converter 
LEFT JOIN (SELECT id as lang_in_id, name as lang_in_name FROM language) as n1 
ON converter.lang_in_id = n1.lang_in_id
LEFT JOIN (SELECT id as lang_out_id, name as lang_out_name FROM language) as n2
ON converter.lang_out_id = n2.lang_out_id;

SELECT name FROM language WHERE file_extension = 'cs';
SELECT * FROM converter WHERE lang_in_id = 2 AND lang_out_id = 1;

DROP TABLE converter;
DROP TABLE language;