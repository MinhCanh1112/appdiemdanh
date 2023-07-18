<?php
	include "../connect.php";


	$id = $_POST['id'];
	$lydo  = $_POST['lydo'];


	//check data
	$query = "SELECT * FROM diemdanh WHERE id ='$id' ";
	$data = mysqli_query($connect,$query);
	$numrow = mysqli_num_rows($data);


	if ($numrow == 1 ){
		$query2 = "UPDATE  diemdanh SET lydo = '$lydo' WHERE id ='$id'";
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
		print_r(json_encode($arr));
	}
	
?>