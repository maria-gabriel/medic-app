<?php
include ('functions.php');
$json=array();
mysqli_set_charset($connect, "utf8");

	$queri ="SELECT COUNT(*) AS Motivo1 FROM `observaciones` WHERE Motivo='Enfermedad aguda'";
	$resultado = mysqli_query($connect,$queri);

	$queri2 ="SELECT COUNT(*) AS Motivo2 FROM `observaciones` WHERE Motivo='Enfermedad crónica'";
	$resultado2 = mysqli_query($connect,$queri2);

	$queri3 ="SELECT COUNT(*) AS Motivo3 FROM `observaciones` WHERE Motivo='Enfermedad mental'";
	$resultado3 = mysqli_query($connect,$queri3);

	$queri4 ="SELECT COUNT(*) AS Motivo4 FROM `observaciones` WHERE Motivo='Enfermedad inmunológica'";
	$resultado4 = mysqli_query($connect,$queri4);

	$queri5 ="SELECT COUNT(*) AS Motivo5 FROM `observaciones` WHERE Motivo='Enfermedad endocrina'";
	$resultado5 = mysqli_query($connect,$queri5);

	$queri6 ="SELECT COUNT(*) AS Motivo6 FROM `observaciones` WHERE Motivo='Enfermedad neurológica'";
	$resultado6 = mysqli_query($connect,$queri6);

	$queri7 ="SELECT COUNT(*) AS Motivo7 FROM `observaciones` WHERE Motivo='Enfermedad visual'";
	$resultado7 = mysqli_query($connect,$queri7);

	$queri8 ="SELECT COUNT(*) AS Motivo8 FROM `observaciones` WHERE Motivo='Enfermedad auditiva'";
	$resultado8 = mysqli_query($connect,$queri8);

	$queri9 ="SELECT COUNT(*) AS Motivo9 FROM `observaciones` WHERE Motivo='Enfermedad respiratoria'";
	$resultado9 = mysqli_query($connect,$queri9);

	$queri10 ="SELECT COUNT(*) AS Motivo10 FROM `observaciones` WHERE Motivo='Enfermedad cardiovascular'";
	$resultado10 = mysqli_query($connect,$queri10);

	$queri11 ="SELECT COUNT(*) AS Motivo11 FROM `observaciones` WHERE Motivo='Enfermedad digestiva'";
	$resultado11 = mysqli_query($connect,$queri11);

	$queri12 ="SELECT COUNT(*) AS Motivo12 FROM `observaciones` WHERE Motivo='Enfermedad dermatológica'";
	$resultado12 = mysqli_query($connect,$queri12);

	$queri13 ="SELECT COUNT(*) AS Motivo13 FROM `observaciones` WHERE Motivo='Enfermedad locomotora'";
	$resultado13 = mysqli_query($connect,$queri13);

	$queri14 ="SELECT COUNT(*) AS Motivo14 FROM `observaciones` WHERE Motivo='Enfermedad genitourinaria'";
	$resultado14 = mysqli_query($connect,$queri14);

	$queri15 ="SELECT COUNT(*) AS Motivo15 FROM `observaciones` WHERE Motivo='Enfermedad infecciosa'";
	$resultado15 = mysqli_query($connect,$queri15);

	$queri16 ="SELECT COUNT(*) AS Motivo16 FROM `observaciones` WHERE Motivo='Enfermedad traumática'";
	$resultado16 = mysqli_query($connect,$queri16);

	$queri17 ="SELECT COUNT(*) AS Motivo17 FROM `observaciones` WHERE Motivo='Enfermedad por causa externa'";
	$resultado17 = mysqli_query($connect,$queri17);

	$queri18 ="SELECT COUNT(*) AS Motivo18 FROM `observaciones` WHERE Motivo='Otro'";
	$resultado18 = mysqli_query($connect,$queri18);

	$reg1=mysqli_fetch_array($resultado);
	$reg2=mysqli_fetch_array($resultado2);
	$reg3=mysqli_fetch_array($resultado3);
	$reg4=mysqli_fetch_array($resultado4);
	$reg5=mysqli_fetch_array($resultado5);
	$reg6=mysqli_fetch_array($resultado6);
	$reg7=mysqli_fetch_array($resultado7);
	$reg8=mysqli_fetch_array($resultado8);
	$reg9=mysqli_fetch_array($resultado9);
	$reg10=mysqli_fetch_array($resultado10);
	$reg11=mysqli_fetch_array($resultado11);
	$reg12=mysqli_fetch_array($resultado12);
	$reg13=mysqli_fetch_array($resultado13);
	$reg14=mysqli_fetch_array($resultado14);
	$reg15=mysqli_fetch_array($resultado15);
	$reg16=mysqli_fetch_array($resultado16);
	$reg17=mysqli_fetch_array($resultado17);
	$reg18=mysqli_fetch_array($resultado18);

		$re1 = array_merge($reg1, $reg2);
		$re2 = array_merge($re1,$reg3);
		$re3 = array_merge($re2,$reg4);
		$re4 = array_merge($re3,$reg5);
		$re5 = array_merge($re4,$reg6);
		$re6 = array_merge($re5,$reg7);
		$re7 = array_merge($re6,$reg8);
		$re8 = array_merge($re7,$reg9);
		$re9 = array_merge($re8,$reg10);
		$re10 = array_merge($re9,$reg11);
		$re11 = array_merge($re10,$reg12);
		$re12 = array_merge($re11,$reg13);
		$re13 = array_merge($re12,$reg14);
		$re14 = array_merge($re13,$reg15);
		$re15 = array_merge($re14,$reg16);
		$re16 = array_merge($re15,$reg17);
		$re17 = array_merge($re16,$reg18);
		
		$json['datos'][]=$re17;
		
			
			echo json_encode($json,JSON_UNESCAPED_UNICODE);
 ?>
