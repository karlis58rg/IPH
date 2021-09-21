package mx.ssp.iph.delictivo.model;

public class ModeloAnexosMultimedia_Delictivo {
    private String IdHechoDelictivo;
    private String AnexoDocumentacion;
    private String IdAnexoMultimedia;
    private String RutaAnexoMultimedia;
    private String AnexoMultimediaOtro;

    public ModeloAnexosMultimedia_Delictivo(String idHechoDelictivo, String anexoDocumentacion,
                                            String idAnexoMultimedia, String rutaAnexoMultimedia,
                                            String anexoMultimediaOtro) {
        IdHechoDelictivo = idHechoDelictivo;
        AnexoDocumentacion = anexoDocumentacion;
        IdAnexoMultimedia = idAnexoMultimedia;
        RutaAnexoMultimedia = rutaAnexoMultimedia;
        AnexoMultimediaOtro = anexoMultimediaOtro;
    }

    public String getIdHechoDelictivo() {
        return IdHechoDelictivo;
    }

    public void setIdHechoDelictivo(String idHechoDelictivo) {
        IdHechoDelictivo = idHechoDelictivo;
    }

    public String getAnexoDocumentacion() {
        return AnexoDocumentacion;
    }

    public void setAnexoDocumentacion(String anexoDocumentacion) {
        AnexoDocumentacion = anexoDocumentacion;
    }

    public String getIdAnexoMultimedia() {
        return IdAnexoMultimedia;
    }

    public void setIdAnexoMultimedia(String idAnexoMultimedia) {
        IdAnexoMultimedia = idAnexoMultimedia;
    }

    public String getRutaAnexoMultimedia() {
        return RutaAnexoMultimedia;
    }

    public void setRutaAnexoMultimedia(String rutaAnexoMultimedia) {
        RutaAnexoMultimedia = rutaAnexoMultimedia;
    }

    public String getAnexoMultimediaOtro() {
        return AnexoMultimediaOtro;
    }

    public void setAnexoMultimediaOtro(String anexoMultimediaOtro) {
        AnexoMultimediaOtro = anexoMultimediaOtro;
    }

}
