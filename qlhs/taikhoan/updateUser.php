<?php  
include "../connect.php";

	$iduser = $_POST['iduser'];
	$email = $_POST['email'];
	$username = $_POST['username'];
	$mobile = $_POST['mobile'];
	$target_dir = "imageUser/";

	$target_file = $target_dir . basename($_FILES["avatar"]["name"]);
	$imageFileType = strtolower(pathinfo($target_file, PATHINFO_EXTENSION));


	// Kiểm tra xem email đã tồn tại trong cơ sở dữ liệu hay chưa
	$query3 = "SELECT * FROM user WHERE id = '$iduser'";
	$data3 = mysqli_query($connect, $query3);
	$row3 = mysqli_fetch_assoc($data3);
	$email3 = $row3["email"];

		// Kiểm tra xem email đã tồn tại trong cơ sở dữ liệu hay chưa
	$query4 = "SELECT * FROM user WHERE  email = '$email'";
	$data4 = mysqli_query($connect, $query4);
	$numrow3 = mysqli_num_rows($data4);

	function updateNoAvatarUser($email,$username,$mobile,$iduser,$connect){
				$query1 = "UPDATE user SET email='$email',username='$username',mobile='$mobile' WHERE id = '$iduser'";
				$data1 = mysqli_query($connect,$query1);
				if ($data1) {
					$arr = [
						'success' => true,'message' => "thanh cong"
					];
				}else{
					$arr = [
						'success' => false, 'message' => "khong thanh cong"
					];
				}mysqli_close($connect);
				return $arr;
	}

	function updateAvatarUser($email,$username,$mobile,$avatar,$iduser,$connect){
			    $query1 = "UPDATE user SET email='$email',username='$username',mobile='$mobile',avatar='$avatar' WHERE id = '$iduser'";
				$data1 = mysqli_query($connect,$query1);
				if ($data1) {
					$arr = [
						'success' => true,'message' => "thanh cong"
					];
				}else{
					$arr = [
						'success' => false, 'message' => "khong thanh cong"
					];
				}mysqli_close($connect);
				return $arr;
	}
	

	if($email3 == $email) {

		if(basename($_FILES["avatar"]["name"]) == "a" ){
			$arr = updateNoAvatarUser($email,$username,$mobile,$iduser,$connect);
		}
		
		// Kiểm tra nếu là tệp ảnh hợp lệ
		else if ($imageFileType == "jpg" || $imageFileType == "jpeg" || $imageFileType == "png") {
			if (move_uploaded_file($_FILES["avatar"]["tmp_name"], $target_file)) {			
				$avatar = basename($_FILES["avatar"]["name"]);
				$arr = updateAvatarUser($email,$username,$mobile,$avatar,$iduser,$connect);
			}else {
		        $arr = [
					'success' => false,'message' => "Lỗi gửi hình ảnh."
				];
		    }
		}else {
		    $arr = [
				'success' => false,'message' => "Loại tệp hình ảnh không hợp lệ"
			];
		}

	}

	else {
		if ($numrow3 > 0) {
		    $arr = [
				'success' => false,
				'message' => "Email đã tồn tại. Vui lòng sử dụng email khác"
			];


		} else {

		if(basename($_FILES["avatar"]["name"]) == "a" ){
			$arr = updateNoAvatarUser($email,$username,$mobile,$iduser,$connect);
		}
		
		// Kiểm tra nếu là tệp ảnh hợp lệ
		else if ($imageFileType == "jpg" || $imageFileType == "jpeg" || $imageFileType == "png") {
				if (move_uploaded_file($_FILES["avatar"]["tmp_name"], $target_file)) {
					$avatar = basename($_FILES["avatar"]["name"]);
				    $arr = updateAvatarUser($email,$username,$mobile,$avatar,$iduser,$connect);
				}else {
			        $arr = [
						'success' => false,'message' => "Lỗi gửi hình ảnh."
					];
			    }
			}else {
			    $arr = [
					'success' => false,'message' => "Loại tệp hình ảnh không hợp lệ"
				];
			}
		}

	}


	print_r(json_encode($arr));
	


?>

