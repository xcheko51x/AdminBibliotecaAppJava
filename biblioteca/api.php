<?php

/*

LISTA DE ACCIONES

ACCIONES USUARIOS
    200 - Obtener usuario login
    201 - Obtener todos los usuarios
    202 - Obtener usuario por nombre o id
    203 - Agregar usuario
    204 - Editar usuario usando id
    205 - Borrar usuario usando id
    206 - Obtener usuarios con adeudos

ACCIONES AUTORES
    301 - Obtener todos los autores
    302 - Obtener autor por nombre o id
    303 - Agregar autor
    304 - Editar autor usando id
    305 - Borrar autor usando id

ACCIONES EDITORIALES
    401 - Obtener todas las editoriales
    402 - Obtener editorial por nombre o id
    403 - Agregar editorial
    404 - Editar editorial usando id
    405 - Borrar editorial usando id

ACCIONES CATEGORIAS
    501 - Obtener todas las categorias
    502 - Obtener categoria por nombre o id
    503 - Agregar categoria
    504 - Editar categoria usando id
    505 - Borrar categoria usando id

ACCIONES LIBROS
    601 - Obtener todos los libros

ACCIONES PRESTAMOS
    701 - Obtener prestamos
    
    703 - Agregar prestamo
    706 - Devolver prestamo


CODIGOS ERROR
    Ninguna accion solicitada
    No se recibio ningun valor para la busqueda del usuario
    Solo debes usar un valor para la busqueda del usuario (id o nombre)
    No se recibieron los datos del nuevo usuario
    No se inserto el nuevo usuario
    No se recibio ningun valor para la busqueda del autor
    Solo debes usar un valor para la busqueda del autor (id o nombre)
    No se recibieron los datos del nuevo autor
    No se inserto el nuevo autor
    No se recibio ningun valor para la busqueda de la editorial
    Solo debes usar un valor para la busqueda de la editorial (id o nombre)
    No se recibieron los datos de la nueva editorial
    No se inserto la nueva editorial
    No se recibio ningun valor para la busqueda de la categoria
    Solo debes usar un valor para la busqueda de la categoria (id o nombre)
    No se recibieron los datos de la nueva categoria
    No se inserto la nueva categoria
    No existe o no coinciden los datos de usuario y contraseña

*/

header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Headers: X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Request-Method");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
header("Allow: GET, POST, OPTIONS, PUT, DELETE");
error_reporting(0);

require 'conexion.php';

$accion = $_POST['accion'];

date_default_timezone_set('America/Mexico_City');

if (empty($accion) || is_null($accion)) {
    echo json_encode(array("codigo" => "ERROR", "mensaje" => "Ninguna accion solicitada"));
}

// OBTENER USUARIO ADMIN LOGIN
else if ($accion == 200) {

    $idUsuario = $_POST['usuario'];
    $contrasena = $_POST['contrasena'];

    if (
        (empty($idUsuario) || is_null($idUsuario)) &&
        (empty($contrasena) || is_null($contrasena))
    ) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibio ningun valor para la busqueda del usuario"));
    } else {

        $sql = "SELECT * FROM adminusuarios WHERE lower(id_usuario) = '" . $idUsuario . "' AND contrasena = '" . $contrasena . "';";
        $query = $mysqli->query($sql);
        $data = array();
        while ($res = $query->fetch_assoc()) {
            $data[] = $res;
        }

        if (sizeof($data) == 0) {
            echo json_encode(array("codigo" => "ERROR", "mensaje" => "No existe o no coinciden los datos de usuario y contrasena"));
        } else {
            echo json_encode(array("codigo" => "OK", "usuarios" => $data));
        }
    }
}

// OBTENER USUARIOS
if ($accion == 201) {

    $sql = "SELECT * FROM usuarios;";
    $query = $mysqli->query($sql);

    $data = array();
    while ($res = $query->fetch_assoc()) {
        $data[] = $res;
    }

    echo json_encode(array("codigo" => "OK", "usuarios" => $data));
}

