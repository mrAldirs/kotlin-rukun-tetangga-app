<?php 

include "koneksi.php";

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'insert':
                // kd_keluhan
                $query = mysqli_query($conn, "SELECT max(kd_keluhan) AS id_terbesar FROM keluhan");
                $data = mysqli_fetch_array($query);
                $kd_keluhan = $data['id_terbesar'];
                $urut = (int) substr($kd_keluhan, 3);
                $urut++;
                $depan = "KL";
                $kode_keluhan = $depan . sprintf("%06s", $urut);
                
                $kd_user = $_POST['kd_user'];
                $teks_keluhan = $_POST['teks_keluhan'];
                $imstr = $_POST['image'];
                $file = $_POST['file'];
                $path = "keluhan/";

                if ($imstr == "") {
                    $sql = "INSERT INTO keluhan(kd_keluhan, kd_user, tgl_keluhan, teks_keluhan)
                        VALUES('$kode_keluhan','$kd_user',NOW(),'$teks_keluhan')";
                    $result = mysqli_query($conn,$sql);

                    if ($result) {
                        $respon['respon']= "1";
                        echo json_encode($respon);
                        exit();
                    } else {
                        $respon['respon']= "0";
                        echo json_encode($respon);
                        exit();
                    }
                } else {
                    $sql = "INSERT INTO keluhan(kd_keluhan, kd_user, tgl_keluhan, teks_keluhan, img_keluhan)
                        VALUES('$kode_keluhan','$kd_user',NOW(),'$teks_keluhan','$file')";
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
                }
                break;
            case 'show_data_keluhan':
                $sql = "SELECT kd_keluhan, nama, teks_keluhan, img_keluhan AS img
                    FROM keluhan NATURAL JOIN user ORDER BY tgl_keluhan DESC";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data_keluhan = array();
                    while ($data = mysqli_fetch_assoc($result)) {
                        array_push($data_keluhan, $data);
                    }
                    echo json_encode($data_keluhan);
                    exit();
                } else {
                    $data_keluhan = array();
                    echo json_encode($data_keluhan);
                }
                break;
            case 'detail':
                $kd_keluhan = $_POST['kd_keluhan'];

                $sql = "SELECT kd_keluhan, nama, teks_keluhan, tgl_keluhan, CONCAT('$http_kel', img_keluhan) AS img_keluhan
                    FROM keluhan NATURAL JOIN user WHERE kd_keluhan = '$kd_keluhan'";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data = mysqli_fetch_assoc($result);

                    echo json_encode($data);
                    exit();
                } else {
                    $respon['respon']= "0";
                    echo json_encode($respon);
                    exit();
                }
                break;
        }
    }


?>