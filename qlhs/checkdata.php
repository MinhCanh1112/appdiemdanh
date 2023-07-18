<?php 

	include "connect.php";

	$query1 = "SELECT uid,date,time FROM checkdata ORDER BY id DESC LIMIT 1";
	$result1 = mysqli_query($connect, $query1);
	$row1 = mysqli_fetch_assoc($result1);
	$uid1 = $row1["uid"];
	$date1 = $row1["date"];
	$time1 = $row1["time"];
	
	$query2 = "SELECT uid,date,time FROM postdata ORDER BY id DESC LIMIT 1";
	$result2 = mysqli_query($connect, $query2);
	$row2 = mysqli_fetch_assoc($result2);
	$uid2 = $row2["uid"];
	$date2 = $row2["date"];
	$time2 = $row2["time"];

	// So sánh 2 thuộc tính mới nhất
	if ($uid1 == $uid2 && $date1 == $date2 && $time1 == $time2) {
	    $sql = "SELECT * FROM checkdata ORDER BY id DESC LIMIT 1";
		$result = $connect->query($sql);

		if ($result->num_rows > 0) {
			$row = $result->fetch_assoc();
			$uid = $row["uid"];
			$trangthai = $row["trangthai"];

		} 
		echo json_encode(array("uid" => $uid,"trangthai" => $trangthai));

	} 
	
	
?>

