<?php 
	include "../connect.php";

	$iduser = $_POST['iduser'];
	$idsetupdd = $_POST['idsetupdd'];
	$query =  "SELECT * FROM thongkediemdanh WHERE iduser= '$iduser' AND idsetupdd ='$idsetupdd'";
	$data = mysqli_query($connect,$query);
	$row = mysqli_fetch_assoc($data);
	print_r(json_encode($row));


?>