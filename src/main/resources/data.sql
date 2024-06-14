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

INSERT INTO language (name, display_name, file_extension, web_compiler_url) VALUES 
('java', 'Java', 'java', 'https://www.onlinegdb.com/online_java_compiler'),
('csharp', 'C#', 'cs', 'https://www.onlinegdb.com/online_csharp_compiler'),
('python', 'Python', 'py', 'https://www.onlinegdb.com/online_python_compiler')
ON CONFLICT (name) DO NOTHING;

INSERT INTO converter (name, avaliable, lang_in_id, lang_out_id, file_name, package) VALUES
('cstojava', true, (SELECT id FROM language WHERE name = 'csharp'), (SELECT id FROM language WHERE name = 'java'), 'cstojava', 'cstojava')
ON CONFLICT (name) DO NOTHING;