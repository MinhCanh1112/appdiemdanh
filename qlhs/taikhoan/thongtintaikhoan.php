<?php 
include "../connect.php";

	$iduser = $_POST['iduser'];
	$query = "SELECT * FROM user WHERE id = '$iduser'";
	
	$data = mysqli_query($connect, $query);
	$row = mysqli_fetch_assoc($data);
	
	print_r(json_encode($row));
?>


