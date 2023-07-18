<?php 
include "../connect.php";
	
	$iduser = $_POST['iduser'];
	$date = $_POST['date'];
	$query = 'SELECT * FROM `ngaydiemdanh` WHERE `iduser` = "'.$iduser.'" AND `date` = "'.$date.'"';
	
	$data = mysqli_query($connect, $query);
	$result = array();
	
	while ($row = mysqli_fetch_assoc($data)) {
		$result[] = ($row);
		
	}
	
	print_r(json_encode($result));
?>