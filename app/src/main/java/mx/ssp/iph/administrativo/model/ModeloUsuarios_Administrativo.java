package mx.ssp.iph.administrativo.model;

public class ModeloUsuarios_Administrativo {

    private String APPolicia;
    private String AMPolicia;
    private String NomPolicia;
    private String IdFiscaliaAutoridad;
    private String IdCargo;
    private String IdSexo;
    private String UrlFirma;
    private String Usuario;
    private String Password;

    public ModeloUsuarios_Administrativo(String usuario, String password) {
        Usuario = usuario;
        Password = password;
    }
    /*
        public ModeloUsuarios(String idPoliciaPrimerRespondiente, String APPolicia, String AMPolicia, String nomPolicia,
                          String idFiscaliaAutoridad, String idCargo, String idSexo, String urlFirma, String usuario, String password) {
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
        this.APPolicia = APPolicia;
        this.AMPolicia = AMPolicia;
        NomPolicia = nomPolicia;
        IdFiscaliaAutoridad = idFiscaliaAutoridad;
        IdCargo = idCargo;
        IdSexo = idSexo;
        UrlFirma = urlFirma;
        Usuario = usuario;
        Password = password;
    }
    */

    private String IdPoliciaPrimerRespondiente;

    public void setIdPoliciaPrimerRespondiente(String idPoliciaPrimerRespondiente) {
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
    }

    public void setAPPolicia(String APPolicia) {
        this.APPolicia = APPolicia;
    }

    public void setAMPolicia(String AMPolicia) {
        this.AMPolicia = AMPolicia;
    }

    public void setNomPolicia(String nomPolicia) {
        NomPolicia = nomPolicia;
    }

    public void setIdFiscaliaAutoridad(String idFiscaliaAutoridad) {
        IdFiscaliaAutoridad = idFiscaliaAutoridad;
    }

    public void setIdCargo(String idCargo) {
        IdCargo = idCargo;
    }

    public void setIdSexo(String idSexo) {
        IdSexo = idSexo;
    }

    public void setUrlFirma(String urlFirma) {
        UrlFirma = urlFirma;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getIdPoliciaPrimerRespondiente() {
        return IdPoliciaPrimerRespondiente;
    }

    public String getAPPolicia() {
        return APPolicia;
    }

    public String getAMPolicia() {
        return AMPolicia;
    }

    public String getNomPolicia() {
        return NomPolicia;
    }

    public String getIdFiscaliaAutoridad() {
        return IdFiscaliaAutoridad;
    }

    public String getIdCargo() {
        return IdCargo;
    }

    public String getIdSexo() {
        return IdSexo;
    }

    public String getUrlFirma() {
        return UrlFirma;
    }

    public String getUsuario() {
        return Usuario;
    }

    public String getPassword() {
        return Password;
    }

}
