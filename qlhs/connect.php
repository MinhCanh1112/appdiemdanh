<?php
	$host ="localhost";
	$username = "root";
	$password = "";
	$database = "doan";
	$connect = mysqli_connect($host,$username,$password,$database);

	mysqli_query($connect, "SET NAMES 'utf8'");
	
?>
	