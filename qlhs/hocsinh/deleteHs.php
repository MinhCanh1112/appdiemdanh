<?php  
	include "../connect.php";

	$id = $_POST['id'];
	$iduser = $_POST['iduser'];
	$query = 'DELETE FROM `hocsinh` WHERE `id` = "'.$id.'" AND `iduser`="'.$iduser.'" ';
	
	$data = mysqli_query($connect, $query);

	if ($data) {
		$arr = [
			'success' => true,
			'message' => "thanh cong"
		];

		// Sử dụng COUNT() để đếm số lượng bản ghi trong bảng
		$query1 = 'SELECT COUNT(*) AS total FROM hocsinh WHERE  `iduser`= "'.$iduser.'" ';
		$data1 = mysqli_query($connect, $query1);
		// Lấy kết quả trả về
		$row = mysqli_fetch_assoc($data1);
		$total = $row['total'];
		$query2 = 'UPDATE `lophoc` SET `siso` ="'.$total.'" WHERE iduser = "'.$iduser.'"';
		mysqli_query($connect, $query2);

	}else{
		$arr = [
			'success' => false,
			'message' => "khong thanh cong"
		];
	}
	
	print_r(json_encode($arr));
?>