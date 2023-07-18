<?php
	include "../connect.php";

		 $email = $_POST['email'];
		 $pass  = $_POST['pass'];

	
		 $sql = "SELECT * FROM `user` WHERE `email`='$email' AND `pass`='$pass'";

		 $result = mysqli_query($connect, $sql);

		 if (mysqli_num_rows($result) == 1) {
			 $row = mysqli_fetch_assoc($result);
			
		
			 // thành công thì gán id của hàng email và pass gán vào user_id
			 $response = array('success' => true, 'id' => $row['id'] );
			// $response = array('success' => true);
		 } else {
		 	$response = array('success' => false, 'message' => 'Đăng nhập thất bại. Vui lòng kiểm tra email và mật khẩu.');
		 }
		 echo json_encode($response);

?>