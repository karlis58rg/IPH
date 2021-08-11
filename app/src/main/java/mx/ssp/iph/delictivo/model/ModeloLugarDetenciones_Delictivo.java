package mx.ssp.iph.delictivo.model;

public class ModeloLugarDetenciones_Delictivo {
    private String IdHechoDelictivo;
    private String IdEntidadFederativa;
    private String IdMunicipio;
    private String ColoniaLocalidad;
    private String CalleTramo;
    private String NoExterior;
    private String NoInterior;
    private String Cp;
    private String Referencia;

    public ModeloLugarDetenciones_Delictivo(String idHechoDelictivo, String idEntidadFederativa, String idMunicipio,
                                            String coloniaLocalidad, String calleTramo, String noExterior, String noInterior, String cp,
                                            String referencia) {
        IdHechoDelictivo = idHechoDelictivo;
        IdEntidadFederativa = idEntidadFederativa;
        IdMunicipio = idMunicipio;
        ColoniaLocalidad = coloniaLocalidad;
        CalleTramo = calleTramo;
        NoExterior = noExterior;
        NoInterior = noInterior;
        Cp = cp;
        Referencia = referencia;
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

}
