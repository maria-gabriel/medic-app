<?php 
include ('functions.php');

$Id=$_GET['Id'];
    
	ejecutarSQLCommand("DELETE FROM Fichas WHERE Id_Ficha='$Id'");    

 ?>