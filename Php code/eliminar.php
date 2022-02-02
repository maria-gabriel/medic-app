<?php 
include ('functions.php');

$Id=$_GET['Id'];
    
	ejecutarSQLCommand("DELETE FROM Pacientes WHERE Id_Paciente='$Id'");    

 ?>