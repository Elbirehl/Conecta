DROP DATABASE IF EXISTS examendb;
CREATE DATABASE IF NOT EXISTS examendb;
USE examendb;

CREATE TABLE IF NOT EXISTS UnidadDidactica (
    id INT AUTO_INCREMENT PRIMARY KEY,
    acronimo VARCHAR(50) NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    evaluacion TEXT NOT NULL,
    descripcion TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Enunciado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion TEXT NOT NULL,
    nivel_dificultad ENUM('baja', 'media', 'alta') NOT NULL,
    disponible BOOLEAN NOT NULL DEFAULT TRUE,
    ruta TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS ConvocatoriaExamen (
    convocatoria VARCHAR(50) NOT NULL PRIMARY KEY,
    descripcion TEXT NOT NULL,
    fecha DATE NOT NULL,
    curso VARCHAR(50) NOT NULL,
    enunciado_id INT NOT NULL,
    FOREIGN KEY (enunciado_id) REFERENCES Enunciado(id) ON DELETE CASCADE
);

CREATE TABLE UD_Enunciado (
UD_id INT NOT NULL,
enunciado_id INT NOT NULL,
FOREIGN KEY (enunciado_id) REFERENCES Enunciado(id) ON DELETE CASCADE,
FOREIGN KEY (UD_id) REFERENCES UnidadDidactica(id) ON DELETE CASCADE,
PRIMARY KEY (UD_id, enunciado_id)
);