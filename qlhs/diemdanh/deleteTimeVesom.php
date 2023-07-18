<?php  
	include "../connect.php";

	$iduser = $_POST['iduser'];
	$idsetupdd = $_POST['idsetupdd'];


	$query1 = "DELETE FROM setupvesom WHERE iduser = '$iduser' AND idsetupdd = '$idsetupdd'";
	$data1 = mysqli_query($connect, $query1);

	if ($data1) {
		$arr = [
			'success' => true,
			'message' => "thanh cong"
			];
	}else{
		$arr = [
			'success' => false,
			'message' => "khong thanh cong"
		];
	}


	print_r(json_encode($arr));
?>