<?php

include "../connect.php";


// Lấy thông tin email hoặc tên đăng nhập từ yêu cầu POST
$email = $_POST["email"];

// Tìm kiếm tài khoản của người dùng dựa trên email
$query = 'SELECT * FROM user WHERE email = "'.$email.'"';
$result = mysqli_query($connect, $query);

if (mysqli_num_rows($result) > 0) {
    // Tìm thấy tài khoản của người dùng
    $row = mysqli_fetch_array($result);

    // Tạo mật khẩu mới ngẫu nhiên
    $newPassword = substr(md5(uniqid(rand(), true)), 0, 8);

    // Cập nhật mật khẩu mới vào cơ sở dữ liệu
    $sql = 'UPDATE user SET pass = "'.$newPassword.'" WHERE email = "'.$email.'"';
    mysqli_query($connect, $sql);


    // Gửi email chứa mật khẩu mới đến người dùng
    $to = $row['email'];
    $subject = 'Mật khẩu mới cho tài khoản của bạn';
    $message = 'Mật khẩu mới của bạn là: ' . $newPassword;
    $headers = 'From: cntteam003@gmail.com' . "\r\n" .
        'Reply-To: '.$email.' ' . "\r\n" .
        'X-Mailer: PHP/' . phpversion();

    mail($to, $subject, $message, $headers);


    // Trả về phản hồi thành công
    $response["success"] = true;
    $response["message"] = "ok";
    echo json_encode($response);
} else {
    // Trả về phản hồi thất bại
    $response["success"] = false;
    $response["message"] = "Email không tồn tại";
    echo json_encode($response);
}

?>



