<?php 

include "koneksi.php";

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'insert':
                $query = mysqli_query($conn, "SELECT max(kd_dokumentasi) AS id_terbesar FROM dokumentasi");
                $data = mysqli_fetch_array($query);
                $kd_dokumentasi = $data['id_terbesar'];

                $urut = (int) substr($kd_dokumentasi, 3);
                $urut++;

                $depan = "ALB";
                $kode_dokumentasi = $depan . sprintf("%06s", $urut);

                $kd_kegiatan = $_POST['kd_kegiatan'];
                $imstr = $_POST['image'];
                $file = $_POST['file'];
                $path = "doc/";

                $sql = "INSERT INTO dokumentasi(kd_dokumentasi, kd_kegiatan, foto_dokumentasi)
                    VALUES('$kode_dokumentasi','$kd_kegiatan','$file')";
                $result = mysqli_query($conn,$sql);

                if ($result) {
                    if(file_put_contents($path.$file, base64_decode($imstr))==false){
                        $respon['respon']= "0";
                        echo json_encode($respon);
                        exit();
                    } else {
                        $respon['respon']= "1";
                        echo json_encode($respon);
                        exit();
                    }
                }
                break;
            case 'show_dokumentasi':
                $kd_kegiatan = $_POST['kd_kegiatan'];
                
                $sql = "SELECT kd_kegiatan, kd_dokumentasi, foto_dokumentasi, CONCAT('$http_doc', foto_dokumentasi) AS img_dokumentasi
                    FROM dokumentasi NATURAL JOIN kegiatan WHERE kd_kegiatan = '$kd_kegiatan' ORDER BY kd_kegiatan ASC";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data_dokumentasi = array();
                    while ($data = mysqli_fetch_assoc($result)) {
                        array_push($data_dokumentasi, $data);
                    }
                    echo json_encode($data_dokumentasi);
                    exit();
                } else {
                    $data_dokumentasi = array();
                    echo json_encode($data_dokumentasi);
                }
                break;
            case 'delete':
                $kd_dokumentasi = $_POST['kd_dokumentasi'];

                $sql = "DELETE FROM dokumentasi WHERE kd_dokumentasi = '$kd_dokumentasi'";
                $result = mysqli_query($conn,$sql);
                if ($result) {
                    $respon['respon']= "1";
                    echo json_encode($respon);
                    exit();
                } else {
                    $respon['respon'] = "0";
                    echo json_encode($respon);
                    exit();
                }
                break;
        }
    }

?>