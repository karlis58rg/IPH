package mx.ssp.iph.administrativo.model;

public class ModeloDetenciones_Administrativo {
    private String IdFaltaAdmin;
    private String IdDetenido;
    private String NumDetencion;
    private String Fecha;
    private String Hora;
    private String APDetenido;
    private String AMDetenido;
    private String NomDetenido;
    private String ApodoAlias;
    private String DescripcionDetenido;
    private String IdNacionalidad;
    private String IdSexo;
    private String FechaNacimiento;
    private String UrlFirmaDetenido;
    private String IdEntidadFederativa;
    private String IdMunicipio;
    private String ColoniaLocalidad;
    private String CalleTramo;
    private String NoExterior;
    private String NoInterior;
    private String Cp;
    private String Referencia;
    private String Lesiones;
    private String Padecimientos;
    private String DescPadecimientos;
    private String GrupoVulnerable;
    private String DescGrupoVulnerable;
    private String IdLugarTraslado;
    private String IdPoliciaPrimerRespondiente;

    public void setIdFaltaAdmin(String idFaltaAdmin) {
        IdFaltaAdmin = idFaltaAdmin;
    }

    public void setIdDetenido(String idDetenido) {
        IdDetenido = idDetenido;
    }

    public void setNumDetencion(String numDetencion) {
        NumDetencion = numDetencion;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public void setAPDetenido(String APDetenido) {
        this.APDetenido = APDetenido;
    }

    public void setAMDetenido(String AMDetenido) {
        this.AMDetenido = AMDetenido;
    }

    public void setNomDetenido(String nomDetenido) {
        NomDetenido = nomDetenido;
    }

    public void setApodoAlias(String apodoAlias) {
        ApodoAlias = apodoAlias;
    }

    public void setDescripcionDetenido(String descripcionDetenido) {
        DescripcionDetenido = descripcionDetenido;
    }

    public void setIdNacionalidad(String idNacionalidad) {
        IdNacionalidad = idNacionalidad;
    }

    public void setIdSexo(String idSexo) {
        IdSexo = idSexo;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        FechaNacimiento = fechaNacimiento;
    }

    public void setUrlFirmaDetenido(String urlFirmaDetenido) {
        UrlFirmaDetenido = urlFirmaDetenido;
    }

    public void setIdEntidadFederativa(String idEntidadFederativa) {
        IdEntidadFederativa = idEntidadFederativa;
    }

    public void setIdMunicipio(String idMunicipio) {
        IdMunicipio = idMunicipio;
    }

    public void setColoniaLocalidad(String coloniaLocalidad) {
        ColoniaLocalidad = coloniaLocalidad;
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

    public void setLesiones(String lesiones) {
        Lesiones = lesiones;
    }

    public void setPadecimientos(String padecimientos) {
        Padecimientos = padecimientos;
    }

    public void setDescPadecimientos(String descPadecimientos) {
        DescPadecimientos = descPadecimientos;
    }

    public void setGrupoVulnerable(String grupoVulnerable) {
        GrupoVulnerable = grupoVulnerable;
    }

    public void setDescGrupoVulnerable(String descGrupoVulnerable) {
        DescGrupoVulnerable = descGrupoVulnerable;
    }

    public void setIdLugarTraslado(String idLugarTraslado) {
        IdLugarTraslado = idLugarTraslado;
    }

    public void setIdPoliciaPrimerRespondiente(String idPoliciaPrimerRespondiente) {
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
    }


    public String getIdFaltaAdmin() {
        return IdFaltaAdmin;
    }

    public String getIdDetenido() {
        return IdDetenido;
    }

    public String getNumDetencion() {
        return NumDetencion;
    }

    public String getFecha() {
        return Fecha;
    }

    public String getHora() {
        return Hora;
    }

    public String getAPDetenido() {
        return APDetenido;
    }

    public String getAMDetenido() {
        return AMDetenido;
    }

    public String getNomDetenido() {
        return NomDetenido;
    }

    public String getApodoAlias() {
        return ApodoAlias;
    }

    public String getDescripcionDetenido() {
        return DescripcionDetenido;
    }

    public String getIdNacionalidad() {
        return IdNacionalidad;
    }

    public String getIdSexo() {
        return IdSexo;
    }

    public String getFechaNacimiento() {
        return FechaNacimiento;
    }

    public String getUrlFirmaDetenido() {
        return UrlFirmaDetenido;
    }

    public String getIdEntidadFederativa() {
        return IdEntidadFederativa;
    }

    public String getIdMunicipio() {
        return IdMunicipio;
    }

    public String getColoniaLocalidad() {
        return ColoniaLocalidad;
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

    public String getLesiones() {
        return Lesiones;
    }

    public String getPadecimientos() {
        return Padecimientos;
    }

    public String getDescPadecimientos() {
        return DescPadecimientos;
    }

    public String getGrupoVulnerable() {
        return GrupoVulnerable;
    }

    public String getDescGrupoVulnerable() {
        return DescGrupoVulnerable;
    }

    public String getIdLugarTraslado() {
        return IdLugarTraslado;
    }

    public String getIdPoliciaPrimerRespondiente() {
        return IdPoliciaPrimerRespondiente;
    }

    public ModeloDetenciones_Administrativo(String idFaltaAdmin,String numDetencion,
                                            String fecha, String hora, String APDetenido, String AMDetenido,
                                            String nomDetenido, String apodoAlias, String descripcionDetenido,
                                            String idNacionalidad, String idSexo, String fechaNacimiento,
                                            String urlFirmaDetenido, String idEntidadFederativa, String idMunicipio,
                                            String coloniaLocalidad, String calleTramo, String noExterior, String noInterior,
                                            String cp, String referencia, String lesiones, String padecimientos, String descPadecimientos,
                                            String grupoVulnerable, String descGrupoVulnerable, String idLugarTraslado, String idPoliciaPrimerRespondiente) {
        IdFaltaAdmin = idFaltaAdmin;
        NumDetencion = numDetencion;
        Fecha = fecha;
        Hora = hora;
        this.APDetenido = APDetenido;
        this.AMDetenido = AMDetenido;
        NomDetenido = nomDetenido;
        ApodoAlias = apodoAlias;
        DescripcionDetenido = descripcionDetenido;
        IdNacionalidad = idNacionalidad;
        IdSexo = idSexo;
        FechaNacimiento = fechaNacimiento;
        UrlFirmaDetenido = urlFirmaDetenido;
        IdEntidadFederativa = idEntidadFederativa;
        IdMunicipio = idMunicipio;
        ColoniaLocalidad = coloniaLocalidad;
        CalleTramo = calleTramo;
        NoExterior = noExterior;
        NoInterior = noInterior;
        Cp = cp;
        Referencia = referencia;
        Lesiones = lesiones;
        Padecimientos = padecimientos;
        DescPadecimientos = descPadecimientos;
        GrupoVulnerable = grupoVulnerable;
        DescGrupoVulnerable = descGrupoVulnerable;
        IdLugarTraslado = idLugarTraslado;
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
    }
}
