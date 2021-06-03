package mx.ssp.iph.administrativo.model;

public class ModeloDescripcionVehiculos_Administrativo {
    private String IdFaltaAdmin;
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
    private String Observaciones;
    private String Destino;
    private String IdPoliciaPrimerRespondiente;

    public void setIdFaltaAdmin(String idFaltaAdmin) {
        IdFaltaAdmin = idFaltaAdmin;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public void setTipoOtro(String tipoOtro) {
        TipoOtro = tipoOtro;
    }

    public void setProcedencia(String procedencia) {
        Procedencia = procedencia;
    }

    public void setIdMarca(String idMarca) {
        IdMarca = idMarca;
    }

    public void setIdSubMarca(String idSubMarca) {
        IdSubMarca = idSubMarca;
    }

    public void setModelo(String modelo) {
        Modelo = modelo;
    }

    public void setColor(String color) {
        Color = color;
    }

    public void setUso(String uso) {
        Uso = uso;
    }

    public void setPlaca(String placa) {
        Placa = placa;
    }

    public void setNoSerie(String noSerie) {
        NoSerie = noSerie;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }

    public void setDestino(String destino) {
        Destino = destino;
    }

    public void setIdPoliciaPrimerRespondiente(String idPoliciaPrimerRespondiente) {
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
    }

    public String getIdFaltaAdmin() {
        return IdFaltaAdmin;
    }

    public String getFecha() {
        return Fecha;
    }

    public String getHora() {
        return Hora;
    }

    public String getTipo() {
        return Tipo;
    }

    public String getTipoOtro() {
        return TipoOtro;
    }

    public String getProcedencia() {
        return Procedencia;
    }

    public String getIdMarca() {
        return IdMarca;
    }

    public String getIdSubMarca() {
        return IdSubMarca;
    }

    public String getModelo() {
        return Modelo;
    }

    public String getColor() {
        return Color;
    }

    public String getUso() {
        return Uso;
    }

    public String getPlaca() {
        return Placa;
    }

    public String getNoSerie() {
        return NoSerie;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public String getDestino() {
        return Destino;
    }

    public String getIdPoliciaPrimerRespondiente() {
        return IdPoliciaPrimerRespondiente;
    }



    public ModeloDescripcionVehiculos_Administrativo(String idFaltaAdmin, String fecha, String hora, String tipo,
                                                     String tipoOtro, String procedencia, String idMarca,
                                                     String idSubMarca, String modelo, String color, String uso,
                                                     String placa, String noSerie, String observaciones,
                                                     String destino, String idPoliciaPrimerRespondiente) {
        IdFaltaAdmin = idFaltaAdmin;
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
        Observaciones = observaciones;
        Destino = destino;
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
    }


}