// OBTENER USUARIO POR ID O POR NOMBRE
else if ($accion == 202) {

    $nomUsuario = $_POST['nom_usuario'];
    $idUsuario = $_POST['id_usuario'];

    if (
        (empty($nomUsuario) || is_null($nomUsuario)) &&
        (empty($idUsuario) || is_null($idUsuario))
    ) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibio ningun valor para la busqueda del usuario"));
    } else {

        $sql = "";

        if ((empty($idUsuario) || is_null($idUsuario))) {
            $sql = "SELECT * FROM usuarios WHERE lower(nom_usuario) LIKE '%" . $nomUsuario . "%';";
        } else if ((empty($nomUsuario) || is_null($nomUsuario))) {
            $sql = "SELECT * FROM usuarios WHERE id_usuario LIKE '" . $idUsuario . "';";
        } else {
            echo json_encode(array("codigo" => "ERROR", "mensaje" => "Solo debes usar un valor para la busqueda del usuario (id o nombre)"));
        }

        $query = $mysqli->query($sql);
        $data = array();
        while ($res = $query->fetch_assoc()) {
            $data[] = $res;
        }

        echo json_encode(array("codigo" => "OK", "usuarios" => $data));
    }
}

// AGREGAR USUARIO
else if ($accion == 203) {

    $idUsuario = $_POST['id_usuario'];
    $nomUsuario = $_POST['nom_usuario'];
    $contrasena = $_POST['contrasena'];

    if (
        (empty($idUsuario) || is_null($idUsuario)) ||
        (empty($nomUsuario) || is_null($nomUsuario)) ||
        (empty($contrasena) || is_null($contrasena))
    ) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibieron los datos del nuevo usuario"));
    } else {

        $sql = "INSERT INTO usuarios(id_usuario, nom_usuario, contrasena) VALUES('" . $idUsuario . "', '" . $nomUsuario . "', '" . $contrasena . "');";
        if ($query = $mysqli->query($sql)) {

            echo json_encode(array("codigo" => "OK", "mensaje" => "Se agrego el usuario exitosamente"));
        } else {

            echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se inserto el nuevo usuario"));
        }
    }
}

// EDITAR USUARIO
else if ($accion == 204) {

    $idUsuario = $_POST['id_usuario'];
    $nomUsuario = $_POST['nom_usuario'];
    $contrasena = $_POST['contrasena'];

    if (
        (empty($idUsuario) || is_null($idUsuario)) ||
        (empty($nomUsuario) || is_null($nomUsuario)) ||
        (empty($contrasena) || is_null($contrasena))
    ) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibieron los datos del usuario"));
    } else {

        $sql = "UPDATE usuarios SET nom_usuario='" . $nomUsuario . "', contrasena='" . $contrasena . "' WHERE id_usuario='" . $idUsuario . "';";
        if ($query = $mysqli->query($sql)) {

            echo json_encode(array("codigo" => "OK", "mensaje" => "Se actualizo el usuario exitosamente"));
        } else {

            echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se actualizo el usuario"));
        }
    }
}

// BORRAR USUARIO
else if ($accion == 205) {

    $idUsuario = $_POST['id_usuario'];

    if (empty($idUsuario) || is_null($idUsuario)) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibi el ID del usuario"));
    } else {

        $sql = "DELETE FROM usuarios WHERE id_usuario='" . $idUsuario . "';";
        if ($query = $mysqli->query($sql)) {

            echo json_encode(array("codigo" => "OK", "mensaje" => "Se borro el usuario exitosamente"));
        } else {

            echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se pudo borrar el usuario"));
        }
    }
}

// OBTENER USUARIO CON ADEUDOS
else if ($accion == 206) {

    $sql = "SELECT * FROM usuarios WHERE estado_usuario='deudor';";
    $query = $mysqli->query($sql);
    $data = array();
    while ($res = $query->fetch_assoc()) {
        $data[] = $res;
    }

    echo json_encode(array("codigo" => "OK", "usuarios_deudores" => $data));
}

