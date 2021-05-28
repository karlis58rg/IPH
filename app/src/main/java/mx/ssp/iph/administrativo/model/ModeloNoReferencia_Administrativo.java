package mx.ssp.iph.administrativo.model;

public class ModeloNoReferencia_Administrativo {
    private String IdFaltaAdmin;
    private String NumReferencia;
    private String NumSistema;
    private String IdEntidadFederativa;
    private String IdMunicipio;
    private String IdInstitucion;
    private String IdGobierno;
    private String OtraAutoridad;
    private String Fecha;
    private String Hora;
    private String Usuario;

    public void setIdFaltaAdmin(String idFaltaAdmin) {
        IdFaltaAdmin = idFaltaAdmin;
    }

    public void setNumReferencia(String numReferencia) {
        NumReferencia = numReferencia;
    }

    public void setNumSistema(String numSistema) {
        NumSistema = numSistema;
    }

    public void setIdEntidadFederativa(String idEntidadFederativa) {
        IdEntidadFederativa = idEntidadFederativa;
    }

    public void setIdMunicipio(String idMunicipio) {
        IdMunicipio = idMunicipio;
    }

    public void setIdInstitucion(String idInstitucion) {
        IdInstitucion = idInstitucion;
    }

    public void setIdGobierno(String idGobierno) {
        IdGobierno = idGobierno;
    }

    public void setOtraAutoridad(String otraAutoridad) {
        OtraAutoridad = otraAutoridad;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }


    public String getIdFaltaAdmin() {
        return IdFaltaAdmin;
    }

    public String getNumReferencia() {
        return NumReferencia;
    }

    public String getNumSistema() {
        return NumSistema;
    }

    public String getIdEntidadFederativa() {
        return IdEntidadFederativa;
    }

    public String getIdMunicipio() {
        return IdMunicipio;
    }

    public String getIdInstitucion() {
        return IdInstitucion;
    }

    public String getIdGobierno() {
        return IdGobierno;
    }

    public String getOtraAutoridad() {
        return OtraAutoridad;
    }

    public String getFecha() {
        return Fecha;
    }

    public String getHora() {
        return Hora;
    }

    public String getUsuario() {
        return Usuario;
    }

    /*public ModeloNoReferencia_Administrativo(String idFaltaAdmin, String numReferencia, String numFolioInterno, String idEntidadFederativa, String idMunicipio, String idInstitucion, String Gobierno, String otraAutoridad, String fecha, String hora) {
        IdFaltaAdmin = idFaltaAdmin;
        NumReferencia = numReferencia;
        NumFolioInterno = numFolioInterno;
        IdEntidadFederativa = idEntidadFederativa;
        IdMunicipio = idMunicipio;
        IdInstitucion = idInstitucion;
        IdGobierno = idGobierno;
        OtraAutoridad = otraAutoridad;
        Fecha = fecha;
        Hora = hora;
    }*/

    public ModeloNoReferencia_Administrativo(String numReferencia,String Gobierno,String fecha, String hora) {
        NumReferencia = numReferencia;
        IdGobierno = Gobierno;
        Fecha = fecha;
        Hora = hora;
    }
}
