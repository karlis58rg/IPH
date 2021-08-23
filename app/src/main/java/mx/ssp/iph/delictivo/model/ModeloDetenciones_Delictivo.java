package mx.ssp.iph.delictivo.model;

public class ModeloDetenciones_Delictivo {


    private String IdHechoDelictivo;
    private String NumDetencionRND;
    private String Fecha;
    private String Hora;
    private String APDentenido;
    private String AMDetenido;
    private String NomDetenido;
    private String ApodoAlias;
    private String DescripcionDetenido;
    private String IdNacionalidad;
    private String IdSexo;
    private String FechaNacimiento;
    private String Edad;
    private String IdIdentificacion;
    private String IdentificacionOtro;
    private String NumIdentificacion;
    private String IdEntidadfederativa;
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
    private String GrupoDelictivo;
    private String DescGrupoDelictivo;
    private String ProporcionoFamiliar;
    private String APFamiliar;
    private String AMFamiliar;
    private String NomFamiliar;
    private String TelefonoFamiliar;
    private String InformoDerechos;
    private String RutaFirma;
    private String RecolectoPertenencias;
    private String LugarDetencionIntervencion;
    private String IdLugarTraslado;
    private String DescLugarTrasladoOtro;
    private String ObservacionesDetencion;
    private String IdPoliciaPrimerRespondiente;

    public ModeloDetenciones_Delictivo(String idHechoDelictivo, String numDetencionRND, String fecha, String hora,
                                       String APDentenido, String AMDetenido, String nomDetenido, String apodoAlias,
                                       String descripcionDetenido, String idNacionalidad, String idSexo, String fechaNacimiento,
                                       String edad, String idIdentificacion, String identificacionOtro, String numIdentificacion,
                                       String idEntidadfederativa, String idMunicipio, String coloniaLocalidad, String calleTramo,
                                       String noExterior, String noInterior, String cp, String referencia, String lesiones,
                                       String padecimientos, String descPadecimientos, String grupoVulnerable, String descGrupoVulnerable,
                                       String grupoDelictivo, String descGrupoDelictivo, String proporcionoFamiliar, String APFamiliar,
                                       String AMFamiliar, String nomFamiliar, String telefonoFamiliar, String informoDerechos, String rutaFirma,
                                       String recolectoPertenencias, String lugarDetencionIntervencion, String idLugarTraslado, String descLugarTrasladoOtro,
                                       String observacionesDetencion, String idPoliciaPrimerRespondiente) {

        IdHechoDelictivo = idHechoDelictivo;
        NumDetencionRND = numDetencionRND;
        Fecha = fecha;
        Hora = hora;
        this.APDentenido = APDentenido;
        this.AMDetenido = AMDetenido;
        NomDetenido = nomDetenido;
        ApodoAlias = apodoAlias;
        DescripcionDetenido = descripcionDetenido;
        IdNacionalidad = idNacionalidad;
        IdSexo = idSexo;
        FechaNacimiento = fechaNacimiento;
        Edad = edad;
        IdIdentificacion = idIdentificacion;
        IdentificacionOtro = identificacionOtro;
        NumIdentificacion = numIdentificacion;
        IdEntidadfederativa = idEntidadfederativa;
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
        GrupoDelictivo = grupoDelictivo;
        DescGrupoDelictivo = descGrupoDelictivo;
        ProporcionoFamiliar = proporcionoFamiliar;
        this.APFamiliar = APFamiliar;
        this.AMFamiliar = AMFamiliar;
        NomFamiliar = nomFamiliar;
        TelefonoFamiliar = telefonoFamiliar;
        InformoDerechos = informoDerechos;
        RutaFirma = rutaFirma;
        RecolectoPertenencias = recolectoPertenencias;
        LugarDetencionIntervencion = lugarDetencionIntervencion;
        IdLugarTraslado = idLugarTraslado;
        DescLugarTrasladoOtro = descLugarTrasladoOtro;
        ObservacionesDetencion = observacionesDetencion;
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
    }

    public String getIdHechoDelictivo() {
        return IdHechoDelictivo;
    }

    public void setIdHechoDelictivo(String idHechoDelictivo) {
        IdHechoDelictivo = idHechoDelictivo;
    }

    public String getNumDetencionRND() {
        return NumDetencionRND;
    }

    public void setNumDetencionRND(String numDetencionRND) {
        NumDetencionRND = numDetencionRND;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getAPDentenido() {
        return APDentenido;
    }

    public void setAPDentenido(String APDentenido) {
        this.APDentenido = APDentenido;
    }

    public String getAMDetenido() {
        return AMDetenido;
    }

    public void setAMDetenido(String AMDetenido) {
        this.AMDetenido = AMDetenido;
    }

    public String getNomDetenido() {
        return NomDetenido;
    }

    public void setNomDetenido(String nomDetenido) {
        NomDetenido = nomDetenido;
    }

    public String getApodoAlias() {
        return ApodoAlias;
    }

    public void setApodoAlias(String apodoAlias) {
        ApodoAlias = apodoAlias;
    }

    public String getDescripcionDetenido() {
        return DescripcionDetenido;
    }

    public void setDescripcionDetenido(String descripcionDetenido) {
        DescripcionDetenido = descripcionDetenido;
    }

    public String getIdNacionalidad() {
        return IdNacionalidad;
    }

    public void setIdNacionalidad(String idNacionalidad) {
        IdNacionalidad = idNacionalidad;
    }

    public String getIdSexo() {
        return IdSexo;
    }

    public void setIdSexo(String idSexo) {
        IdSexo = idSexo;
    }

    public String getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        FechaNacimiento = fechaNacimiento;
    }

