INSERT INTO language (name, display_name, file_extension, web_compiler_url) VALUES 
('java', 'Java', 'java', 'https://www.onlinegdb.com/online_java_compiler'),
('csharp', 'C#', 'cs', 'https://www.onlinegdb.com/online_csharp_compiler'),
('python', 'Python', 'py', 'https://www.onlinegdb.com/online_python_compiler')
ON CONFLICT (name) DO NOTHING;

INSERT INTO converter (name, avaliable, lang_in_id, lang_out_id, file_name, package) VALUES
('cstojava', true, (SELECT id FROM language WHERE name = 'csharp'), (SELECT id FROM language WHERE name = 'java'), 'cstojava', 'cstojava')
ON CONFLICT (name) DO NOTHING;