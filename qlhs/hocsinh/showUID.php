<?php 
	include "../connect.php";

	$query =  "SELECT * FROM postdata WHERE id = (SELECT MAX(id) FROM postdata)";
	$data = mysqli_query($connect, $query);
	$row = mysqli_fetch_assoc($data);
	$uid_attribute = $row["uid"];

	//check data
	$query1 = "SELECT * FROM hocsinh WHERE uid = '$uid_attribute'";
	$data1 = mysqli_query($connect,$query1);
	$numrow_uid = mysqli_num_rows($data1);
		

	$query2 =  "SELECT uid FROM postdata WHERE id = (SELECT MAX(id) FROM postdata) AND $numrow_uid = 0";
	$data2 = mysqli_query($connect,$query2);
		
	$result = array();

	while ($row2 = mysqli_fetch_assoc($data2)) {
		$result[] = ($row2);
	}
		
	print_r(json_encode($result));



?>