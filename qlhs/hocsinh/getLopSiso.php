<?php 
	include "../connect.php";

	$iduser = $_POST['iduser'];
	$query =  'SELECT * FROM lophoc WHERE iduser= "'.$iduser.'"';
	$data = mysqli_query($connect,$query);
	$row = mysqli_fetch_assoc($data);
		
	print_r(json_encode($row));


?>