<?php 
	include "../connect.php";
	
	$iduser = $_POST['iduser'];
	
	$query = "SELECT * FROM hocsinh WHERE iduser = '$iduser'" ;
	$data = mysqli_query($connect, $query);
	$result = array();
	
	while ($row = mysqli_fetch_assoc($data)) {
		$result[] = ($row);
		
	}
	
	print_r(json_encode($result));
?>