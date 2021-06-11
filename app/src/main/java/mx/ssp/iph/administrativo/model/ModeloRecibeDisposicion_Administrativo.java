package mx.ssp.iph.administrativo.model;

public class ModeloRecibeDisposicion_Administrativo {
    private String IdFaltaAdmin;
    private String IdPuestaDisposicion;
    private String IdFiscaliaAutoridad;
    private String IdCargo;
    private String NomRecibePuestaDisp;
    private String UrlFirma;

    public void setIdFaltaAdmin(String idFaltaAdmin) {
        IdFaltaAdmin = idFaltaAdmin;
    }

    public void setIdPuestaDisposicion(String idPuestaDisposicion) {
        IdPuestaDisposicion = idPuestaDisposicion;
    }

    public void setIdFiscaliaAutoridad(String idFiscaliaAutoridad) {
        IdFiscaliaAutoridad = idFiscaliaAutoridad;
    }

    public void setIdCargo(String idCargo) {
        IdCargo = idCargo;
    }

    public void setNomRecibePuestaDisp(String nomRecibePuestaDisp) {
        NomRecibePuestaDisp = nomRecibePuestaDisp;
    }

    public void setUrlFirma(String urlFirma) {
        UrlFirma = urlFirma;
    }

    public String getIdFaltaAdmin() {
        return IdFaltaAdmin;
    }

    public String getIdPuestaDisposicion() {
        return IdPuestaDisposicion;
    }

    public String getIdFiscaliaAutoridad() {
        return IdFiscaliaAutoridad;
    }

    public String getIdCargo() {
        return IdCargo;
    }

    public String getNomRecibePuestaDisp() {
        return NomRecibePuestaDisp;
    }

    public String getUrlFirma() {
        return UrlFirma;
    }



    public ModeloRecibeDisposicion_Administrativo(String idFaltaAdmin,
                                                  String idFiscaliaAutoridad, String idCargo,
                                                  String nomRecibePuestaDisp, String urlFirma) {
        IdFaltaAdmin = idFaltaAdmin;
        IdFiscaliaAutoridad = idFiscaliaAutoridad;
        IdCargo = idCargo;
        NomRecibePuestaDisp = nomRecibePuestaDisp;
        UrlFirma = urlFirma;
    }

    public ModeloRecibeDisposicion_Administrativo(String nomRecibePuestaDisp) {
        NomRecibePuestaDisp = nomRecibePuestaDisp;
    }


}
