create database Medic;
use Medic;

CREATE TABLE `Medicos` (
  `Id_Medico` int auto_increment,
  `Nombre` varchar(40) NOT NULL,
  `ApellidoP` varchar(40) NOT NULL,
  `ApellidoM` varchar(40) NOT NULL,
  `Correo` varchar(60) NOT NULL,
  `Telefono` long NOT NULL,
  `Cedula` varchar(10) NOT NULL,
  `Contra` varchar(30) NOT NULL,
  `TotalCitas` int,
  unique(`Correo`),
  PRIMARY KEY (`Id_Medico`)
) ENGINE=InnoDB;

CREATE TABLE `Pacientes` (
  `Id_Paciente` int auto_increment,
  `Nombre` varchar(40) NOT NULL,
  `ApellidoP` varchar(40) NOT NULL,
  `ApellidoM` varchar(40) NOT NULL,
  `Correo` varchar(60) NOT NULL,
  `Edad` int NOT NULL,
  `Telefono` long NOT NULL,
  `Contra` varchar(30) NOT NULL,
  `NumCitas` int,
  unique(`Correo`),
  PRIMARY KEY (`Id_Paciente`)
) ENGINE=InnoDB;

CREATE TABLE `Citas` (
  `Id_Cita` int auto_increment,
  `Sintomas` varchar(100) NOT NULL,
  `Hora` varchar(10) NOT NULL,
  `Fecha` date NOT NULL,
  `Latitud` varchar(30) NOT NULL,
  `Longitud` varchar(30) NOT NULL,
  `Referencias` varchar(100) NOT NULL,
  `Estado` varchar(20) NOT NULL,
  PRIMARY KEY (`Id_Cita`)
) ENGINE=InnoDB;

CREATE TABLE `Fichas` (
  `Id_Ficha` int auto_increment,
  `Curp` varchar(20) NOT NULL,
  `FechaNac` date NOT NULL,
  `TipoSan` varchar(3) NOT NULL,
  `Genero` varchar(20) NOT NULL,
  `EstadoCiv` varchar(30) NOT NULL,
  `Nacionalidad` varchar(30) NOT NULL,
  `Familiar` varchar(40),
  `Parent` varchar(20),
  `Telefon` long,
  PRIMARY KEY (`Id_Ficha`)
) ENGINE=InnoDB;

CREATE TABLE `Observaciones` (
  `Id_Observacion` int auto_increment,
  `Diagnostico` varchar(50) NOT NULL,
  `Presion` varchar(10),
  `Respiracion` varchar(10),
  `Pulso` varchar(10),
  `Temperatura` varchar(10),
  `Motivo` varchar(30) NOT NULL,
  `Prescripcion` varchar(50) NOT NULL,
  PRIMARY KEY (`Id_Observacion`)
) ENGINE=InnoDB;


CREATE TABLE `Paciente_Ficha` (
  `Id_Paciente` int NOT NULL,
  `Id_Ficha` int NOT NULL,
  FOREIGN KEY (Id_Paciente) REFERENCES Pacientes (Id_Paciente) ON DELETE CASCADE,
  FOREIGN KEY (Id_Ficha) REFERENCES Fichas (Id_ficha) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `Paciente_Observacion` (
  `Id_Paciente` int NOT NULL,
  `Id_Medico` int NOT NULL,
  `Id_Observacion` int NOT NULL,
  FOREIGN KEY (Id_Paciente) REFERENCES Pacientes (Id_Paciente) ON DELETE CASCADE,
  FOREIGN KEY (Id_Observacion) REFERENCES observaciones (Id_Observacion) ON DELETE CASCADE,
  FOREIGN KEY (Id_Medico) REFERENCES Medicos (Id_Medico) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `Cita_Medico_Paciente` (
  `Id_Paciente` int NOT NULL,
  `Id_Medico` int NOT NULL,
  `Id_Cita` int NOT NULL,
  FOREIGN KEY (Id_Paciente) REFERENCES Pacientes (Id_Paciente) ON DELETE CASCADE,
  FOREIGN KEY (Id_Cita) REFERENCES Citas (Id_Cita) ON DELETE CASCADE,
  FOREIGN KEY (Id_Medico) REFERENCES Medicos (Id_Medico) ON DELETE CASCADE
) ENGINE=InnoDB;