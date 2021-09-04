package mx.ssp.iph.delictivo.model;

public class ModeloRecepcionLugarIntervencion {
    public ModeloRecepcionLugarIntervencion(String idHechoDelictivo, String idRecepcion, String descLugarIntervencion,
                                            String apoyoServiciosEspecializados, String servicioEspecializado,
                                            String idPoliciaPrimerRespondiente, String APRecibeIntervencion, String AMRecibeIntervencion,
                                            String nombreReciveIntervencion, String idCargoRecibe, String idAdscripcionRecibe,
                                            String rutaFirmaRecibe, String observaciones, String fecha, String hora) {
        IdHechoDelictivo = idHechoDelictivo;
        IdRecepcion = idRecepcion;
        DescLugarIntervencion = descLugarIntervencion;
        ApoyoServiciosEspecializados = apoyoServiciosEspecializados;
        ServicioEspecializado = servicioEspecializado;
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
        this.APRecibeIntervencion = APRecibeIntervencion;
        this.AMRecibeIntervencion = AMRecibeIntervencion;
        NombreReciveIntervencion = nombreReciveIntervencion;
        IdCargoRecibe = idCargoRecibe;
        IdAdscripcionRecibe = idAdscripcionRecibe;
        RutaFirmaRecibe = rutaFirmaRecibe;
        Observaciones = observaciones;
        Fecha = fecha;
        Hora = hora;
    }

    public String getIdHechoDelictivo() {
        return IdHechoDelictivo;
    }

    public void setIdHechoDelictivo(String idHechoDelictivo) {
        IdHechoDelictivo = idHechoDelictivo;
    }

    public String getIdRecepcion() {
        return IdRecepcion;
    }

    public void setIdRecepcion(String idRecepcion) {
        IdRecepcion = idRecepcion;
    }

    public String getDescLugarIntervencion() {
        return DescLugarIntervencion;
    }

    public void setDescLugarIntervencion(String descLugarIntervencion) {
        DescLugarIntervencion = descLugarIntervencion;
    }

    public String getApoyoServiciosEspecializados() {
        return ApoyoServiciosEspecializados;
    }

    public void setApoyoServiciosEspecializados(String apoyoServiciosEspecializados) {
        ApoyoServiciosEspecializados = apoyoServiciosEspecializados;
    }

    public String getServicioEspecializado() {
        return ServicioEspecializado;
    }

    public void setServicioEspecializado(String servicioEspecializado) {
        ServicioEspecializado = servicioEspecializado;
    }

    public String getIdPoliciaPrimerRespondiente() {
        return IdPoliciaPrimerRespondiente;
    }

    public void setIdPoliciaPrimerRespondiente(String idPoliciaPrimerRespondiente) {
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
    }

    public String getAPRecibeIntervencion() {
        return APRecibeIntervencion;
    }

    public void setAPRecibeIntervencion(String APRecibeIntervencion) {
        this.APRecibeIntervencion = APRecibeIntervencion;
    }

    public String getAMRecibeIntervencion() {
        return AMRecibeIntervencion;
    }

    public void setAMRecibeIntervencion(String AMRecibeIntervencion) {
        this.AMRecibeIntervencion = AMRecibeIntervencion;
    }

    public String getNombreReciveIntervencion() {
        return NombreReciveIntervencion;
    }

    public void setNombreReciveIntervencion(String nombreReciveIntervencion) {
        NombreReciveIntervencion = nombreReciveIntervencion;
    }

    public String getIdCargoRecibe() {
        return IdCargoRecibe;
    }

    public void setIdCargoRecibe(String idCargoRecibe) {
        IdCargoRecibe = idCargoRecibe;
    }

    public String getIdAdscripcionRecibe() {
        return IdAdscripcionRecibe;
    }

    public void setIdAdscripcionRecibe(String idAdscripcionRecibe) {
        IdAdscripcionRecibe = idAdscripcionRecibe;
    }

    public String getRutaFirmaRecibe() {
        return RutaFirmaRecibe;
    }

    public void setRutaFirmaRecibe(String rutaFirmaRecibe) {
        RutaFirmaRecibe = rutaFirmaRecibe;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
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

    private String IdHechoDelictivo;
    private String IdRecepcion;
    private String DescLugarIntervencion;
    private String ApoyoServiciosEspecializados;
    private String ServicioEspecializado;
    private String IdPoliciaPrimerRespondiente;
    private String APRecibeIntervencion;
    private String AMRecibeIntervencion;
    private String NombreReciveIntervencion;
    private String IdCargoRecibe;
    private String IdAdscripcionRecibe;
    private String RutaFirmaRecibe;
    private String Observaciones;
    private String Fecha;
    private String Hora;
}
