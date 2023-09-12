<?php 

include "koneksi.php";

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'regis':
                // kd_user
                $query = mysqli_query($conn, "SELECT max(kd_user) AS id_terbesar FROM user");
                $data = mysqli_fetch_array($query);
                $kd_user = $data['id_terbesar'];
                $urut = (int) substr($kd_user, 3);
                $urut++;
                $depan = "USR";
                $kode_user = $depan . sprintf("%06s", $urut);

                $username = $_POST['username'];
                $password = $_POST['password'];
                $no_hp = $_POST['no_hp'];
                $nama = $_POST['nama'];

                $sql = "SELECT * FROM user WHERE username = '$username'";
                $result = mysqli_query($conn, $sql);
                if (mysqli_num_rows($result)>0) {
                    $respon['respon'] = '0';
                    echo json_encode($respon);
                    exit();
                } else {
                    $sql = "SELECT * FROM profil WHERE no_hp = '$no_hp'";
                    $result = mysqli_query($conn, $sql);
                    if (mysqli_num_rows($result)>0) {
                        $respon['respon'] = '1';
                        echo json_encode($respon);
                        exit();
                    } else {
                        $sql = "SELECT * FROM profil WHERE nama = '$nama'";
                        $result = mysqli_query($conn, $sql);
                        if (mysqli_num_rows($result)>0) {
                            $respon['respon'] = '2';
                            echo json_encode($respon);
                            exit();
                        } else {
                            $sql = "INSERT INTO profil(nama, no_hp, foto_profil) VALUES('$nama','$no_hp','user.png')";
                            $result = mysqli_query($conn, $sql);

                            $sql2 = "INSERT INTO `user`(kd_user, nama, username, `password`, `level`)
                                VALUES('$kode_user','$nama','$username','$password','User')";
                            $result2 = mysqli_query($conn, $sql2);
                            if ($result2) {
                                $respon['respon']= "3";
                                echo json_encode($respon);
                                exit();
                            }
                        }
                    }
                }
                break;
            case 'login':
                $username = $_POST['username'];
                $password = $_POST['password'];

                $sql = "SELECT * FROM user WHERE username = '$username' AND `password` = '$password'";

                $result = mysqli_query($conn, $sql);
                if (mysqli_num_rows($result)>0) {
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data = mysqli_fetch_assoc($result);

                    echo json_encode($data);
                    exit();
                }else {
                    $respon['nama'] = "0";
                    $respon['level'] = "0";
                    $respon['kd_user'] = "0";
                    echo json_encode($respon);
                    exit();
                }
                break;
        }
    }

?>