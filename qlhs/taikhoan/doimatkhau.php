<?php

include "../connect.php";

    $iduser = $_POST['id'];
    $pass = $_POST['pass'];
    $newpass = $_POST['newpass'];

    $sql_pass = 'SELECT * FROM `user` WHERE `id` = "'.$iduser.'"';
    $data_pass = mysqli_query($connect,$sql_pass);
    $row = mysqli_fetch_assoc($data_pass);
    $pass_attribute = $row["pass"];

    if($pass_attribute == $pass) {
        $query = 'UPDATE `user` SET `pass`="'.$newpass.'"  WHERE `id` = "'.$iduser.'"';
        $data = mysqli_query($connect, $query);

        if ($data) {
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

    else{
        // Trả về phản hồi thất bại
        $response["success"] = false;
        $response["message"] = "Mật khẩu cũ không chính xác";
        echo json_encode($response);
    }

?>




