-- Dati iniziali inseriti solo se la tabella TASKS è vuota

INSERT INTO tasks (id, title, description, completed, created_at)
SELECT 1, 'Comprare latte', 'Anche pane', 0, CURRENT_TIMESTAMP
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM tasks WHERE id = 1);

INSERT INTO tasks (id, title, description, completed, created_at)
SELECT 2, 'Studiare Spring Boot', 'Controller e Service', 1, CURRENT_TIMESTAMP
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM tasks WHERE id = 2);

INSERT INTO tasks (id, title, description, completed, created_at)
SELECT 3, 'Fare la spesa', NULL, 0, CURRENT_TIMESTAMP
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM tasks WHERE id = 3);
