<?php 
	include "../connect.php";
	$iduser = $_POST['iduser'];
	$week = $_POST['week'];
	$year = $_POST['year'];
	$query =  "SELECT * FROM thongketuan WHERE iduser= '$iduser' AND week = '$week' AND year = '$year'";
	$data = mysqli_query($connect,$query);
	$result = array();
	while ($row = mysqli_fetch_assoc($data)) {
		$result[] = ($row);
	}		
	print_r(json_encode($result));
?>