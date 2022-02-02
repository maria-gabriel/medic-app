<?php 
include ('functions.php');

$Id_Cita=$_GET['Id_Cita'];
    
	ejecutarSQLCommand("UPDATE Citas set Estado = 'Finalizada' where Id_Cita='$Id_Cita'");    

 ?>