// OBTENER AUTORES
if ($accion == 301) {

    $sql = "SELECT * FROM autores ORDER BY nom_autor ASC;";
    $query = $mysqli->query($sql);

    $data = array();
    while ($res = $query->fetch_assoc()) {
        $data[] = $res;
    }

    echo json_encode(array("codigo" => "OK", "autores" => $data));
}

// OBTENER AUTOR POR ID O POR NOMBRE
else if ($accion == 302) {

    $idAutor = $_POST['id_autor'];
    $nomAutor = $_POST['nom_autor'];


    if (
        (empty($idAutor) || is_null($idAutor)) &&
        (empty($nomAutor) || is_null($nomAutor))
    ) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibio ningun valor para la busqueda del autor"));
    } else {

        $sql = "";

        if ((empty($idAutor) || is_null($idAutor))) {
            $sql = "SELECT * FROM autores WHERE lower(nom_autor) LIKE '%" . $nomAutor . "%' ORDER BY DESC;";
        } else if ((empty($nomAutor) || is_null($nomAutor))) {
            $sql = "SELECT * FROM autores WHERE id_autor LIKE '" . $idAutor . "' ORDER BY DESC;";
        } else {
            echo json_encode(array("codigo" => "ERROR", "mensaje" => "Solo debes usar un valor para la busqueda del autor (id o nombre)"));
        }

        $query = $mysqli->query($sql);
        $data = array();
        while ($res = $query->fetch_assoc()) {
            $data[] = $res;
        }

        echo json_encode(array("codigo" => "OK", "autores" => $data));
    }
}

// AGREGAR AUTOR
else if ($accion == 303) {

    $idAutor = $_POST['id_autor'];
    $nomAutor = $_POST['nom_autor'];

    if (
        (empty($idAutor) || is_null($idAutor)) ||
        (empty($nomAutor) || is_null($nomAutor))
    ) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibieron los datos del nuevo autor"));
    } else {

        $sql = "INSERT INTO autores VALUES('" . $idAutor . "', '" . $nomAutor . "');";
        if ($query = $mysqli->query($sql)) {

            echo json_encode(array("codigo" => "OK", "mensaje" => "Se agrego el autor exitosamente"));
        } else {

            echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se inserto el nuevo autor"));
        }
    }
}

// EDITAR AUTOR
else if ($accion == 304) {

    $idAutor = $_POST['id_autor'];
    $nomAutor = $_POST['nom_autor'];

    if (
        (empty($idAutor) || is_null($idAutor)) ||
        (empty($nomAutor) || is_null($nomAutor))
    ) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibieron los datos del autor"));
    } else {

        $sql = "UPDATE autores SET nom_autor='" . $nomAutor . "' WHERE id_autor='" . $idAutor . "';";
        if ($query = $mysqli->query($sql)) {

            echo json_encode(array("codigo" => "OK", "mensaje" => "Se actualizo el autor exitosamente"));
        } else {

            echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se actualizo el autor"));
        }
    }
}

// BORRAR AUTOR
else if ($accion == 305) {

    $idAutor = $_POST['id_autor'];

    if (empty($idAutor) || is_null($idAutor)) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibi el ID del autor"));
    } else {

        $sql = "DELETE FROM autores WHERE id_autor='" . $idAutor . "';";
        if ($query = $mysqli->query($sql)) {

            echo json_encode(array("codigo" => "OK", "mensaje" => "Se borro el autor exitosamente"));
        } else {

            echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se pudo borrar el autor"));
        }
    }
}

// OBTENER EDITORIALES
if ($accion == 401) {

    $sql = "SELECT * FROM editoriales;";
    $query = $mysqli->query($sql);

    $data = array();
    while ($res = $query->fetch_assoc()) {
        $data[] = $res;
    }

    echo json_encode(array("codigo" => "OK", "editoriales" => $data));
}

