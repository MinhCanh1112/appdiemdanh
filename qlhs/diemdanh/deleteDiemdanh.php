<?php  
	include "../connect.php";
	
	$idsetupdd = $_POST['idsetupdd'];
	$query = "DELETE FROM diemdanh WHERE idsetupdd = '$idsetupdd'";
	
	$data = mysqli_query($connect, $query);


	if ($data) {
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

