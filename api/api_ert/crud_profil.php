<?php 

include "koneksi.php";

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'show_profil':
                $kd_user = $_POST['kd_user'];

                $sql = "SELECT nama, nik, jenis_kelamin, usia, pendidikan, pekerjaan, email, no_hp, kd_user, username, `password`, `level`,
                    CONCAT('$http_img', foto_profil) AS foto
                    FROM profil NATURAL JOIN `user` WHERE kd_user = '$kd_user'";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data = mysqli_fetch_assoc($result);
                    
                    echo json_encode($data); 
                    exit();
                }else{
                    $respon['respon']= "0";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'get_nama':
                $sql = "SELECT nama FROM user WHERE level != 'Admin' ORDER BY nama ASC";
                $result = mysqli_query($conn,$sql);
                if (mysqli_num_rows($result)>0) {
                    header("Access-Control-Allow-Origin: *");
                    header("Content-type: application/json; charset=UTF-8");

                    $nama_user = array();
                        while($nama = mysqli_fetch_assoc($result)){
                            array_push($nama_user, $nama);
                        }
                    echo json_encode($nama_user);
                }
                break;
            case 'show_data_pengguna':
                $nama = $_POST['nama'];
                
                $sql = "SELECT kd_user, nama, no_hp, CONCAT('$http_img', foto_profil) AS img
                    FROM user NATURAL JOIN profil WHERE nama LIKE '%$nama%' AND level = 'User' ORDER BY nama ASC";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data_users = array();
                    while ($data = mysqli_fetch_assoc($result)) {
                        array_push($data_users, $data);
                    }
                    echo json_encode($data_users);
                    exit();
                } else {
                    $data_users = array();
                    echo json_encode($data_users);
                }
                break;
            case 'edit':
                $nm = $_POST['nm'];
                $nama = $_POST['nama'];
                $nik = $_POST['nik'];
                $usia = $_POST['usia'];
                $jenis_kelamin = $_POST['jenis_kelamin'];
                $email = $_POST['email'];
                $no_hp = $_POST['no_hp'];
                $pendidikan = $_POST['pendidikan'];
                $pekerjaan = $_POST['pekerjaan'];
                $imstr = $_POST['image'];
                $file = $_POST['file'];
                $path = "image/";

                if ($imstr == "") {
                    $sql = "UPDATE profil SET nama = '$nama', nik = '$nik', usia = '$usia', jenis_kelamin = '$jenis_kelamin', email = '$email', no_hp = '$no_hp', 
                        pendidikan = '$pendidikan', pekerjaan = '$pekerjaan' WHERE nama = '$nm'";
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
                    $sql = "UPDATE profil SET nama = '$nama', nik = '$nik', usia = '$usia', jenis_kelamin = '$jenis_kelamin', email = '$email', no_hp = '$no_hp', 
                        pendidikan = '$pendidikan', pekerjaan = '$pekerjaan', foto_profil = '$file' WHERE nama = '$nm'";
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
        }
    }

?>