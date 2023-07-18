<?php  
	include "../connect.php";

	$idngaydd = $_POST['idngaydd'];
	$idsetupdd = $_POST['idsetupdd'];

	$query = "DELETE FROM ngaydiemdanh WHERE idngaydd = '$idngaydd'";
	$data = mysqli_query($connect, $query);

	if ($data) {
		$arr = [
			'success' => true,
			'message' => "thanh cong"
		];
		$query1 = "DELETE FROM diemdanh WHERE idsetupdd = '$idsetupdd'";
		$data1 = mysqli_query($connect, $query1);
	}else{
		$arr = [
			'success' => false,
			'message' => "khong thanh cong"
		];
	}
	
	print_r(json_encode($arr));
?>