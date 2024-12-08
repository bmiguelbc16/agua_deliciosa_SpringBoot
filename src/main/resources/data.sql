-- Limpiar datos existentes
DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM roles;

-- Insertar roles
INSERT INTO roles (name, description) VALUES 
('ROLE_ADMIN', 'Administrador del sistema'),
('ROLE_GERENTE', 'Gerente'),
('ROLE_VENDEDOR', 'Vendedor'),
('ROLE_REPARTIDOR', 'Repartidor'),
('ROLE_CLIENTE', 'Cliente');

-- Insertar usuario admin por defecto (password: admin123)
INSERT INTO users (username, email, password, status) VALUES 
('admin', 'admin@aguadeliciosa.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', true);

-- Asignar rol admin
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id 
FROM users u 
CROSS JOIN roles r 
WHERE u.username = 'admin' 
AND r.name = 'ROLE_ADMIN';