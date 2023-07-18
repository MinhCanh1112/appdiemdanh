<?php 
	include "../connect.php";

	$id = $_POST['id'];
	$timestart = $_POST['timestart'];
	$timevao = $_POST['timevao'];
	$timera = $_POST['timera'];

    $query2 = "UPDATE setupdiemdanh SET timestart='$timestart', timevao='$timevao', timera='$timera' WHERE id = '$id'";
    $data2 = mysqli_query($connect, $query2);
    if ($data2) {
        $arr = [ 'success' => true,'message' => "thanh cong"];

    }else{
        $arr = ['success' => false, 'message' => "khong thanh cong"];
    }

	print_r(json_encode($arr));
?>	