package mx.ssp.iph.administrativo.model;

public class ModeloPuestaDisposicion_Administrativo {
    private String IdFaltaAdmin;
    private String NumReferencia;
    private String NumFolio;
    private String Fecha;
    private String Hora;
    private String NumExpediente;
    private String Narrativa;
    private String Detenciones;
    private String NumDetenciones;
    private String Vehiculos;
    private String NumVehiculos;
    private String SinAnexos;
    private String IdPoliciaPrimerRespondiente;
    private String IdUnidad;
    private String IdConocimiento;
    private String Telefono911;
    private String Otro;

    public String getIdFaltaAdmin() {
        return IdFaltaAdmin;
    }

    public String getNumReferencia() {
        return NumReferencia;
    }

    public String getNumFolio() {
        return NumFolio;
    }

    public String getFecha() {
        return Fecha;
    }

    public String getHora() {
        return Hora;
    }

    public String getNumExpediente() {
        return NumExpediente;
    }

    public String getNarrativa() {
        return Narrativa;
    }

    public String getDetenciones() {
        return Detenciones;
    }

    public String getNumDetenciones() {
        return NumDetenciones;
    }

    public String getVehiculos() {
        return Vehiculos;
    }

    public String getNumVehiculos() {
        return NumVehiculos;
    }

    public String getSinAnexos() {
        return SinAnexos;
    }

    public String getIdPoliciaPrimerRespondiente() {
        return IdPoliciaPrimerRespondiente;
    }

    public String getIdUnidad() {
        return IdUnidad;
    }

    public String getIdConocimiento() {
        return IdConocimiento;
    }

    public String getTelefono911() {
        return Telefono911;
    }

    public String getOtro() {
        return Otro;
    }

    public void setIdFaltaAdmin(String idFaltaAdmin) {
        IdFaltaAdmin = idFaltaAdmin;
    }

    public void setNumReferencia(String numReferencia) {
        NumReferencia = numReferencia;
    }

    public void setNumFolio(String numFolio) {
        NumFolio = numFolio;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public void setNumExpediente(String numExpediente) {
        NumExpediente = numExpediente;
    }

    public void setNarrativa(String narrativa) {
        Narrativa = narrativa;
    }

    public void setDetenciones(String detenciones) {
        Detenciones = detenciones;
    }

    public void setNumDetenciones(String numDetenciones) {
        NumDetenciones = numDetenciones;
    }

    public void setVehiculos(String vehiculos) {
        Vehiculos = vehiculos;
    }

    public void setNumVehiculos(String numVehiculos) {
        NumVehiculos = numVehiculos;
    }

    public void setSinAnexos(String sinAnexos) {
        SinAnexos = sinAnexos;
    }

    public void setIdPoliciaPrimerRespondiente(String idPoliciaPrimerRespondiente) {
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
    }

    public void setIdUnidad(String idUnidad) {
        IdUnidad = idUnidad;
    }

    public void setIdConocimiento(String idConocimiento) {
        IdConocimiento = idConocimiento;
    }

    public void setTelefono911(String telefono911) {
        Telefono911 = telefono911;
    }

    public void setOtro(String otro) {
        Otro = otro;
    }
    /*
        public ModeloPuestaDisposicion_Administrativo(String idFaltaAdmin, String numReferencia, String numFolio, String fecha,
                                                  String hora, String numExpediente, String narrativa, String detenciones,
                                                  String numDetenciones, String vehiculos, String numVehiculos, String sinAnexos,
                                                  String idPoliciaPrimerRespondiente, String idUnidad, String idConocimiento, String telefono911, String otro) {
        IdFaltaAdmin = idFaltaAdmin;
        NumReferencia = numReferencia;
        NumFolio = numFolio;
        Fecha = fecha;
        Hora = hora;
        NumExpediente = numExpediente;
        Narrativa = narrativa;
        Detenciones = detenciones;
        NumDetenciones = numDetenciones;
        Vehiculos = vehiculos;
        NumVehiculos = numVehiculos;
        SinAnexos = sinAnexos;
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
        IdUnidad = idUnidad;
        IdConocimiento = idConocimiento;
        Telefono911 = telefono911;
        Otro = otro;
    }
     */

    public ModeloPuestaDisposicion_Administrativo(String idFaltaAdmin, String numReferencia, String fecha,
                                                  String hora, String numExpediente) {
        IdFaltaAdmin = idFaltaAdmin;
        NumReferencia = numReferencia;
        Fecha = fecha;
        Hora = hora;
        NumExpediente = numExpediente;
    }
}
