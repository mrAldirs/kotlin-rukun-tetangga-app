<?php 

include "koneksi.php";

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'show_data_kegiatan_main':
                $sql = "SELECT nama, nama_kegiatan, jam_kegiatan, tgl_kegiatan FROM kegiatan NATURAL JOIN user ORDER BY tgl_kegiatan DESC LIMIT 3";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data_kegiatan = array();
                    while ($data = mysqli_fetch_assoc($result)) {
                        array_push($data_kegiatan, $data);
                    }
                    echo json_encode($data_kegiatan);
                    exit();
                } else {
                    $data_kegiatan = array();
                    echo json_encode($data_kegiatan);
                }
                break;
            case 'show_data_kegiatan':
                $nama_kegiatan = $_POST['nama_kegiatan'];

                $sql = "SELECT kd_kegiatan, nama, nama_kegiatan, jam_kegiatan, tgl_kegiatan FROM kegiatan NATURAL JOIN user
                    WHERE nama_kegiatan LIKE '%$nama_kegiatan%' ORDER BY tgl_kegiatan DESC";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data_kegiatan = array();
                    while ($data = mysqli_fetch_assoc($result)) {
                        array_push($data_kegiatan, $data);
                    }
                    echo json_encode($data_kegiatan);
                    exit();
                } else {
                    $data_kegiatan = array();
                    echo json_encode($data_kegiatan);
                }
                break;
            case 'insert':
                $nama = $_POST['nama'];

                $sql = "SELECT kd_user FROM user WHERE nama = '$nama'";
                $result = mysqli_query($conn,$sql);
                $data = mysqli_fetch_array($result);
                $kd_user = $data['kd_user'];

                // kd_kegiatan
                $query = mysqli_query($conn, "SELECT max(kd_kegiatan) AS id_terbesar FROM kegiatan");
                $data = mysqli_fetch_array($query);
                $kd_kegiatan = $data['id_terbesar'];
                $urut = (int) substr($kd_kegiatan, 3);
                $urut++;
                $depan = "KL";
                $kode_kegiatan = $depan . sprintf("%06s", $urut);
                
                $nama_kegiatan = $_POST['nama_kegiatan'];
                $jam_kegiatan = $_POST['jam_kegiatan'];
                $tgl_kegiatan = $_POST['tgl_kegiatan'];

                $sql = "INSERT INTO kegiatan(kd_kegiatan, kd_user, nama_kegiatan, tgl_kegiatan, jam_kegiatan)
                    VALUES('$kode_kegiatan','$kd_user','$nama_kegiatan','$tgl_kegiatan','$jam_kegiatan')";
                $result = mysqli_query($conn,$sql);
                if ($result) {
                    $respon['respon']= "1";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'detail':
                $kd_kegiatan = $_POST['kd_kegiatan'];

                $sql = "SELECT * FROM kegiatan NATURAL JOIN user WHERE kd_kegiatan = '$kd_kegiatan'";
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
            case 'get_nama':
                $sql = "SELECT nama_kegiatan FROM kegiatan ORDER BY tgl_kegiatan DESC";
                $result = mysqli_query($conn,$sql);
                if (mysqli_num_rows($result)>0) {
                    header("Access-Control-Allow-Origin: *");
                    header("Content-type: application/json; charset=UTF-8");

                    $nama_kegiatan = array();
                        while($nama = mysqli_fetch_assoc($result)){
                            array_push($nama_kegiatan, $nama);
                        }
                    echo json_encode($nama_kegiatan);
                }
                break;
            case 'edit':
                $nama = $_POST['nama'];

                $sql = "SELECT kd_user FROM user WHERE nama = '$nama'";
                $result = mysqli_query($conn,$sql);
                $data = mysqli_fetch_array($result);
                $kd_user = $data['kd_user'];
                
                $kd_kegiatan = $_POST['kd_kegiatan'];
                $nama_kegiatan = $_POST['nama_kegiatan'];
                $jam_kegiatan = $_POST['jam_kegiatan'];
                $tgl_kegiatan = $_POST['tgl_kegiatan'];

                $sql = "UPDATE kegiatan SET kd_user = '$kd_user', nama_kegiatan = '$nama_kegiatan', jam_kegiatan = '$jam_kegiatan', tgl_kegiatan = '$tgl_kegiatan'
                    WHERE kd_kegiatan = '$kd_kegiatan'";
                $result = mysqli_query($conn,$sql);
                if ($result) {
                    $respon['respon']= "1";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'delete':
                $kd_kegiatan = $_POST['kd_kegiatan'];

                $sql = "DELETE FROM kegiatan WHERE kd_kegiatan = '$kd_kegiatan'";
                $result = mysqli_query($conn,$sql);
                if ($result) {
                    $respon['respon']= "1";
                    echo json_encode($respon);
                    exit();
                }
                break;
        }
    }

?>