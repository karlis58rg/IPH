package mx.ssp.iph.delictivo.model;

public class ModeloInsepccionVehiculo_Delictivo {

    private String IdHechoDelictivo;
    private String Fecha;
    private String Hora;
    private String Tipo;
    private String TipoOtro;
    private String Procedencia;
    private String IdMarca;
    private String IdSubMarca;
    private String Modelo;
    private String Color;
    private String Uso;
    private String Placa;
    private String NoSerie;
    private String Situacion;
    private String Observaciones;
    private String Destino;
    private String ObjetosRelacionados;
    private String IdPoliciaPrimerRespondiente;

    public ModeloInsepccionVehiculo_Delictivo(String idHechoDelictivo, String fecha, String hora, String tipo, String tipoOtro,
                                              String procedencia, String idMarca, String idSubMarca, String modelo, String color,
                                              String uso, String placa, String noSerie, String situacion, String observaciones,
                                              String destino, String objetosRelacionados, String idPoliciaPrimerRespondiente) {
        IdHechoDelictivo = idHechoDelictivo;
        Fecha = fecha;
        Hora = hora;
        Tipo = tipo;
        TipoOtro = tipoOtro;
        Procedencia = procedencia;
        IdMarca = idMarca;
        IdSubMarca = idSubMarca;
        Modelo = modelo;
        Color = color;
        Uso = uso;
        Placa = placa;
        NoSerie = noSerie;
        Situacion = situacion;
        Observaciones = observaciones;
        Destino = destino;
        ObjetosRelacionados = objetosRelacionados;
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
    }

    public String getIdHechoDelictivo() {
        return IdHechoDelictivo;
    }

    public void setIdHechoDelictivo(String idHechoDelictivo) {
        IdHechoDelictivo = idHechoDelictivo;
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

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getTipoOtro() {
        return TipoOtro;
    }

    public void setTipoOtro(String tipoOtro) {
        TipoOtro = tipoOtro;
    }

    public String getProcedencia() {
        return Procedencia;
    }

    public void setProcedencia(String procedencia) {
        Procedencia = procedencia;
    }

    public String getIdMarca() {
        return IdMarca;
    }

    public void setIdMarca(String idMarca) {
        IdMarca = idMarca;
    }

    public String getIdSubMarca() {
        return IdSubMarca;
    }

    public void setIdSubMarca(String idSubMarca) {
        IdSubMarca = idSubMarca;
    }

    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String modelo) {
        Modelo = modelo;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getUso() {
        return Uso;
    }

    public void setUso(String uso) {
        Uso = uso;
    }

    public String getPlaca() {
        return Placa;
    }

    public void setPlaca(String placa) {
        Placa = placa;
    }

    public String getNoSerie() {
        return NoSerie;
    }

    public void setNoSerie(String noSerie) {
        NoSerie = noSerie;
    }

    public String getSituacion() {
        return Situacion;
    }

    public void setSituacion(String situacion) {
        Situacion = situacion;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }

    public String getDestino() {
        return Destino;
    }

    public void setDestino(String destino) {
        Destino = destino;
    }

    public String getObjetosRelacionados() {
        return ObjetosRelacionados;
    }

    public void setObjetosRelacionados(String objetosRelacionados) {
        ObjetosRelacionados = objetosRelacionados;
    }

    public String getIdPoliciaPrimerRespondiente() {
        return IdPoliciaPrimerRespondiente;
    }

    public void setIdPoliciaPrimerRespondiente(String idPoliciaPrimerRespondiente) {
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
    }

}