// OBTENER EDITORIAL POR ID O POR NOMBRE
else if ($accion == 402) {

    $idEditorial = $_POST['id_editorial'];
    $nomEditorial = $_POST['nom_editorial'];


    if (
        (empty($idEditorial) || is_null($idEditorial)) &&
        (empty($nomEditorial) || is_null($nomEditorial))
    ) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibio ningun valor para la busqueda de la editorial"));
    } else {

        $sql = "";

        if ((empty($idEditorial) || is_null($idEditorial))) {
            $sql = "SELECT * FROM editoriales WHERE lower(nom_editorial) LIKE '%" . $nomEditorial . "%';";
        } else if ((empty($nomAutor) || is_null($nomAutor))) {
            $sql = "SELECT * FROM editoriales WHERE id_editorial LIKE '" . $idEditorial . "';";
        } else {
            echo json_encode(array("codigo" => "ERROR", "mensaje" => "Solo debes usar un valor para la busqueda de la editorial (id o nombre)"));
        }

        $query = $mysqli->query($sql);
        $data = array();
        while ($res = $query->fetch_assoc()) {
            $data[] = $res;
        }

        echo json_encode(array("codigo" => "OK", "editoriales" => $data));
    }
}

// AGREGAR EDITORIAL
else if ($accion == 403) {

    $idEditorial = $_POST['id_editorial'];
    $nomEditorial = $_POST['nom_editorial'];

    if (
        (empty($idEditorial) || is_null($idEditorial)) ||
        (empty($nomEditorial) || is_null($nomEditorial))
    ) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibieron los datos de la nueva editorial"));
    } else {

        $sql = "INSERT INTO editoriales VALUES('" . $idEditorial . "', '" . $nomEditorial . "');";
        if ($query = $mysqli->query($sql)) {

            echo json_encode(array("codigo" => "OK", "mensaje" => "Se agrego la editorial exitosamente"));
        } else {

            echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se inserto la nueva editorial"));
        }
    }
}

// EDITAR EDITORIAL
else if ($accion == 404) {

    $idEditorial = $_POST['id_editorial'];
    $nomEditorial = $_POST['nom_editorial'];

    if (
        (empty($idEditorial) || is_null($idEditorial)) ||
        (empty($nomEditorial) || is_null($nomEditorial))
    ) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibieron los datos del autor"));
    } else {

        $sql = "UPDATE autores SET nom_autor='" . $nomEditorial . "' WHERE id_autor='" . $idEditorial . "';";
        if ($query = $mysqli->query($sql)) {

            echo json_encode(array("codigo" => "OK", "mensaje" => "Se actualizo la editorial exitosamente"));
        } else {

            echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se actualizo la editorial"));
        }
    }
}

// BORRAR EDITORIAL
else if ($accion == 405) {

    $idEditorial = $_POST['id_editorial'];

    if (empty($idEditorial) || is_null($idEditorial)) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibi el ID de la editorial"));
    } else {

        $sql = "DELETE FROM editoriales WHERE id_editorial='" . $idEditorial . "';";
        if ($query = $mysqli->query($sql)) {

            echo json_encode(array("codigo" => "OK", "mensaje" => "Se borro la editorial exitosamente"));
        } else {

            echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se pudo borrar la editorial"));
        }
    }
}

// OBTENER CATEGORIAS
if ($accion == 501) {

    $sql = "SELECT * FROM categorias;";
    $query = $mysqli->query($sql);

    $data = array();
    while ($res = $query->fetch_assoc()) {
        $data[] = $res;
    }

    echo json_encode(array("codigo" => "OK", "categorias" => $data));
}

// OBTENER CATEGORIAS POR ID O NOMBRE
else if ($accion == 502) {

    $idCategoria = $_POST['id_categoria'];
    $nomCategoria = $_POST['nom_categoria'];


    if (
        (empty($idCategoria) || is_null($idCategoria)) &&
        (empty($nomCategoria) || is_null($nomCategoria))
    ) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibio ningun valor para la busqueda de la categoria"));
    } else {

        $sql = "";

        if ((empty($idCategoria) || is_null($idCategoria))) {
            $sql = "SELECT * FROM categorias WHERE lower(nom_categoria) LIKE '%" . $nomCategoria . "%';";
        } else if ((empty($nomCategoria) || is_null($nomCategoria))) {
            $sql = "SELECT * FROM categorias WHERE id_categoria LIKE '" . $idCategoria . "';";
        } else {
            echo json_encode(array("codigo" => "ERROR", "mensaje" => "Solo debes usar un valor para la busqueda de la categoria (id o nombre)"));
        }

        $query = $mysqli->query($sql);
        $data = array();
        while ($res = $query->fetch_assoc()) {
            $data[] = $res;
        }

        echo json_encode(array("codigo" => "OK", "editoriales" => $data));
    }
}

