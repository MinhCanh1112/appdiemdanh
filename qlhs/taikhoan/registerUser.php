<?php  
include "../connect.php";

	$email = $_POST['email'];
	$username = $_POST['username'];
	$mobile = $_POST['mobile'];
	$lop = $_POST['lop'];
	$pass = $_POST['pass'];
	$target_dir = "imageUser/";

	$target_file = $target_dir . basename($_FILES["avatar"]["name"]);
	$imageFileType = strtolower(pathinfo($target_file, PATHINFO_EXTENSION));

	// Kiểm tra xem email đã tồn tại trong cơ sở dữ liệu hay chưa
	$query = "SELECT * FROM user WHERE email = '$email'";
	$data = mysqli_query($connect, $query);

	if (mysqli_num_rows($data) > 0) {
	    $response["success"] = false;
	    $response["message"] = "Email đã tồn tại. Vui lòng sử dụng email khác";

	} else {
		// Kiểm tra nếu là tệp ảnh hợp lệ
		if ($imageFileType == "jpg" || $imageFileType == "jpeg" || $imageFileType == "png") {
			if (move_uploaded_file($_FILES["avatar"]["tmp_name"], $target_file)) {
				$avatar = basename($_FILES["avatar"]["name"]);
			    // Thực hiện thêm mới người dùng vào cơ sở dữ liệu
			    $query1 = "INSERT INTO user(email, username, mobile,pass,avatar) VALUES ('$email','$username','$mobile','$pass','$avatar')";
				$data1 = mysqli_query($connect,$query1);
			    if ($data1) {
					$response["success"] = true;
					$query2 = 'SELECT * FROM user WHERE email = "'.$email.'"';
					$data2 = mysqli_query($connect,$query2);
					$row2 = mysqli_fetch_assoc($data2);
					$id2 = $row2["id"];

					// Sử dụng COUNT() để đếm số lượng bản ghi trong bảng 
					$query3 = 'SELECT COUNT(*) AS total FROM hocsinh WHERE  `iduser`= "'.$id2.'" ';
					$data3 = mysqli_query($connect, $query3);
					// Lấy kết quả trả về
					$row3 = mysqli_fetch_assoc($data3);
					$total = $row3['total'];

					$query4 = 'INSERT INTO `lophoc`(`iduser` ,`lop`,`siso`) VALUES ("'.$id2.'","'.$lop.'","'.$total.'")';
					mysqli_query($connect, $query4);


				} else {
					$response = array('success' => false, 'message' => 'Đăng ký thất bại.');
				}
			} else {
					$response = array('success' => false, 'message' => 'Đăng ký thất bại.');
			}
		} else {
			$response = array('success' => false, 'message' => 'Đăng ký thất bại.');
		}
	}

	echo json_encode($response);



?>