    public String getEdad() {
        return Edad;
    }

    public void setEdad(String edad) {
        Edad = edad;
    }

    public String getIdIdentificacion() {
        return IdIdentificacion;
    }

    public void setIdIdentificacion(String idIdentificacion) {
        IdIdentificacion = idIdentificacion;
    }

    public String getIdentificacionOtro() {
        return IdentificacionOtro;
    }

    public void setIdentificacionOtro(String identificacionOtro) {
        IdentificacionOtro = identificacionOtro;
    }

    public String getNumIdentificacion() {
        return NumIdentificacion;
    }

    public void setNumIdentificacion(String numIdentificacion) {
        NumIdentificacion = numIdentificacion;
    }

    public String getIdEntidadfederativa() {
        return IdEntidadfederativa;
    }

    public void setIdEntidadfederativa(String idEntidadfederativa) {
        IdEntidadfederativa = idEntidadfederativa;
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

    public String getLesiones() {
        return Lesiones;
    }

    public void setLesiones(String lesiones) {
        Lesiones = lesiones;
    }

    public String getPadecimientos() {
        return Padecimientos;
    }

    public void setPadecimientos(String padecimientos) {
        Padecimientos = padecimientos;
    }

    public String getDescPadecimientos() {
        return DescPadecimientos;
    }

    public void setDescPadecimientos(String descPadecimientos) {
        DescPadecimientos = descPadecimientos;
    }

    public String getGrupoVulnerable() {
        return GrupoVulnerable;
    }

    public void setGrupoVulnerable(String grupoVulnerable) {
        GrupoVulnerable = grupoVulnerable;
    }

    public String getDescGrupoVulnerable() {
        return DescGrupoVulnerable;
    }

    public void setDescGrupoVulnerable(String descGrupoVulnerable) {
        DescGrupoVulnerable = descGrupoVulnerable;
    }

    public String getGrupoDelictivo() {
        return GrupoDelictivo;
    }

    public void setGrupoDelictivo(String grupoDelictivo) {
        GrupoDelictivo = grupoDelictivo;
    }

    public String getDescGrupoDelictivo() {
        return DescGrupoDelictivo;
    }

    public void setDescGrupoDelictivo(String descGrupoDelictivo) {
        DescGrupoDelictivo = descGrupoDelictivo;
    }

    public String getProporcionoFamiliar() {
        return ProporcionoFamiliar;
    }

    public void setProporcionoFamiliar(String proporcionoFamiliar) {
        ProporcionoFamiliar = proporcionoFamiliar;
    }

    public String getAPFamiliar() {
        return APFamiliar;
    }

    public void setAPFamiliar(String APFamiliar) {
        this.APFamiliar = APFamiliar;
    }

    public String getAMFamiliar() {
        return AMFamiliar;
    }

    public void setAMFamiliar(String AMFamiliar) {
        this.AMFamiliar = AMFamiliar;
    }

    public String getNomFamiliar() {
        return NomFamiliar;
    }

    public void setNomFamiliar(String nomFamiliar) {
        NomFamiliar = nomFamiliar;
    }

    public String getTelefonoFamiliar() {
        return TelefonoFamiliar;
    }

    public void setTelefonoFamiliar(String telefonoFamiliar) {
        TelefonoFamiliar = telefonoFamiliar;
    }

    public String getInformoDerechos() {
        return InformoDerechos;
    }

    public void setInformoDerechos(String informoDerechos) {
        InformoDerechos = informoDerechos;
    }

    public String getRutaFirma() {
        return RutaFirma;
    }

    public void setRutaFirma(String rutaFirma) {
        RutaFirma = rutaFirma;
    }

    public String getRecolectoPertenencias() {
        return RecolectoPertenencias;
    }
    public void setRecolectoPertenencias(String recolectoPertenencias) {
        RecolectoPertenencias = recolectoPertenencias;
    }
    public String getLugarDetencionIntervencion() {
        return LugarDetencionIntervencion;
    }

    public void setLugarDetencionIntervencion(String lugarDetencionIntervencion) {
        LugarDetencionIntervencion = lugarDetencionIntervencion;
    }

    public String getIdLugarTraslado() {
        return IdLugarTraslado;
    }

    public void setIdLugarTraslado(String idLugarTraslado) {
        IdLugarTraslado = idLugarTraslado;
    }

    public String getDescLugarTrasladoOtro() {
        return DescLugarTrasladoOtro;
    }

    public void setDescLugarTrasladoOtro(String descLugarTrasladoOtro) {
        DescLugarTrasladoOtro = descLugarTrasladoOtro;
    }

    public String getObservacionesDetencion() {
        return ObservacionesDetencion;
    }

    public void setObservacionesDetencion(String observacionesDetencion) {
        ObservacionesDetencion = observacionesDetencion;
    }

    public String getIdPoliciaPrimerRespondiente() {
        return IdPoliciaPrimerRespondiente;
    }

    public void setIdPoliciaPrimerRespondiente(String idPoliciaPrimerRespondiente) {
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
    }
}
