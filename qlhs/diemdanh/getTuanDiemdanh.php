<?php 
	include "../connect.php";
	$iduser = $_POST['iduser'];
	$week = $_POST['week'];
	$year = $_POST['year'];

	$query = "SELECT *FROM tuandiemdanh WHERE iduser='$iduser' AND week='$week' AND year='$year'";
	$data = mysqli_query($connect,$query);
	$row = mysqli_fetch_assoc($data);
	
	print_r(json_encode($row));

?>	