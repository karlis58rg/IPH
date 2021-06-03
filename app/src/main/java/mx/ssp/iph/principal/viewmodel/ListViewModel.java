package mx.ssp.iph.principal.viewmodel;
import androidx.databinding.BaseObservable;

public class ListViewModel extends BaseObservable {
    public String folio , referencia;

    public ListViewModel(String folio, String referencia) {
        this.folio = folio;
        this.referencia = referencia;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
