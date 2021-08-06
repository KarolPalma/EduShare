package com.example.edushareproyect;

public class RestApiMehotds {
    private static final String ipaddress = "http://3.128.217.135:1880";
    public static final String ApiGetUrl = ipaddress+"/WSCurso/listaempleados.php";
    public static final String ApiPostUrl = ipaddress+"/WSCurso/crear.php";
    public static final String ApiImageUrl = ipaddress+"/WSCurso/UploadFile.php";

    /*
    * Metodos de registro
    * */

    //Comunes
    public static final String ApiPOSTSesionMail = ipaddress+"/api/sesion/mail";
    public static final String ApiGETCarreras = ipaddress+"/api/carreras/lista";
    public static final String ApiGETCampus = ipaddress+"/api/campus/lista";

    //Alumno
    public static final String ApiPOSTAlumno = ipaddress+"/api/registro/alumno";

    //Catedratico
    public static final String ApiPOSTcatedratico = ipaddress+"/api/registro/catedratico";
    public static final String ApiPOSTCrearGrupo = ipaddress+"/api/lista/crear/grupo";
    public static final String ApiPOSTListaGrupos = ipaddress+"/api/grupos/lista/catedratico";

    //Compañeros
    public static final String ApiPostFriends = ipaddress + "/api/contactos/lista";
    public static final String ApiPostInfoFriend = ipaddress + "/api/contacto/informacion";
    public static final String ApiPostInfoUser = ipaddress + "/api/usuario/informacion";
    public static final String ApiPostAddFriend = ipaddress + "/api/grupos/agregar/contacto";
    public static final String ApiPostDeleteFriend = ipaddress + "/api/contacto/eliminar";

     /**
     * Session (Controles para inicio, validacion y finalizacion de sesion)
     **/
     public static final String ApiPOSTLogin = ipaddress+"/api/session/login";

     /*
     * Interaccion con Grupos
     */
    public static final String ApiGruposUser = ipaddress + "/api/grupos/lista/usuario";
    public static final String ApiPostAdmin = ipaddress + "/api/grupos/administrador";
    public static final String ApiPostMembers = ipaddress + "/api/grupos/lista/integrantes";
    public static final String ApiPostAddMiembro = ipaddress +"/api/grupo/agregar/alumno";

}
