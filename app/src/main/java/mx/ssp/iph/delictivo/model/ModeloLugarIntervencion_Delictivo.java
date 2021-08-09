package mx.ssp.iph.delictivo.model;

public class ModeloLugarIntervencion_Delictivo {
    private String IdHechoDelictivo;
    private String IdEntidadFederativa;
    private String IdMunicipio;
    private String ColoniaLocalidad;
    private String CalleTramo;
    private String NoExterior;
    private String NoInterior;
    private String Cp;
    private String Referencia;
    private String Latitud;
    private String Longitud;
    private String RutaCroquis;
    private String RealizoInspeccion;
    private String AnexoObjetosRelacionados;
    private String PreservoLugar;
    private String PriorizacionIntervencion;
    private String TipoRiesgoPresentado;
    private String DesTipoRiesgoPresentado;

    public ModeloLugarIntervencion_Delictivo(String idHechoDelictivo, String idEntidadFederativa, String idMunicipio, String coloniaLocalidad,
                                             String calleTramo, String noExterior, String noInterior, String cp, String referencia, String latitud,
                                             String longitud, String rutaCroquis, String realizoInspeccion, String anexoObjetosRelacionados,
                                             String preservoLugar, String priorizacionIntervencion, String tipoRiesgoPresentado, String desTipoRiesgoPresentado) {
        IdHechoDelictivo = idHechoDelictivo;
        IdEntidadFederativa = idEntidadFederativa;
        IdMunicipio = idMunicipio;
        ColoniaLocalidad = coloniaLocalidad;
        CalleTramo = calleTramo;
        NoExterior = noExterior;
        NoInterior = noInterior;
        Cp = cp;
        Referencia = referencia;
        Latitud = latitud;
        Longitud = longitud;
        RutaCroquis = rutaCroquis;
        RealizoInspeccion = realizoInspeccion;
        AnexoObjetosRelacionados = anexoObjetosRelacionados;
        PreservoLugar = preservoLugar;
        PriorizacionIntervencion = priorizacionIntervencion;
        TipoRiesgoPresentado = tipoRiesgoPresentado;
        DesTipoRiesgoPresentado = desTipoRiesgoPresentado;
    }

    public String getIdHechoDelictivo() {
        return IdHechoDelictivo;
    }

    public void setIdHechoDelictivo(String idHechoDelictivo) {
        IdHechoDelictivo = idHechoDelictivo;
    }

    public String getIdEntidadFederativa() {
        return IdEntidadFederativa;
    }

    public void setIdEntidadFederativa(String idEntidadFederativa) {
        IdEntidadFederativa = idEntidadFederativa;
    }

    public String getIdMunicipio() {
        return IdMunicipio;
    }

    public void setIdMunicipio(String idMunicipio) {
        IdMunicipio = idMunicipio;
    }

    public String getColoniaLocalidad() {
        return ColoniaLocalidad;
    }

    public void setColoniaLocalidad(String coloniaLocalidad) {
        ColoniaLocalidad = coloniaLocalidad;
    }

    public String getCalleTramo() {
        return CalleTramo;
    }

    public void setCalleTramo(String calleTramo) {
        CalleTramo = calleTramo;
    }

    public String getNoExterior() {
        return NoExterior;
    }

    public void setNoExterior(String noExterior) {
        NoExterior = noExterior;
    }

    public String getNoInterior() {
        return NoInterior;
    }

    public void setNoInterior(String noInterior) {
        NoInterior = noInterior;
    }

    public String getCp() {
        return Cp;
    }

    public void setCp(String cp) {
        Cp = cp;
    }

    public String getReferencia() {
        return Referencia;
    }

    public void setReferencia(String referencia) {
        Referencia = referencia;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    public String getRutaCroquis() {
        return RutaCroquis;
    }

    public void setRutaCroquis(String rutaCroquis) {
        RutaCroquis = rutaCroquis;
    }

    public String getRealizoInspeccion() {
        return RealizoInspeccion;
    }

    public void setRealizoInspeccion(String realizoInspeccion) {
        RealizoInspeccion = realizoInspeccion;
    }

    public String getAnexoObjetosRelacionados() {
        return AnexoObjetosRelacionados;
    }

    public void setAnexoObjetosRelacionados(String anexoObjetosRelacionados) {
        AnexoObjetosRelacionados = anexoObjetosRelacionados;
    }

    public String getPreservoLugar() {
        return PreservoLugar;
    }

    public void setPreservoLugar(String preservoLugar) {
        PreservoLugar = preservoLugar;
    }

    public String getPriorizacionIntervencion() {
        return PriorizacionIntervencion;
    }

    public void setPriorizacionIntervencion(String priorizacionIntervencion) {
        PriorizacionIntervencion = priorizacionIntervencion;
    }

    public String getTipoRiesgoPresentado() {
        return TipoRiesgoPresentado;
    }

    public void setTipoRiesgoPresentado(String tipoRiesgoPresentado) {
        TipoRiesgoPresentado = tipoRiesgoPresentado;
    }

    public String getDesTipoRiesgoPresentado() {
        return DesTipoRiesgoPresentado;
    }

    public void setDesTipoRiesgoPresentado(String desTipoRiesgoPresentado) {
        DesTipoRiesgoPresentado = desTipoRiesgoPresentado;
    }

}
