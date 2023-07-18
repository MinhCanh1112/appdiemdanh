<?php 
	include "../connect.php";

	$id = $_POST['id'];
	$iduser = $_POST['iduser'];
	$name = $_POST['name'];
	$uid = $_POST['uid'];
	$mshs = $_POST['mshs'];
	$gender = $_POST['gender'];
	$mobile = $_POST['mobile'];	
	$week = $_POST['week'];
	$year = $_POST['year'];	
	$target_dir = "image/";

	// Kiểm tra nếu là tệp ảnh hợp lệ
	$target_file = $target_dir . basename($_FILES["avatar"]["name"]);
	$imageFileType = strtolower(pathinfo($target_file, PATHINFO_EXTENSION));

	// Chọn uid và mshs từ bảng học sinh với điều kiện id trên csdl bằng với id từ ứng dụng gửi lên
	$query1 = "SELECT * FROM hocsinh WHERE id = '$id' AND iduser = '$iduser'" ;
	$data1 = mysqli_query($connect, $query1);
	$row1 = mysqli_fetch_assoc($data1);
	$uid1 = $row1["uid"];
	$mshs1 = $row1["mshs"];
	$avatar1 = $row1["avatar"];

	//check data
	$sql_uid = "SELECT * FROM hocsinh WHERE uid = '$uid'";
	$data_uid = mysqli_query($connect,$sql_uid);
	$numrow_uid = mysqli_num_rows($data_uid);

	$sql_mshs = "SELECT * FROM hocsinh WHERE mshs = '$mshs'";
	$data_mshs = mysqli_query($connect,$sql_mshs);
	$numrow_mshs = mysqli_num_rows($data_mshs);

	function updateNoAvatar($iduser,$name,$uid,$mshs,$gender,$mobile,$id,$connect){
		$query2 = "UPDATE hocsinh SET iduser='$iduser', name='$name', uid='$uid', mshs='$mshs', gender='$gender', mobile='$mobile' WHERE id = '$id'";
		$data2 = mysqli_query($connect, $query2);
		if ($data2) {
			$arr = [
				'success' => true,'message' => "thanh cong"
			];
			$query6 = "UPDATE thongketuan SET name='$name',uid='$uid',mshs='$mshs' WHERE idhs='$id'";
			mysqli_query($connect,$query6);
		}else{
			$arr = [
				'success' => false, 'message' => "khong thanh cong"
			];
		}mysqli_close($connect);
		return $arr;
	}

	function updateAvatar($iduser,$name,$uid,$mshs,$gender,$mobile,$avatar,$id,$avatar1,$connect){
		$query2 = "UPDATE hocsinh SET iduser='$iduser', name='$name', uid='$uid', mshs='$mshs', gender='$gender', mobile='$mobile',avatar='$avatar' WHERE id = '$id'";
		$data2 = mysqli_query($connect, $query2);
		if ($data2) {
			$arr = ['success' => true,'message' => "thanh cong"];
			$query3 = "UPDATE diemdanh SET avatar='$avatar' WHERE iduser = '$iduser' AND uid = '$uid' AND avatar = '$avatar1'";
			mysqli_query($connect, $query3);
			$query6 = "UPDATE thongketuan SET name='$name',uid='$uid',mshs='$mshs',avatar='$avatar' WHERE idhs='$id'";
			mysqli_query($connect,$query6);

		}else{
			$arr = ['success' => false, 'message' => "khong thanh cong"];
		}mysqli_close($connect);
		return $arr;
	}
	
	// Nếu giá trị uid và mshs được gửi lên bằng với uid và mshs trên csdl theo cùng id thì cập nhật
	if($uid1 == $uid && $mshs1 == $mshs ) {
		if(basename($_FILES["avatar"]["name"]) == "a" ){
			$arr = updateNoAvatar($iduser,$name,$uid,$mshs,$gender,$mobile,$id,$connect);
		}
		// Kiểm tra nếu là tệp ảnh hợp lệ
		else if ($imageFileType == "jpg" || $imageFileType == "jpeg" || $imageFileType == "png") {	
			if (move_uploaded_file($_FILES["avatar"]["tmp_name"], $target_file)) {
				$avatar = basename($_FILES["avatar"]["name"]);
				$arr = updateAvatar($iduser,$name,$uid,$mshs,$gender,$mobile,$avatar,$id,$avatar1,$connect);
			}else {
			    $arr = ['success' => false,'message' => "Lỗi gửi hình ảnh."];
			}
		}else {
			$arr = ['success' => false,'message' => "Loại tệp hình ảnh không hợp lệ."];
		}
	}
	else if($uid1 == $uid && $mshs1 != $mshs ){
		if ($numrow_mshs > 0 ){
			$arr = [
				'success' => false,
				'message' => "Mã số học sinh đã tồn tại"
			];
		} else {		
			if(basename($_FILES["avatar"]["name"]) == "a" ){
				$arr = updateNoAvatar($iduser,$name,$uid,$mshs,$gender,$mobile,$id,$connect);
			}
			// Kiểm tra nếu là tệp ảnh hợp lệ
			else if ($imageFileType == "jpg" || $imageFileType == "jpeg" || $imageFileType == "png") {
				if (move_uploaded_file($_FILES["avatar"]["tmp_name"], $target_file)) {
					require_once '../connect.php';
					$avatar = basename($_FILES["avatar"]["name"]);
					$arr = updateAvatar($iduser,$name,$uid,$mshs,$gender,$mobile,$avatar,$id,$avatar1,$connect);
				}else {
			        $arr = ['success' => false,'message' => "Lỗi gửi hình ảnh."];
			    }
			}else {
			    $arr = ['success' => false,'message' => "Loại tệp hình ảnh không hợp lệ."];
			}
		}
	}
	else if ($uid1 != $uid && $mshs1 == $mshs ){
		if ($numrow_uid > 0 ){
			$arr = [
				'success' => false,
				'message' => "UID đã tồn tại"
			];
		}else {		
			if(basename($_FILES["avatar"]["name"]) == "a" ){
				$arr = updateNoAvatar($iduser,$name,$uid,$mshs,$gender,$mobile,$id,$connect);
			}
			// Kiểm tra nếu là tệp ảnh hợp lệ
			else if ($imageFileType == "jpg" || $imageFileType == "jpeg" || $imageFileType == "png") {
				if (move_uploaded_file($_FILES["avatar"]["tmp_name"], $target_file)) {
					$avatar = basename($_FILES["avatar"]["name"]);
					$arr = updateAvatar($iduser,$name,$uid,$mshs,$gender,$mobile,$avatar,$id,$avatar1,$connect);
				}else {
			        $arr = ['success' => false,'message' => "Lỗi gửi hình ảnh."];
			    }
			}else {
			    $arr = ['success' => false,'message' => "Loại tệp hình ảnh không hợp lệ."];
			}
		}
	}
	else {
		if ($numrow_uid > 0 ){
			$arr = [
				'success' => false,
				'message' => "UID đã tồn tại"
			];
		}
		else if ($numrow_mshs > 0 ){
			$arr = [
				'success' => false,
				'message' => "Mã số học sinh đã tồn tại"
			];
		} else {
			if(basename($_FILES["avatar"]["name"]) == "a" ){
				$arr = updateNoAvatar($iduser,$name,$uid,$mshs,$gender,$mobile,$id,$connect);
			}
			// Kiểm tra nếu là tệp ảnh hợp lệ
			else if ($imageFileType == "jpg" || $imageFileType == "jpeg" || $imageFileType == "png") {
				if (move_uploaded_file($_FILES["avatar"]["tmp_name"], $target_file)) {
					$avatar = basename($_FILES["avatar"]["name"]);
					$arr = updateAvatar($iduser,$name,$uid,$mshs,$gender,$mobile,$avatar,$id,$avatar1,$connect);
				}else {
			        $arr = ['success' => false,'message' => "Lỗi gửi hình ảnh."];
			    }
			}else {
			    $arr = ['success' => false,'message' => "Loại tệp hình ảnh không hợp lệ."];
			}
		}
	}
	print_r(json_encode($arr));
?>	