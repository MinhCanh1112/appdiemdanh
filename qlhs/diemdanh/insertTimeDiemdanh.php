<?php
	include "../connect.php";

	$iduser = $_POST['iduser'];
	$timestart = $_POST['timestart'];
	$timevao = $_POST['timevao'];
	$timera  = $_POST['timera'];
	$days = $_POST["date"];
	
	$days_str = trim($days);
    $days_arr = explode(" ", $days_str);
    foreach($days_arr as $day_str) {
        $timestamp = strtotime($day_str);
        $date = date("Y-m-d", $timestamp);
        $query2 = "INSERT INTO setupdiemdanh(iduser ,date ,timestart,timevao, timera) VALUES ('$iduser','$date','$timestart','$timevao','$timera')";
        $data2 =  mysqli_query($connect, $query2);
    }
    	
		if ($data2) {
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
	// }
	
	print_r(json_encode($arr));
?>