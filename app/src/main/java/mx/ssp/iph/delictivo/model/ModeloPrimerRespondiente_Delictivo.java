package mx.ssp.iph.delictivo.model;

public class ModeloPrimerRespondiente_Delictivo {
    private String IdHechoDelictivo;
    private String IdUnidad;
    private String MasElementos;
    private String NumElementos;

    public String getIdHechoDelictivo() {
        return IdHechoDelictivo;
    }

    public void setIdHechoDelictivo(String idHechoDelictivo) { IdHechoDelictivo = idHechoDelictivo; }

    public String getIdUnidad() {
        return IdUnidad;
    }

    public void setIdUnidad(String idUnidad) {
        IdUnidad = idUnidad;
    }

    public String getMasElementos() {
        return MasElementos;
    }

    public void setMasElementos(String masElementos) {
        MasElementos = masElementos;
    }

    public String getNumElementos() {
        return NumElementos;
    }

    public void setNumElementos(String numElementos) {
        NumElementos = numElementos;
    }

    public ModeloPrimerRespondiente_Delictivo(String idHechoDelictivo,String idUnidad, String masElementos, String numElementos) {
        IdHechoDelictivo = idHechoDelictivo;
        IdUnidad = idUnidad;
        MasElementos = masElementos;
        NumElementos = numElementos;
    }

}
