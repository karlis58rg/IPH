package mx.ssp.iph.delictivo.model;

public class ModeloObjetos_Delictivo {
    private String IdHechoDelictivo;
    private String Objeto;
    private String ObjetoOtro;
    private String Aportacion;
    private String Inspeccion;
    private String LugarEncontro;
    private String DescObjeto;
    private String Destino;
    private String APEncontro;
    private String AMEncontro;
    private String NombreEncontro;
    private String RutaFirmaEncontro;
    private String APTestigoUno;
    private String AMTestigoUno;
    private String NombreTestigoUno;
    private String RutaFirmaTestigoUno;
    private String APTestigoDos;
    private String AMTestigoDos;
    private String NombreTestigoDos;
    private String RutaFirmaTestigoDos;
    private String IdPoliciaPrimerRespondiente;

    public ModeloObjetos_Delictivo(String idHechoDelictivo, String objeto, String objetoOtro, String aportacion, String inspeccion, String lugarEncontro,
                                   String descObjeto, String destino, String APEncontro, String AMEncontro, String nombreEncontro, String rutaFirmaEncontro,
                                   String APTestigoUno, String AMTestigoUno, String nombreTestigoUno, String rutaFirmaTestigoUno, String APTestigoDos,
                                   String AMTestigoDos, String nombreTestigoDos, String rutaFirmaTestigoDos, String idPoliciaPrimerRespondiente) {

        IdHechoDelictivo = idHechoDelictivo;
        Objeto = objeto;
        ObjetoOtro = objetoOtro;
        Aportacion = aportacion;
        Inspeccion = inspeccion;
        LugarEncontro = lugarEncontro;
        DescObjeto = descObjeto;
        Destino = destino;
        this.APEncontro = APEncontro;
        this.AMEncontro = AMEncontro;
        NombreEncontro = nombreEncontro;
        RutaFirmaEncontro = rutaFirmaEncontro;
        this.APTestigoUno = APTestigoUno;
        this.AMTestigoUno = AMTestigoUno;
        NombreTestigoUno = nombreTestigoUno;
        RutaFirmaTestigoUno = rutaFirmaTestigoUno;
        this.APTestigoDos = APTestigoDos;
        this.AMTestigoDos = AMTestigoDos;
        NombreTestigoDos = nombreTestigoDos;
        RutaFirmaTestigoDos = rutaFirmaTestigoDos;
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
    }

    public String getIdHechoDelictivo() {
        return IdHechoDelictivo;
    }

    public void setIdHechoDelictivo(String idHechoDelictivo) {
        IdHechoDelictivo = idHechoDelictivo;
    }

    public String getObjeto() {
        return Objeto;
    }

    public void setObjeto(String objeto) {
        Objeto = objeto;
    }

    public String getObjetoOtro() {
        return ObjetoOtro;
    }

    public void setObjetoOtro(String objetoOtro) {
        ObjetoOtro = objetoOtro;
    }

    public String getAportacion() {
        return Aportacion;
    }

    public void setAportacion(String aportacion) {
        Aportacion = aportacion;
    }

    public String getInspeccion() {
        return Inspeccion;
    }

    public void setInspeccion(String inspeccion) {
        Inspeccion = inspeccion;
    }

    public String getLugarEncontro() {
        return LugarEncontro;
    }

    public void setLugarEncontro(String lugarEncontro) {
        LugarEncontro = lugarEncontro;
    }

    public String getDescObjeto() {
        return DescObjeto;
    }

    public void setDescObjeto(String descObjeto) {
        DescObjeto = descObjeto;
    }

    public String getDestino() {
        return Destino;
    }

    public void setDestino(String destino) {
        Destino = destino;
    }

    public String getAPEncontro() {
        return APEncontro;
    }

    public void setAPEncontro(String APEncontro) {
        this.APEncontro = APEncontro;
    }

    public String getAMEncontro() {
        return AMEncontro;
    }

    public void setAMEncontro(String AMEncontro) {
        this.AMEncontro = AMEncontro;
    }

    public String getNombreEncontro() {
        return NombreEncontro;
    }

    public void setNombreEncontro(String nombreEncontro) {
        NombreEncontro = nombreEncontro;
    }

    public String getRutaFirmaEncontro() {
        return RutaFirmaEncontro;
    }

    public void setRutaFirmaEncontro(String rutaFirmaEncontro) {
        RutaFirmaEncontro = rutaFirmaEncontro;
    }

    public String getAPTestigoUno() {
        return APTestigoUno;
    }

    public void setAPTestigoUno(String APTestigoUno) {
        this.APTestigoUno = APTestigoUno;
    }

    public String getAMTestigoUno() {
        return AMTestigoUno;
    }

    public void setAMTestigoUno(String AMTestigoUno) {
        this.AMTestigoUno = AMTestigoUno;
    }

    public String getNombreTestigoUno() {
        return NombreTestigoUno;
    }

    public void setNombreTestigoUno(String nombreTestigoUno) {
        NombreTestigoUno = nombreTestigoUno;
    }

    public String getRutaFirmaTestigoUno() {
        return RutaFirmaTestigoUno;
    }

    public void setRutaFirmaTestigoUno(String rutaFirmaTestigoUno) {
        RutaFirmaTestigoUno = rutaFirmaTestigoUno;
    }

    public String getAPTestigoDos() {
        return APTestigoDos;
    }

    public void setAPTestigoDos(String APTestigoDos) {
        this.APTestigoDos = APTestigoDos;
    }

    public String getAMTestigoDos() {
        return AMTestigoDos;
    }

    public void setAMTestigoDos(String AMTestigoDos) {
        this.AMTestigoDos = AMTestigoDos;
    }

    public String getNombreTestigoDos() {
        return NombreTestigoDos;
    }

    public void setNombreTestigoDos(String nombreTestigoDos) {
        NombreTestigoDos = nombreTestigoDos;
    }

    public String getRutaFirmaTestigoDos() {
        return RutaFirmaTestigoDos;
    }

    public void setRutaFirmaTestigoDos(String rutaFirmaTestigoDos) {
        RutaFirmaTestigoDos = rutaFirmaTestigoDos;
    }

    public String getIdPoliciaPrimerRespondiente() {
        return IdPoliciaPrimerRespondiente;
    }

    public void setIdPoliciaPrimerRespondiente(String idPoliciaPrimerRespondiente) {
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
    }

}

