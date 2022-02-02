<?php 
include ('functions.php');

$Id=$_GET['Id'];
    
	ejecutarSQLCommand("DELETE FROM Medicos WHERE Id_Medico='$Id'");    

 ?>