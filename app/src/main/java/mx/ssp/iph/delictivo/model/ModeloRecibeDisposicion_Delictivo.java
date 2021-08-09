package mx.ssp.iph.delictivo.model;

public class ModeloRecibeDisposicion_Delictivo {
    private String IdHechoDelictivo;
    private String NombreRecibeDisposicion;
    private String IdFiscaliaAutoridad;
    private String IdAdscripcion;
    private String IdCargo;
    private String RutaFirma;

    public ModeloRecibeDisposicion_Delictivo(String idHechoDelictivo, String nombreRecibeDisposicion, String idFiscaliaAutoridad, String idAdscripcion, String idCargo, String rutaFirma) {
        IdHechoDelictivo = idHechoDelictivo;
        NombreRecibeDisposicion = nombreRecibeDisposicion;
        IdFiscaliaAutoridad = idFiscaliaAutoridad;
        IdAdscripcion = idAdscripcion;
        IdCargo = idCargo;
        RutaFirma = rutaFirma;
    }

    public String getIdHechoDelictivo() {
        return IdHechoDelictivo;
    }

    public String getNombreRecibeDisposicion() {
        return NombreRecibeDisposicion;
    }

    public String getIdFiscaliaAutoridad() {
        return IdFiscaliaAutoridad;
    }

    public String getIdAdscripcion() {
        return IdAdscripcion;
    }

    public String getIdCargo() {
        return IdCargo;
    }

    public String getRutaFirma() {
        return RutaFirma;
    }

    public void setIdHechoDelictivo(String idHechoDelictivo) {
        IdHechoDelictivo = idHechoDelictivo;
    }

    public void setNombreRecibeDisposicion(String nombreRecibeDisposicion) {
        NombreRecibeDisposicion = nombreRecibeDisposicion;
    }

    public void setIdFiscaliaAutoridad(String idFiscaliaAutoridad) {
        IdFiscaliaAutoridad = idFiscaliaAutoridad;
    }

    public void setIdAdscripcion(String idAdscripcion) {
        IdAdscripcion = idAdscripcion;
    }

    public void setIdCargo(String idCargo) {
        IdCargo = idCargo;
    }

    public void setRutaFirma(String rutaFirma) {
        RutaFirma = rutaFirma;
    }


}
