<?php 
	include "../connect.php";
	$iduser = $_POST['iduser'];
	$idweek = $_POST['idweek'];
    $idhs = $_POST['idhs'];
    $trangthai = $_POST['trangthai'];


	$query = "SELECT *FROM danhsachthongketuan WHERE iduser='$iduser' AND idweek='$idweek'AND idhs='$idhs'AND trangthai='$trangthai'";
	$data = mysqli_query($connect,$query);
	
	$result = array();
	while ($row = mysqli_fetch_assoc($data)) {
		$result[] = ($row);
	}		
	print_r(json_encode($result));

?>	