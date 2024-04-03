CREATE TABLE IF NOT EXISTS language ( 
    id SERIAL,
    name VARCHAR(255) UNIQUE NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    file_extension VARCHAR(63) NOT NULL,
    web_compiler_url VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS converter ( 
    id SERIAL,
    name VARCHAR(255) NOT NULL,
    avaliable BOOLEAN NOT NULL,
    lang_in_id INT NOT NULL,
    lang_out_id INT NOT NULL,
    file_name VARCHAR(255),
    package VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (lang_in_id) REFERENCES language(id),
    FOREIGN KEY (lang_out_id) REFERENCES language(id)
);