package mx.ssp.iph.administrativo.model;

public class ModelLugarIntervencion_Administrativo {
    private String IdFaltaAdmin;
    private String IdLugar;
    private String IdEntidadFederativa;
    private String IdMunicipio;
    private String IdColoniaLocalidad;
    private String CalleTramo;
    private String NoExterior;
    private String NoInterior;
    private String Cp;
    private String Latitud;
    private String Longitud;

    public ModelLugarIntervencion_Administrativo(String idFaltaAdmin,String idEntidadFederativa, String idMunicipio, String idColoniaLocalidad,
                                                 String calleTramo, String noExterior, String noInterior, String cp, String referencia, String latitud, String longitud) {
        IdFaltaAdmin = idFaltaAdmin;
        IdEntidadFederativa = idEntidadFederativa;
        IdMunicipio = idMunicipio;
        IdColoniaLocalidad = idColoniaLocalidad;
        CalleTramo = calleTramo;
        NoExterior = noExterior;
        NoInterior = noInterior;
        Cp = cp;
        Referencia = referencia;
        Latitud = latitud;
        Longitud = longitud;
    }


    public void setIdFaltaAdmin(String idFaltaAdmin) {
        IdFaltaAdmin = idFaltaAdmin;
    }

    public void setIdLugar(String idLugar) {
        IdLugar = idLugar;
    }

    public void setIdEntidadFederativa(String idEntidadFederativa) {
        IdEntidadFederativa = idEntidadFederativa;
    }

    public void setIdMunicipio(String idMunicipio) {
        IdMunicipio = idMunicipio;
    }

    public void setIdColoniaLocalidad(String idColoniaLocalidad) {
        IdColoniaLocalidad = idColoniaLocalidad;
    }

    public void setCalleTramo(String calleTramo) {
        CalleTramo = calleTramo;
    }

    public void setNoExterior(String noExterior) {
        NoExterior = noExterior;
    }

    public void setNoInterior(String noInterior) {
        NoInterior = noInterior;
    }

    public void setCp(String cp) {
        Cp = cp;
    }

    public void setReferencia(String referencia) {
        Referencia = referencia;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    private String Referencia;

    public String getIdFaltaAdmin() {
        return IdFaltaAdmin;
    }

    public String getIdLugar() {
        return IdLugar;
    }

    public String getIdEntidadFederativa() {
        return IdEntidadFederativa;
    }

    public String getIdMunicipio() {
        return IdMunicipio;
    }

    public String getIdColoniaLocalidad() {
        return IdColoniaLocalidad;
    }

    public String getCalleTramo() {
        return CalleTramo;
    }

    public String getNoExterior() {
        return NoExterior;
    }

    public String getNoInterior() {
        return NoInterior;
    }

    public String getCp() {
        return Cp;
    }

    public String getReferencia() {
        return Referencia;
    }

    public String getLatitud() {
        return Latitud;
    }

    public String getLongitud() {
        return Longitud;
    }




}

