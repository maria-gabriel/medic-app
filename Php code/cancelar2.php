<?php 
include ('functions.php');

$Id_Cita=$_GET['Id_Cita'];
    
	ejecutarSQLCommand("UPDATE Citas set Estado = 'Cancelada (doctor)' where Id_Cita='$Id_Cita'");    

 ?>