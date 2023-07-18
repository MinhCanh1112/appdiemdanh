<?php  
	include "../connect.php";
	$iduser = $_POST['iduser'];
	$name = $_POST['name'];
	$uid = $_POST['uid'];
	$mshs = $_POST['mshs'];
	$gender = $_POST['gender'];
	$mobile = $_POST['mobile'];
	$week = $_POST['week'];
	$year = $_POST['year'];
	$target_dir = "image/";

	$target_file = $target_dir . basename($_FILES["avatar"]["name"]);
	$imageFileType = strtolower(pathinfo($target_file, PATHINFO_EXTENSION));


		//check data
	$query = "SELECT * FROM hocsinh WHERE uid = '$uid' ";
	$data = mysqli_query($connect,$query);
	$numrow = mysqli_num_rows($data);
	$query1 = "SELECT * FROM hocsinh WHERE  mshs = '$mshs'";
	$data1 = mysqli_query($connect,$query1);
	$numrow1 = mysqli_num_rows($data1);


	if ($numrow > 0 ){
		$arr = [
			'success' => false,
			'message' => "UID đã tồn tại"
		];
	}

	else if ($numrow1 > 0 ){
		$arr = [
			'success' => false,
			'message' => "Mã số học sinh đã tồn tại"
		];
	}else {

		// Kiểm tra nếu là tệp ảnh hợp lệ
		if ($imageFileType == "jpg" || $imageFileType == "jpeg" || $imageFileType == "png") {
			if (move_uploaded_file($_FILES["avatar"]["tmp_name"], $target_file)) {
				//insert
				$avatar = basename($_FILES["avatar"]["name"]);
				$query2 = "INSERT INTO hocsinh (iduser ,name, uid,mshs, gender, mobile,avatar) 
				VALUES ('$iduser','$name','$uid','$mshs','$gender','$mobile','$avatar')";
				$data2 = mysqli_query($connect, $query2);

				if ($data2) {
					$arr = [
						'success' => true,
						'message' => "thanh cong"
					];

					$id = mysqli_insert_id($connect);

					$query5 = "SELECT * FROM thongketuan WHERE iduser = '$iduser' AND week = '$week' AND year = '$year' ";
					$data5 = mysqli_query($connect,$query5);
					$row5 = mysqli_num_rows($data5);
					if($row5 > 0){
						$query6 = "INSERT INTO thongketuan(iduser,week,year,idhs,name,uid,mshs,avatar)
						VALUE ('$iduser','$week','$year','$id','$name','$uid','$mshs','$avatar')";
						mysqli_query($connect,$query6);
					}

					// Sử dụng COUNT() để đếm số lượng bản ghi trong bảng
					$query3 = 'SELECT COUNT(*) AS total FROM hocsinh WHERE  `iduser`= "'.$iduser.'" ';
					$data3 = mysqli_query($connect, $query3);
					// Lấy kết quả trả về
					$row3 = mysqli_fetch_assoc($data3);
					$total = $row3['total'];
					$query4 = 'UPDATE `lophoc` SET `siso` ="'.$total.'" WHERE iduser = "'.$iduser.'"';
					mysqli_query($connect, $query4);
				}else{
					$arr = [
						'success' => false,
						'message' => "khong thanh cong"
					];
				}
		        mysqli_close($connect);

			}else {
		        $arr = [
					'success' => false,
					'message' => "Lỗi gửi hình ảnh."
				];
		    }

		}else {


		    $arr = [
				'success' => false,
				'message' => "Loại tệp hình ảnh không hợp lệ"
			];
		}
	}

	print_r(json_encode($arr));
	
	
?>