// AGREGAR CATEGORIA
else if ($accion == 503) {

    $idCategoria = $_POST['id_categoria'];
    $nomCategoria = $_POST['nom_categoria'];

    if (
        (empty($idCategoria) || is_null($idCategoria)) ||
        (empty($nomCategoria) || is_null($nomCategoria))
    ) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibieron los datos de la nueva categoria"));
    } else {

        $sql = "INSERT INTO categorias VALUES('" . $idCategoria . "', '" . $nomCategoria . "');";
        if ($query = $mysqli->query($sql)) {

            echo json_encode(array("codigo" => "OK", "mensaje" => "Se agrego la categoria exitosamente"));
        } else {

            echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se inserto la nueva categoria"));
        }
    }
}

// EDITAR CATEGORIA
else if ($accion == 504) {

    $idCategoria = $_POST['id_categoria'];
    $nomCategoria = $_POST['nom_categoria'];

    if (
        (empty($idCategoria) || is_null($idCategoria)) ||
        (empty($nomCategoria) || is_null($nomCategoria))
    ) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibieron los datos de la categoria"));
    } else {

        $sql = "UPDATE categorias SET nom_categoria='" . $nomCategoria . "' WHERE id_categoria='" . $idCategoria . "';";
        if ($query = $mysqli->query($sql)) {

            echo json_encode(array("codigo" => "OK", "mensaje" => "Se actualizo la categoria exitosamente"));
        } else {

            echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se actualizo la categoria"));
        }
    }
}

// BORRAR CATEGORIA
else if ($accion == 505) {

    $idCategoria = $_POST['id_categoria'];

    if (empty($idCategoria) || is_null($idCategoria)) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibi el ID de la categoria"));
    } else {

        $sql = "DELETE FROM categorias WHERE id_categoria='" . $idCategoria . "';";
        if ($query = $mysqli->query($sql)) {

            echo json_encode(array("codigo" => "OK", "mensaje" => "Se borro la categoria exitosamente"));
        } else {

            echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se pudo borrar la categoria"));
        }
    }
}

// OBTENER LIBROS
if ($accion == 601) {

    $sql = "SELECT * FROM libros;";
    $query = $mysqli->query($sql);

    $data = array();
    while ($res = $query->fetch_assoc()) {
        $data[] = $res;
    }

    echo json_encode(array("codigo" => "OK", "libros" => $data));
}

// AGREGAR LIBRO
else if ($accion == 603) {

    $isbn = $_POST['isbn'];
    $nomLibro = $_POST['nom_libro'];
    $nomAutor = $_POST['nom_autor'];
    $descripcion = $_POST['descripcion'];
    $editorial = $_POST['nom_editorial'];
    $nomCategoria = $_POST['nom_categoria'];
    $anioPublicacion = $_POST['anio_publicacion'];
    $edicion = $_POST['edicion'];
    $existencias = $_POST['existencias'];

    if (
        (empty($isbn) || is_null($isbn)) ||
        (empty($nomLibro) || is_null($nomLibro)) ||
        (empty($nomAutor) || is_null($nomAutor)) ||
        (empty($editorial) || is_null($editorial)) ||
        (empty($nomCategoria) || is_null($nomCategoria)) ||
        (empty($anioPublicacion) || is_null($anioPublicacion)) ||
        (empty($edicion) || is_null($edicion)) ||
        (empty($existencias) || is_null($existencias))
    ) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibieron los datos del libro"));
    } else {

        $portada = $isbn . ".png";

        $sql = "INSERT INTO libros VALUES(
            '" . $isbn . "', 
            '" . $portada . "', 
            '" . $nomLibro . "', 
            '" . $nomAutor . "', 
            '" . $descripcion . "', 
            '" . $editorial . "', 
            '" . $anioPublicacion . "', 
            '" . $edicion . "', 
            '" . $existencias . "', 
            '" . $nomCategoria . "'
        );";
        if ($query = $mysqli->query($sql)) {

            echo json_encode(array("codigo" => "OK", "mensaje" => "Se agrego el libro exitosamente"));
        } else {

            echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se inserto el libro"));
        }
    }
}

