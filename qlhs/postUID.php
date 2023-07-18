<?php
//Creates new record as per request
    //Connect to database
	include "connect.php";

    // Check connection
    if ($connect->connect_error) {
        die("Database Connection failed: " . $connect->connect_error);
    }

    //Get current date and time
	//date_default_timezone_get();
	//$timezone = date_default_timezone_get();
	//echo $timezone;
    date_default_timezone_set('Asia/Ho_Chi_Minh');
    $d = date("Y-m-d");
    //echo " Date:".$d."<BR>";
    $t = date("H:i:s");

    $uida = $_POST['uid'];

	$sql = "INSERT INTO postdata (uid, date, time) VALUES ('$uida', '$d', '$t')";

	if ($connect->query($sql) === TRUE) {
		echo "cap nhat thanh cong";
		
	// insert bảng postdata và hocsinh thành bảng checkdata với điều kiện cùng uid và id của postdata là lớn nhất

	$query1 = "INSERT INTO checkdata(uid,iduser,name,mshs,gender,mobile,date,time,avatar) 
	SELECT p.uid,iduser,name,mshs,gender,mobile,date,time,avatar 
	FROM postdata AS p 
	JOIN hocsinh as h ON p.uid = h.uid AND p.id = (SELECT MAX(id) FROM postdata)";

		if ($connect->query($query1) === TRUE) {
			// thực hiện câu lệnh truy vấn
			// chọn các thuộc tính từ bảng checkdata với điều kiện id của checkdata là lớn nhất
			$query3 = "SELECT * FROM checkdata WHERE id = (SELECT MAX(id) FROM checkdata WHERE uid = '$uida')";
			$data3 = mysqli_query($connect, $query3);
			$row3 = mysqli_fetch_assoc($data3);
			// lấy cột thuộc tính date, iduser,time
			$date3 = $row3["date"];
			$iduser3 = $row3["iduser"];
			$time3 = $row3["time"];	

			// Tính toán tuần và năm ứng với ngày điểm danh
			$new_week = date('W', strtotime($date3));
			$new_year = date('Y', strtotime($date3));
			// Lấy ngày đầu tiên của tuần
			$firstDayOfWeek = date('Y-m-d', strtotime($new_year."W".$new_week.'1'));
			// Lấy ngày cuối cùng của tuần
			$lastDayOfWeek = date('Y-m-d', strtotime($new_year."W".$new_week.'7'));				

			// chọn từ bảng setupdiemdanh theo iduser và date(ngày điểm danh) 
			// và time vào là nhỏ nhất theo iduser và date(ngày điểm danh) 
			$query7 = "SELECT * FROM setupdiemdanh 
			WHERE iduser = '$iduser3' AND date = '$date3' 
			AND timevao = (SELECT MIN(timevao) FROM setupdiemdanh WHERE iduser = '$iduser3' AND date = '$date3')";
			$data7 = mysqli_query($connect,$query7);
			$row7 = mysqli_fetch_assoc($data7);
			$timevao7 = $row7["timevao"];
			$timera7 = $row7["timera"];
			$timestart7 = $row7["timestart"];
			$date7 = $row7["date"];
			$idsetupdd7 = $row7["id"];

			// chọn từ bảng setupvesom theo iduser và idsetupdd đang điểm danh
			$query13 = "SELECT * FROM setupvesom WHERE iduser = '$iduser3' AND idsetupdd ='$idsetupdd7'";
			$data13 = mysqli_query($connect,$query13);
			$row13 = mysqli_fetch_assoc($data13);
			// lấy ra dữ liệu time về sớm
			$timevesom13 = $row13["timevesom"];
			// đếm số lượng bản ghi
			$numrow13 = mysqli_num_rows($data13);

			// check trạng thái điểm danh vào
			$query14="SELECT * FROM diemdanh 
			WHERE uid='$uida' AND idsetupdd='$idsetupdd7' AND (trangthai='Đã điểm danh' OR trangthai='Đi học trễ')";
			$data14 = mysqli_query($connect, $query14);	
			$numrow14 = mysqli_num_rows($data14);	

			// check trạng thái điểm danh ra
			$query17="SELECT *FROM diemdanh 
			WHERE uid='$uida' AND idsetupdd='$idsetupdd7' AND (trangthai='Về sớm' OR trangthai='Đã ra về')";
			$data17 = mysqli_query($connect, $query17);	
			$numrow17 = mysqli_num_rows($data17);	

			// check ngày điểm danh đã tồn tại chưa
			$query25 = "SELECT * FROM ngaydiemdanh WHERE idsetupdd = '$idsetupdd7'";
			$data25 = mysqli_query($connect,$query25);
			// đếm số lượng bản ghi ngày điểm danh
			$numrow25 = mysqli_num_rows($data25);

			$query22 = "SELECT * FROM lophoc WHERE iduser = '$iduser3'";
			$data22 = mysqli_query($connect, $query22);
			$row22 = mysqli_fetch_assoc($data22);
			// lấy cột thuộc tính 
			$siso22 = $row22["siso"];	
			$lop22 = $row22["lop"];
			$numrow22 = mysqli_num_rows($data22);

			function thongke($iduser3,$idsetupdd7,$connect){
				$query24 = "SELECT * FROM thongkediemdanh WHERE iduser= '$iduser3' AND idsetupdd= '$idsetupdd7'";
				$data24 = mysqli_query($connect, $query24);
				$row24 = mysqli_fetch_assoc($data24);
				// lấy cột thuộc tính 
				$dadiemdanh24 = $row24["dadiemdanh"];
				$diemdanhve24 = $row24["diemdanhve"];
				$numrow24 = mysqli_num_rows($data24);
				return array("dadiemdanh" => $dadiemdanh24, "diemdanhve" => $diemdanhve24, "numrow" => $numrow24);
			}

			$result = thongke($iduser3,$idsetupdd7,$connect);
			$numrow24 = $result["numrow"];
			$dadiemdanh24 = $result["dadiemdanh"];


			function insertDiemdanh($trangthai,$uid,$idsetupdd){
				$query8 = "INSERT INTO diemdanh(iduser,idngaydd,idsetupdd,uid,name,mshs,gender,mobile,date,time,timevao,timera,trangthai,avatar) 
				SELECT c.iduser,n.idngaydd,s.id,uid,name,mshs,gender,mobile,c.date,c.time,s.timevao,s.timera,'$trangthai' ,avatar
				FROM setupdiemdanh AS s
				JOIN checkdata AS c  ON c.iduser = s.iduser AND c.date = s.date AND c.id = (SELECT MAX(id) FROM checkdata WHERE uid = '$uid')
				JOIN ngaydiemdanh AS n ON s.id = n.idsetupdd WHERE s.id = '$idsetupdd'";
				return $query8;
			}

			function countTrangthai($idsetupdd,$trangthai1,$trangthai2,$conn){
				// Sử dụng COUNT() để đếm số lượng các trạng thái theo idsetupdd
				$query19 = "SELECT COUNT(*) AS total FROM diemdanh WHERE idsetupdd= '$idsetupdd' AND (trangthai='$trangthai1' OR trangthai='$trangthai2')";
				$data19 = mysqli_query($conn, $query19);
				// Lấy kết quả trả về
				$row19 = mysqli_fetch_assoc($data19);	
				$total19 = $row19['total'];
				return $total19;
			}

			function insertNgayThongke($idsetupdd,$iduser,$date,$timestart,$timevao,$timera,$lop,$siso,$numrow,$conn){
				$query12="INSERT INTO ngaydiemdanh(idsetupdd,iduser,date,timestart,timevao,timera)
				VALUES('$idsetupdd','$iduser','$date','$timestart','$timevao','$timera')";
				mysqli_query($conn, $query12);
				if($numrow == 1){
					$query23 = "INSERT INTO thongkediemdanh(iduser,idsetupdd,lop,siso) VALUES ('$iduser','$idsetupdd','$lop','$siso')";
					mysqli_query($conn, $query23);
				}

			}

			function deleteSetup($iduser,$idsetupdd,$conn){
				$query2 = "DELETE FROM setupdiemdanh WHERE iduser = '$iduser' AND id = '$idsetupdd'";
				if ($conn->query($query2) === TRUE) {	
					$query6 = "DELETE FROM setupvesom WHERE iduser = '$iduser'AND idsetupdd = '$idsetupdd'";
					mysqli_query($conn, $query6);
				}
			}
			
			// insert all học sinh từ bảng hocsinh sang bảng thongketuan với đk đúng iduser
			// insert tuần và năm đang điểm danh vào bảng tuandiemdanh
			function insertWeek($iduser3,$new_week,$new_year,$firstDayOfWeek,$lastDayOfWeek,$connect){
				// Kiểm tra xem tuần mới đã tồn tại trong bảng tuần hay chưa
				$query4 = "SELECT * FROM thongketuan WHERE iduser = $iduser3 AND week = $new_week AND year = $new_year";
				$data4 = mysqli_query($connect, $query4);
				$numrow4 = mysqli_num_rows($data4);

				$query = "SELECT * FROM tuandiemdanh WHERE iduser = $iduser3 AND week = $new_week AND year = $new_year";
				$data = mysqli_query($connect, $query);
				$numrow = mysqli_num_rows($data);
			
				// Nếu tuần mới chưa tồn tại, thêm vào bảng tuần
				if($numrow == 0) {
					$query2 = "INSERT INTO tuandiemdanh(iduser,week,year,firstDay,lastDay) 
					VALUE ('$iduser3','$new_week','$new_year','$firstDayOfWeek','$lastDayOfWeek')";
					if ($connect->query($query2) === TRUE) {	
						$id = mysqli_insert_id($connect);
						if($numrow4 == 0) {
							$query9 = "INSERT INTO thongketuan(iduser,idweek,week,year,idhs,name,uid,mshs,gender,mobile,avatar) 
							SELECT h.iduser,'$id','$new_week','$new_year',h.id,name,uid,mshs,gender,mobile,avatar 
							FROM hocsinh AS h WHERE h.iduser = $iduser3";
							mysqli_query($connect, $query9);
						}
					}
				}						
			}

			// đếm số lượng uid với idsetupdd đang điểm danh và trạng thái được chọn
			function countWeek($uid,$idsetupdd,$trangthai,$conn){
				$query = "SELECT COUNT(*) AS total FROM diemdanh WHERE uid = '$uid' AND idsetupdd= '$idsetupdd' AND trangthai='$trangthai'";
				$data = mysqli_query($conn, $query);
				// Lấy kết quả trả về
				$row = mysqli_fetch_assoc($data);	
				$total1 = $row['total'];
				return $total1;
			}



			// cập nhật danh sách học sinh vắng trong buổi học
			function updateVang($iduser3,$idsetupdd7,$date,$timevao,$timera,$new_week,$new_year,$connect){
				// Lấy danh sách các học sinh vắng cần cập nhật
				$query = "SELECT *FROM thongketuan
				WHERE NOT EXISTS (SELECT * FROM diemdanh WHERE thongketuan.uid = diemdanh.uid 
				AND idsetupdd = '$idsetupdd7' AND iduser = '$iduser3' AND week = '$new_week' AND year ='$new_year') 
				AND iduser = '$iduser3' AND week = '$new_week' AND year ='$new_year'";
				$data = mysqli_query($connect,$query);
				// Duyệt qua từng bản ghi và cập nhật trạng thái
				while ($row = mysqli_fetch_assoc($data)) {
					$vang = $row['vang'];
					$id = $row['id'];
					$query2 = "UPDATE thongketuan SET vang = 1 + $vang 
					WHERE id = $id AND NOT EXISTS (
						SELECT * FROM diemdanh 
						WHERE thongketuan.uid = diemdanh.uid AND idsetupdd = '$idsetupdd7'
						AND iduser = '$iduser3' AND week = '$new_week' AND year ='$new_year' AND trangthai ='Chưa điểm danh') 
					AND iduser = '$iduser3' AND week = '$new_week' AND year ='$new_year'";
					mysqli_query($connect, $query2);


					$query3 = "INSERT INTO danhsachthongketuan(iduser,idweek,idsetupdd,idhs,name,uid,date,timevao,timera,trangthai)
					SELECT iduser,idweek, '$idsetupdd7', idhs, name, uid, '$date', '$timevao', '$timera', 'Vắng'
           			FROM thongketuan 
					WHERE id = $id AND NOT EXISTS (
						SELECT * FROM diemdanh 
						WHERE thongketuan.uid = diemdanh.uid 
							AND idsetupdd = '$idsetupdd7'
							AND iduser = '$iduser3' 
							AND week = '$new_week' 
							AND year ='$new_year' 
							AND trangthai ='Chưa điểm danh') 
					AND iduser = '$iduser3' 
					AND week = '$new_week' 
					AND year ='$new_year'";
					mysqli_query($connect, $query3);
				}				
			}

			function insertSolandiemdanh($iduser,$firstDayOfWeek,$lastDayOfWeek,$connect){
				// đếm số lượng all ngày điểm danh trong tuần điểm danh theo iduser
				$query = "SELECT COUNT(*) AS total FROM ngaydiemdanh 
				WHERE iduser ='$iduser' AND date BETWEEN '$firstDayOfWeek' AND '$lastDayOfWeek'";
				$data = mysqli_query($connect, $query);
				$row = mysqli_fetch_assoc($data);	
				$total = $row['total'];	
				//insert số lần điểm danh trong tuần vào bảng tuandiemdanh theo iduser	
				$query15 = "UPDATE tuandiemdanh SET solandd='$total' 
				WHERE iduser = '$iduser' AND firstDay = '$firstDayOfWeek' AND lastDay ='$lastDayOfWeek'";
				mysqli_query($connect, $query15);
			}

			// nếu chưa tồn tại trạng thái 'đã điểm danh' và 'đi học trễ' với đk idsetupdd đang điểm danh
			// và thời gian điểm danh nhỏ hơn thời gian điểm danh ra và >= time start điểm danh
			if($numrow14 == 0 && $time3 < $timera7 && $time3 >= $timestart7){
				echo "ok";
				// nếu chưa tồn tại ngaydiemdanh va thongkediemdanh với đk idsetupdd đang điểm danh
				if($numrow25 ==0 && $numrow24==0){
					//gọi hàm insertNgayThongke để insert ngày và thống kê điểm danh 
					insertNgayThongke($idsetupdd7,$iduser3,$date7,$timestart7,$timevao7,$timera7,$lop22,$siso22,$numrow22,$connect);
				}	
				// nếu quét thẻ đúng giờ 
				if($time3 < $timevao7 && $time3 >= $timestart7){
					echo "Đã điểm danh";
					$query8 = insertDiemdanh('Đã điểm danh',$uida,$idsetupdd7);				
					if ($connect->query($query8) === TRUE) {	
						//gọi hàm insertWeek để thêm all học sinh sang bảng thongketuan với đúng iduser
						insertWeek($iduser3,$new_week,$new_year,$firstDayOfWeek,$lastDayOfWeek,$connect);
						$total19 = countTrangthai($idsetupdd7,'Đi học trễ','Đã điểm danh',$connect);
						// vắng = sĩ số - số hs đã điểm danh
						$vang = $siso22 - $total19;
						$query21 = "UPDATE thongkediemdanh SET dadiemdanh='$total19',vang='$vang' WHERE iduser= '$iduser3' AND idsetupdd= '$idsetupdd7'";
						mysqli_query($connect, $query21);
						if($total19 ==1){
							insertSolandiemdanh($iduser3,$firstDayOfWeek,$lastDayOfWeek,$connect);
						}
						
					}
				}
				// nếu quét thẻ trễ giờ
				else if ($time3 >= $timevao7 && $timera7 > $time3){
					echo "Đi học trễ";
					// gọi hàm insertDiemdanh để insert học sinh điểm danh với trạng thái hiện tại vào bảng điểm danh
					$query8 = insertDiemdanh('Đi học trễ',$uida,$idsetupdd7);
					if ($connect->query($query8) === TRUE) {	
						//gọi hàm insertWeek để thêm all học sinh sang bảng thongketuan với đúng iduser
						insertWeek($iduser3,$new_week,$new_year,$firstDayOfWeek,$lastDayOfWeek,$connect);
						//gọi hàm coutTrangthai để đếm tất cả số lượng học sinh đã điểm danh
						$total19 = countTrangthai($idsetupdd7,'Đi học trễ','Đã điểm danh',$connect);
						if($total19 ==1){
							insertSolandiemdanh($iduser3,$firstDayOfWeek,$lastDayOfWeek,$connect);
						}
						//gọi hàm coutTrangthai để đếm tất cả số lượng học sinh đã đi học trễ
						$total20 =  countTrangthai($idsetupdd7,'Đi học trễ',null,$connect) ;							
						// vắng = sĩ số - số hs đã điểm danh
						$vang = $siso22 - $total19;
						//update số lượng đã điểm danh, vào trễ và vắng trong lần điểm danh hiện tại
						$query21 = "UPDATE thongkediemdanh SET dadiemdanh='$total19',vaotre='$total20',vang='$vang' 
						WHERE iduser= '$iduser3' AND idsetupdd= '$idsetupdd7'";
						mysqli_query($connect, $query21);

						// update đi học trễ cho mỗi học sinh trong tuần điểm danh
						//chọn ra dữ liệu từ field vaotre với điều kiện bằng với uida và iduser
						$query4 = "SELECT * FROM thongketuan 
						WHERE uid = '$uida' AND iduser = '$iduser3'AND week = '$new_week' AND year ='$new_year'";
						$data4 = mysqli_query($connect, $query4);
						$row4 = mysqli_fetch_assoc($data4);	
						$vaotre4 = $row4['vaotre'];
						// đếm số lượng uida vào trễ với idsetupdd hiện tại
						$total1 = countWeek($uida,$idsetupdd7,"Đi học trễ",$connect);
					
						$vaotre5 = $total1 + $vaotre4;
						// update số lượng đi học trễ cho học sinh có uid điểm danh trong tuần điểm danh hiện tại
						$query5 = "UPDATE thongketuan SET vaotre = '$vaotre5' 
						WHERE uid = '$uida' AND iduser = '$iduser3' AND week = '$new_week' AND year ='$new_year'";
						mysqli_query($connect, $query5);

						$query20 = "INSERT INTO danhsachthongketuan(iduser,idweek,idsetupdd,idhs,name,uid,date,timevao,timera,trangthai)
						SELECT iduser,idweek, '$idsetupdd7', idhs, name, uid, '$date7', '$timevao7', '$timera7', 'Đi học trễ'
						FROM thongketuan 
						WHERE uid = '$uida' AND iduser = '$iduser3' AND week = '$new_week' AND year ='$new_year'";;
						mysqli_query($connect, $query20);

					}				
				}
			}

			// nếu đã tồn tại dòng numrow14 
			// và thời gian điểm danh lớn hơn thời gian điểm danh ra
			else if($numrow14 == 1 && $numrow17 == 0 && ($time3 > $timera7 || ($timevesom13 <= $time3 && $time3 < $timera7 && $numrow13 == 1))){
					echo "ok ok";			
				if($numrow25 ==0 && $numrow24==0){
					insertNgayThongke($idsetupdd7,$iduser3,$date7,$timestart7,$timevao7,$timera7,$lop22,$siso22,$numrow22,$connect);
				}					
				if($timevesom13 <= $time3 && $time3 < $timera7 && $numrow13 == 1){
					echo "Về sớm";
					$query8 = insertDiemdanh('Về sớm',$uida,$idsetupdd7);																						
					if ($connect->query($query8) === TRUE) {			
						$total19 = countTrangthai($idsetupdd7,'Đã ra về','Về sớm',$connect) ;
						$total20 =  countTrangthai($idsetupdd7,'Về sớm',null,$connect) ;
						
						$query4 = "SELECT * FROM thongketuan 
						WHERE uid = '$uida' AND iduser = '$iduser3' AND week = '$new_week' AND year ='$new_year'";
						$data4 = mysqli_query($connect, $query4);
						$row4 = mysqli_fetch_assoc($data4);	
						$vesom4 = $row4['vesom'];
						$total1 = countWeek($uida,$idsetupdd7,"Về sớm",$connect);
						$vesom5 = $total1 + $vesom4;
						$query5 = "UPDATE thongketuan SET vesom = '$vesom5' 
						WHERE uid = '$uida' AND iduser = '$iduser3' AND week = '$new_week' AND year ='$new_year'";
						mysqli_query($connect, $query5);


						
						$query20 = "INSERT INTO danhsachthongketuan(iduser,idweek,idsetupdd,idhs,name,uid,date,timevao,timera,trangthai)
						SELECT iduser,idweek, '$idsetupdd7', idhs, name, uid, '$date7', '$timevao7', '$timera7', 'Về sớm'
						FROM thongketuan 
						WHERE uid = '$uida' AND iduser = '$iduser3' AND week = '$new_week' AND year ='$new_year'";;
						mysqli_query($connect, $query20);

						$query21 = "UPDATE thongkediemdanh SET diemdanhve ='$total19', vesom ='$total20' WHERE iduser = '$iduser3' AND idsetupdd = '$idsetupdd7'";				
						if ($connect->query($query21) === TRUE) {	
							$result4 = thongke($iduser3,$idsetupdd7,$connect);						
							$dadiemdanh4 = $result4["dadiemdanh"];
							$diemdanhve4 = $result4["diemdanhve"];
							// khi số lượng điểm danh ra bằng điểm danh vào thì xóa thiết lập điểm danh (kết thúc điểm danh)
							if($dadiemdanh4 == $diemdanhve4){
								updateVang($iduser3,$idsetupdd7,$date7,$timevao7,$timera7,$new_week,$new_year,$connect);							
								deleteSetup($iduser3,$idsetupdd7,$connect);
							}
						}
					}
				}	

				else if($time3 >= $timera7){
					echo "Đã ra về";
					$query8 = insertDiemdanh('Đã ra về',$uida,$idsetupdd7);						
					if ($connect->query($query8) === TRUE) {
						$total19 = countTrangthai($idsetupdd7,'Đã ra về','Về sớm',$connect);
						$query21 = "UPDATE thongkediemdanh SET diemdanhve ='$total19' WHERE iduser = '$iduser3' AND idsetupdd = '$idsetupdd7'";						
						if ($connect->query($query21) === TRUE) {	
							$total2 = countTrangthai($idsetupdd7,'Đã ra về',null,$connect);
							if($total2 ==1){					
								updateVang($iduser3,$idsetupdd7,$date7,$timevao7,$timera7,$new_week,$new_year,$connect);
							}
							$result4 = thongke($iduser3,$idsetupdd7,$connect);						
							$dadiemdanh4 = $result4["dadiemdanh"];
							$diemdanhve4 = $result4["diemdanhve"];
							// khi số lượng điểm danh ra bằng điểm danh vào thì xóa thiết lập điểm danh (kết thúc điểm danh)
							if($dadiemdanh4 == $diemdanhve4){									
								deleteSetup($iduser3,$idsetupdd7,$connect);
							}
						}
					}
				}		
			}
				
			else if($numrow14 == 0 && $time3 > $timera7 && $timera7 != null){
				echo "Chưa điểm danh";
				$query8 = insertDiemdanh('Chưa điểm danh',$uida,$idsetupdd7,$connect);				
				mysqli_query($connect, $query8);					
			}

			// nếu đã quét thẻ lần 2
			else if(($numrow14 == 1 || $numrow17 == 1) && $timera7 != null){
				echo "đã điểm danh 2";
				$query8 = insertDiemdanh('Đã điểm danh 2',$uida,$idsetupdd7,$connect);
				mysqli_query($connect, $query8);
			}
			else {				
				$query8 = "UPDATE checkdata SET trangthai='Chưa bắt đầu' WHERE id = (SELECT MAX(id) FROM checkdata WHERE uid = '$uida') AND iduser = '$iduser3'";
				mysqli_query($connect, $query8);
			}
		
		}
	} 
	else {
		    echo "Error: " . $sql . "<br>" . $connect->error;
		   
	}

	$connect->close();
?>