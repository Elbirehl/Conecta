CREATE DATABASE IF NOT EXISTS examendb;
USE examendb;

CREATE TABLE UnidadDidactica (
    id INT AUTO_INCREMENT PRIMARY KEY,
    acronimo VARCHAR(50) NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    evaluacion TEXT,
    descripcion TEXT
);

CREATE TABLE Enunciado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion TEXT NOT NULL,
    nivel_dificultad ENUM('baja', 'media', 'alta') NOT NULL,
    disponible BOOLEAN NOT NULL DEFAULT TRUE,
    ruta VARCHAR(50)
);

CREATE TABLE ConvocatoriaExamen (
    convocatoria VARCHAR(50) NOT NULL PRIMARY KEY,
    descripcion TEXT,
    fecha DATE NOT NULL,
    curso VARCHAR(50) NOT NULL,
    enunciado_id INT NOT NULL,
    FOREIGN KEY (enunciado_id) REFERENCES Enunciado(id) ON DELETE CASCADE
);

CREATE TABLE UD_Enunciado (
UD_id INT,
enunciado_id INT,
FOREIGN KEY (enunciado_id) REFERENCES Enunciado(id) ON DELETE CASCADE,
FOREIGN KEY (UD_id) REFERENCES UnidadDidactica(id) ON DELETE CASCADE,
PRIMARY KEY (UD_id, enunciado_id)
);

INSERT INTO UnidadDidactica (acronimo, titulo, evaluacion, descripcion) 
VALUES
('UD01', 'Introducción a SQL', 'Primera Evaluación', 'Esta unidad cubre los fundamentos del lenguaje SQL, incluyendo consultas básicas y avanzadas.'),
('UD02', 'Modelado de datos', 'Primera evaluación', 'En esta unidad se exploran los conceptos de modelado de datos, incluyendo diagramas entidad-relación y normalización.'),
('UD03', 'Administración de Bases de Datos', 'Segunda evaluación', 'Esta unidad se enfoca en la administración y mantenimiento de bases de datos, incluyendo backups, recuperación y optimización.'),
('UD04', 'Desarrollo de Aplicaciones con SQL', 'Segunda evaluación', 'Se cubren técnicas de desarrollo de aplicaciones que interactúan con bases de datos SQL, incluyendo el uso de frameworks y APIs.');

INSERT INTO Enunciado (descripcion, nivel_dificultad, disponible, ruta)
VALUES
('Crea una consulta SQL que recupere todos los registros de una tabla de clientes ordenados por nombre.', 'baja', TRUE, 'enunciados/consulta_basica.sql'),
('Diseña un diagrama entidad-relación para un sistema de gestión de biblioteca.', 'media', TRUE, 'enunciados/diagrama_biblioteca.er'),
('Desarrolla un script SQL para implementar un sistema de control de acceso basado en roles.', 'alta', FALSE, 'enunciados/control_acceso.sql'),
('Optimiza una consulta SQL para mejorar su rendimiento en una base de datos de gran tamaño.', 'alta', TRUE, 'enunciados/optimizar_consulta.sql');

INSERT INTO ConvocatoriaExamen (convocatoria, descripcion, fecha, curso, enunciado_id)
VALUES
('Primera Convocatoria 2024', 'Examen de introducción a SQL para evaluar conocimientos básicos.', '2024-03-15', 'Curso de SQL Básico', 1),
('Segunda Convocatoria 2024', 'Evaluación sobre modelado de datos con enfoque en diagramas ER.', '2024-06-10', 'Curso de Modelado de Datos', 2),
('Convocatoria Extraordinaria 2024', 'Examen práctico sobre administración de bases de datos.', '2024-09-20', 'Curso de Administración de Bases de Datos', 3),
('Primera Convocatoria Avanzada 2024', 'Evaluación de técnicas avanzadas en desarrollo de aplicaciones SQL.', '2024-11-05', 'Curso de Desarrollo Avanzado con SQL', 4);

INSERT INTO UD_Enunciado (UD_id, enunciado_id) VALUES
(1, 1),  
(1, 4), 
(2, 2),  
(3, 3), 
(4, 4); 
