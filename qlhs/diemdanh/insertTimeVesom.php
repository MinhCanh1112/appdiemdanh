<?php
	include "../connect.php";

	$iduser = $_POST['iduser'];
	$idsetupdd = $_POST['idsetupdd'];
	$timevesom  = $_POST['timevesom'];

	//check data
	$query = "SELECT * FROM setupvesom WHERE iduser ='$iduser'";
	$data = mysqli_query($connect,$query);
	$numrow = mysqli_num_rows($data);


	if ($numrow > 0 ){
		$arr = [
			'success' => false,
			'message' => "Đang thiết lập về sớm"
		];

	}else {
		//insert
		$query2 = "INSERT INTO setupvesom (iduser,idsetupdd,timevesom) VALUES ('$iduser','$idsetupdd','$timevesom')";
		$data2 = mysqli_query($connect, $query2);
		

		if ($data2) {
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
	}
	
	print_r(json_encode($arr));
?>