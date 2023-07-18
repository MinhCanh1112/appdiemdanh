<?php  
	include "../connect.php";

	$id = $_POST['id'];


		$query1 = "DELETE FROM setupdiemdanh WHERE id = '$id'";
		$data1 = mysqli_query($connect, $query1);


		if ($data1) {
			$arr = [
				'success' => true,
				'message' => "thanh cong"
			];
			$query2 = "DELETE FROM setupvesom WHERE idsetupdd = '$id'";
			$data2 = mysqli_query($connect, $query2);

		}else{
			$arr = [
				'success' => false,
				'message' => "khong thanh cong"
			];
		}


	print_r(json_encode($arr));
?>