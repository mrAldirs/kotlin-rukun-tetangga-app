<?php 

    $server = "localhost";
    $username = "root";
    $password = "";
    $database = "ert";

    $conn = mysqli_connect($server, $username, $password, $database);

    $local = 'http://192.168.137.1/api_ert/';

    $http_img = $local . 'image/';
    $http_kel = $local . 'keluhan/';
    $http_doc = $local . 'doc/';
    $http_evd = $local . 'evidence/';
?>