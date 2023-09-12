<?php 

include "koneksi.php";


function generateKdAnggota() {
    $kdAnggota = 'ANG-' . uniqid();
    return $kdAnggota;
}

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'detail_anggota':
                $kd_user = $_POST['kd_user'];

                $sql = "SELECT * FROM anggota_keluarga WHERE kd_user = ?";
                $stmt = $conn->prepare($sql);
                $stmt->bind_param("s", $kd_user);
                $stmt->execute();
                $result = $stmt->get_result();

                $data = array();
                while ($row = $result->fetch_assoc()) {
                    $data[] = $row;
                }

                if (count($data) > 0) {
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    echo json_encode($data);
                    exit();
                } else {
                    $response = "0"; // Data kosong
                    echo $response;
                }
                break;
            case 'save_anggota':
                $kd_user = $_POST['kd_user'];
    
                // Hapus data anggota keluarga yang terkait dengan kd_user tertentu
                $deleteSql = "DELETE FROM anggota_keluarga WHERE kd_user = '$kd_user'";
                mysqli_query($conn, $deleteSql);

                // Mengubah nama anggota dengan status "Istri" jika ada
                if (!empty($_POST['nama_istri'])) {
                    $kdAnggotaIstri = generateKdAnggota();
                    $namaIstri = $_POST['nama_istri'];
                    $insertIstriSql = "INSERT INTO anggota_keluarga (kd_anggota, kd_user, nama_anggota, status_anggota)
                                    VALUES ('$kdAnggotaIstri', '$kd_user', '$namaIstri', 'Istri')";
                    mysqli_query($conn, $insertIstriSql);
                }
    
                // Loop melalui data anggota keluarga yang dikirimkan
                for ($i = 1; $i <= 5; $i++) {
                    $namaAnggota = $_POST['nama_anak' . $i];
                    $statusAnggota = 'Anak ' . $i;
    
                    // Periksa apakah nama anggota tidak kosong
                    if (!empty($namaAnggota)) {
                        $kdAnggota = generateKdAnggota();
                        $insertSql = "INSERT INTO anggota_keluarga (kd_anggota, kd_user, nama_anggota, status_anggota)
                                    VALUES ('$kdAnggota', '$kd_user', '$namaAnggota', '$statusAnggota')";
                        mysqli_query($conn, $insertSql);
                    }
                }
    
                $respon['respon'] = "1";
                echo json_encode($respon);
                exit();
                break;    
        }
    }

?>