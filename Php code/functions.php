<?php 
header( 'Content-Type: text/html;charset=utf-8' );
//conexión con la base de datos
$connect = mysqli_connect("192.168.100.43", "root", "cisco1234", "medic_app");
function ejecutarSQLCommand($commando){//esto es una funcion para ejecutar un comando
 
$mysqli = new mysqli("192.168.100.43", "root", "cisco1234", "medic_app");

  /* ve la coneccion */
  if ($mysqli->connect_errno) {
      printf("Connect failed: %s\n", $mysqli->connect_error);
      exit();
  }
  //ejecuta el Query
  if ( $mysqli->multi_query($commando)) {
      if ($resultset = $mysqli->store_result()) {   
        while ($row = $resultset->fetch_array(MYSQLI_BOTH)) {}
        $resultset->free();
      }  
    }
    $mysqli->close();
}


function getSQLResultSet($commando){//funcion para ejecutar un comando y devolver algun valor

  $mysqli = new mysqli("192.168.100.43", "root", "cisco1234", "medic_app");
  /* check connection */
  if ($mysqli->connect_errno) {
    printf("Connect failed: %s\n", $mysqli->connect_error);
    exit();
  }

  if ( $mysqli->multi_query($commando)) {
    return $mysqli->store_result();      //Devuelve el valor de la consulta
  }
  $mysqli->close();
}

?>