// OBTENER PRESTAMOS
if ($accion == 701) {

    $sql = "
    SELECT 
        libros.isbn, 
        libros.nom_libro, 
        libros.autor, 
        libros.editorial, 
        libros.anio_publicacion, 
        libros.edicion,
        usuarios.id_usuario,
        usuarios.nom_usuario,
        usuarios.estado_usuario,
        prestamos.fecha_prestamo,
        prestamos.fecha_devolucion 
    FROM libros, usuarios, prestamos
    WHERE 
        libros.isbn = prestamos.isbn and
        usuarios.id_usuario = prestamos.id_usuario ORDER BY nom_usuario ASC;";

    $query = $mysqli->query($sql);

    $data = array();
    while ($res = $query->fetch_assoc()) {
        $data[] = $res;
    }

    echo json_encode(array("codigo" => "OK", "prestamos" => $data));
}

// AGREGAR PRESTAMO
else if ($accion == 703) {
    $isbn = $_POST['isbn'];
    $idUsuario = $_POST['id_usuario'];

    if (
        (empty($isbn) || is_null($isbn)) ||
        (empty($idUsuario) || is_null($idUsuario))
    ) {
        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibieron los datos del prestamo"));
    } else {

        $hoy = date("Y-m-d H:i:s");
        //echo "HOY: ".$hoy;
        $diasMas = date("Y-m-d H:i:s", strtotime($hoy . "+ 3 days"));
        //echo "3 DIAS MAS: ".$diasMas;


        $sql = "update libros set existencias=existencias-1 where isbn='" . $isbn . "';";
        if ($query = $mysqli->query($sql)) {

            $sql2 = "insert into prestamos values('" . $isbn . "', '" . $idUsuario . "', '" . $hoy . "', '" . $diasMas . "');";
            if ($query = $mysqli->query($sql2)) {

                $sql3 = "update usuarios set estado_usuario = 'deudor' where id_usuario='" . $idUsuario . "';";
                if ($query = $mysqli->query($sql3)) {
                    echo json_encode(array("codigo" => "OK", "mensaje" => "Se agrego el prestamo exitosamente"));
                } else {

                    echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se inserto el prestamo"));
                }
            } else {

                echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se inserto el prestamo"));
            }
        } else {

            echo json_encode(array("codigo" => "ERROR", "mensaje" => "Ocurrio un error"));
        }
    }
}

// DEVOLVER PRESTAMO
else if($accion == 706) {

    $idUsuario = $_POST['id_usuario'];
    $isbn = $_POST['isbn'];
    
    if (
        (empty($idUsuario) || is_null($idUsuario)) ||
        (empty($isbn) || is_null($isbn))
    ) {

        echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se recibieron los datos de la devolucion"));

    } else {

        $sql = "update libros set existencias=existencias+1 where isbn='" . $isbn . "';";
        if ($query = $mysqli->query($sql)) {

            $sql2 = "update usuarios set estado_usuario=NULL where id_usuario='" . $idUsuario . "';";
            if ($query = $mysqli->query($sql2)) {

                $sql3 = "delete from prestamos where isbn='" . $isbn . "' and id_usuario='" . $idUsuario . "';";
                if ($query = $mysqli->query($sql3)) {
                    echo json_encode(array("codigo" => "OK", "mensaje" => "Se registro la devolucion exitosamente"));
                } else {

                    echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se registro la devolucion"));
                }
            } else {

                echo json_encode(array("codigo" => "ERROR", "mensaje" => "No se registro la devolucion"));
            }
        } else {

            echo json_encode(array("codigo" => "ERROR", "mensaje" => "Ocurrio un error"));
        }
    }
}
