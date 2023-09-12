<?php 

include "koneksi.php";

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'show_data_laporan_main':
                $sql = "SELECT kd_laporan, nama_kegiatan, tgl_laporan, uang_masuk, uang_keluar, total, (uang_masuk-uang_keluar) AS sisa, keterangan
                    FROM kegiatan NATURAL JOIN laporan ORDER BY tgl_laporan DESC LIMIT 1";
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
            case 'show_data_laporan':
                $tgl_laporan = $_POST['tgl_laporan'];
                
                $sql = "SELECT kd_laporan, tgl_laporan, tgl_laporan, uang_masuk, uang_keluar, total, keterangan
                    FROM laporan WHERE tgl_laporan LIKE '%$tgl_laporan%' ORDER BY tgl_laporan DESC";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data_laporan = array();
                    while ($data = mysqli_fetch_assoc($result)) {
                        array_push($data_laporan, $data);
                    }
                    echo json_encode($data_laporan);
                    exit();
                } else {
                    $data_laporan = array();
                    echo json_encode($data_laporan);
                }
                break;
            case 'laporan_chart':
                $sql = "SELECT tgl_laporan, total FROM laporan ORDER BY tgl_laporan ASC LIMIT 5";
                $result = mysqli_query($conn, $sql);

                if(mysqli_num_rows($result) > 0) {
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");

                    $data_laporan = array();
                    while ($data = mysqli_fetch_assoc($result)) {
                        $tgl_laporan = $data['tgl_laporan'];
                        $total = $data['total'];
                        $entry = array("x" => $tgl_laporan, "y" => $total);
                        array_push($data_laporan, $entry);
                    }

                    echo json_encode($data_laporan);
                } else {
                    $data_laporan = array();
                    echo json_encode($data_laporan);
                }
                break;
            case 'laporan_chart_keuangan':
                $sql = "SELECT tgl_laporan, uang_masuk, uang_keluar FROM laporan ORDER BY tgl_laporan ASC LIMIT 5";
                $result = mysqli_query($conn, $sql);

                if(mysqli_num_rows($result) > 0) {
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");

                    $data_laporan = array();
                    while ($data = mysqli_fetch_assoc($result)) {
                        $tgl_laporan = $data['tgl_laporan'];
                        $uang_masuk = $data['uang_masuk'];
                        $uang_keluar = $data['uang_keluar'];
                        $entry = array("x" => $tgl_laporan, "y1" => $uang_masuk, "y2" => $uang_keluar);
                        array_push($data_laporan, $entry);
                    }

                    echo json_encode($data_laporan);
                } else {
                    $data_laporan = array();
                    echo json_encode($data_laporan);
                }
                break;
            case 'insert':
                $nama_kegiatan = $_POST['nama_kegiatan'];

                // kd_kegiatan
                $query = mysqli_query($conn, "SELECT kd_kegiatan FROM kegiatan WHERE nama_kegiatan = '$nama_kegiatan'");
                $data = mysqli_fetch_array($query);
                $kd_kegiatan = $data['kd_kegiatan'];

                // kd_laporan
                $query = mysqli_query($conn, "SELECT max(kd_laporan) AS id_terbesar FROM laporan");
                $data = mysqli_fetch_array($query);
                $kd_laporan = $data['id_terbesar'];
                $urut = (int) substr($kd_laporan, 3);
                $urut++;
                $depan = "LP";
                $kode_laporan = $depan . sprintf("%06s", $urut);

                // total kas
                $sql = "SELECT total FROM laporan ORDER BY kd_laporan DESC limit 1";
                $result = mysqli_query($conn, $sql);
                $data = mysqli_fetch_array($result);
                if ($data) {
                    $total = $data['total'];
                } else {
                    // Penanganan kesalahan jika data tidak ditemukan
                    $total = 0; // Set total ke 0 jika tidak ada data sebelumnya
                }

                $uang_masuk = $_POST['uang_masuk'];
                $uang_keluar = $_POST['uang_keluar'];
                $keterangan = $_POST['keterangan'];
                $sisa = $_POST['sisa'];
                $total_akhir = $total + $sisa;
                $imstr = $_POST['image'];
                $file = $_POST['file'];
                $path = "evidence/";

                $sql = "INSERT INTO laporan(kd_laporan, kd_kegiatan, tgl_laporan, uang_masuk, uang_keluar, total, keterangan, bukti_bayar)
                        VALUES('$kode_laporan','$kd_kegiatan',NOW(),'$uang_masuk','$uang_keluar','$total_akhir','$keterangan','$file')";
                $result = mysqli_query($conn, $sql);
                if(file_put_contents($path.$file, base64_decode($imstr))==false){
                    $respon['respon']= "0";
                    echo json_encode($respon);
                    exit();
                } else {
                    $respon['respon']= "1";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'detail':
                $kd_laporan = $_POST['kd_laporan'];

                $sql = "SELECT kd_laporan, nama_kegiatan, uang_masuk, uang_keluar, (uang_masuk-uang_keluar) AS sisa, total, keterangan, tgl_laporan,
                    CONCAT('$http_evd', bukti_bayar) AS img_bayar
                    FROM laporan NATURAL JOIN kegiatan WHERE kd_laporan = '$kd_laporan'";
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
            case 'edit':
                $kd_laporan = $_POST['kd_laporan'];

                // total kas
                $sql = "SELECT total FROM laporan ORDER BY kd_laporan DESC LIMIT 1 OFFSET 1;";
                $result = mysqli_query($conn,$sql);
                $data = mysqli_fetch_array($result);
                $total_akhir = $data['total'];

                $uang_masuk = $_POST['uang_masuk'];
                $uang_keluar = $_POST['uang_keluar'];
                $keterangan = $_POST['keterangan'];
                $sisa = $_POST['sisa'];

                $sql = "UPDATE laporan SET tgl_laporan = NOW(), uang_masuk = '$uang_masuk', uang_keluar = '$uang_keluar', keterangan = '$keterangan', 
                    total = '$total_akhir' + '$sisa' WHERE kd_laporan = '$kd_laporan'";
                $result = mysqli_query($conn,$sql);
                if ($result) {
                    $respon['respon']= "1";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'delete':
                $kd_laporan = $_POST['kd_laporan'];

                $sql = "DELETE FROM laporan WHERE kd_laporan = '$kd_laporan'";
                $result = mysqli_query($conn,$sql);
                if ($result) {
                    $respon['respon']= "1";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'coba':
                // total kas
                $sql = "SELECT total FROM laporan ORDER BY kd_laporan DESC limit 1";
                $result = mysqli_query($conn,$sql);
                $data = mysqli_fetch_array($result);
                $total = $data['total'];

                echo json_encode($total);
                break;
        }
    }

?>