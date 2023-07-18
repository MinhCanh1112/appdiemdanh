<?php 
	include "../connect.php";
	
	$iduser = $_POST['iduser'];
	$lop = $_POST['lop'];

	// Sử dụng COUNT() để đếm số lượng bản ghi trong bảng "users"
	$query = 'SELECT COUNT(*) AS total FROM hocsinh WHERE  `iduser`= "'.$iduser.'" ';
	$data = mysqli_query($connect, $query);
	// Lấy kết quả trả về
	$row = mysqli_fetch_assoc($data);
	$total = $row['total'];

	// chọn id lophoc để cập nhật tên lớp
	//chọn id của lophoc với điều kiện siso = total và iduser = iduser từ ứng dụng gửi lên
	// $query1 = 'SELECT id FROM `lophoc` WHERE `siso` = "'.$total.'" AND `iduser` = "'.$iduser.'" ';
	// $data1 = mysqli_query($connect, $query1);	
	// $row1 = mysqli_fetch_assoc($data1);
	// $id1 = $row1["id"];



	//check data
	//chọn lop của lophoc với điều kiện id bằng với id vừa check ở trên, siso = total và iduser = iduser từ ứng dụng gửi lên
	$query2 = 'SELECT lop FROM `lophoc` WHERE `siso` = "'.$total.'" AND `iduser` = "'.$iduser.'"';
	$data2 = mysqli_query($connect,$query2);
	$numrow2 = mysqli_num_rows($data2);



	// nếu đã có giá trị vừa check
	if($numrow2 == 1){
		// update lop
		$query3 = 'UPDATE `lophoc` SET `lop`="'.$lop.'" WHERE `iduser`= "'.$iduser.'" ';
		$data3 = mysqli_query($connect, $query3);
		if ($data3) {
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

	// Trả về kết quả dưới dạng JSON
	// echo json_encode(array("total" => $total));
?>