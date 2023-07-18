<?php 
	include "../connect.php";
	
	$iduser = $_POST['iduser'];
	$idngaydd = $_POST['idngaydd'];
	$idsetupdd = $_POST['idsetupdd'];
	$query = "SELECT * FROM diemdanh WHERE  iduser= '$iduser' AND idngaydd= '$idngaydd' AND idsetupdd= '$idsetupdd' AND (trangthai='Đã điểm danh' OR trangthai='Đi học trễ' OR trangthai = 'Đã ra về' OR trangthai = 'Về sớm' )";
	

	$data = mysqli_query($connect, $query);
	$result = array();
	
	while ($row = mysqli_fetch_assoc($data)) {
		$result[] = ($row);
		
	}
	
	print_r(json_encode($result));
?>