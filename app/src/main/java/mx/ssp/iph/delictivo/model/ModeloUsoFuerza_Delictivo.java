package mx.ssp.iph.delictivo.model;

import android.util.Log;

public class ModeloUsoFuerza_Delictivo {
    private String IdHechoDelictivo;
    private String LesionadosAutoridad;
    private String NumLesionadosAutoridad;
    private String LesionadosPersona;
    private String NumLesionadosPersona;
    private String FallecidosAutoridad;
    private String NumFallecidosAutoridad;
    private String FallecidosPersona;
    private String NumFallecidosPersona;
    private String ReduccionMovimientos;
    private String ArmasIncapacitantes;
    private String ArmasLetal;
    private String NarrativaUsoFuerza;
    private String AsistenciaMedica;
    private String NarrativaAsistenciaMedica;
    private String IdPoliciaPrimerRespondiente;

    public ModeloUsoFuerza_Delictivo(String idHechoDelictivo, String lesionadosAutoridad, String numLesionadosAutoridad, String lesionadosPersona,
                                     String numLesionadosPersona,String fallecidosAutoridad, String numFallecidosAutoridad, String fallecidosPersona,
                                     String numFallecidosPersona, String reduccionMovimientos, String armasIncapacitantes, String armasLetal,
                                     String narrativaUsoFuerza, String asistenciaMedica, String narrativaAsistenciaMedica, String idPoliciaPrimerRespondiente) {
        Log.i("FUERZA", "Inicia Modelo Uso Fuerza");

        IdHechoDelictivo = idHechoDelictivo;
        LesionadosAutoridad = lesionadosAutoridad;
        NumLesionadosAutoridad = numLesionadosAutoridad;
        LesionadosPersona = lesionadosPersona;
        NumLesionadosPersona = numLesionadosPersona;
        FallecidosAutoridad = fallecidosAutoridad;
        NumFallecidosAutoridad = numFallecidosAutoridad;
        FallecidosPersona = fallecidosPersona;
        NumFallecidosPersona = numFallecidosPersona;
        ReduccionMovimientos = reduccionMovimientos;
        ArmasIncapacitantes = armasIncapacitantes;
        ArmasLetal = armasLetal;
        NarrativaUsoFuerza = narrativaUsoFuerza;
        AsistenciaMedica = asistenciaMedica;
        NarrativaAsistenciaMedica = narrativaAsistenciaMedica;
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
    }

    public String getIdHechoDelictivo() {
        return IdHechoDelictivo;
    }

    public void setIdHechoDelictivo(String idHechoDelictivo) {
        IdHechoDelictivo = idHechoDelictivo;
    }

    public String getLesionadosAutoridad() {
        return LesionadosAutoridad;
    }

    public void setLesionadosAutoridad(String lesionadosAutoridad) {
        LesionadosAutoridad = lesionadosAutoridad;
    }

    public String getNumLesionadosAutoridad() {
        return NumLesionadosAutoridad;
    }

    public void setNumLesionadosAutoridad(String numLesionadosAutoridad) {
        NumLesionadosAutoridad = numLesionadosAutoridad;
    }

    public String getLesionadosPersona() {
        return LesionadosPersona;
    }

    public void setLesionadosPersona(String lesionadosPersona) {
        LesionadosPersona = lesionadosPersona;
    }

    public String getNumLesionadosPersona() {
        return NumLesionadosPersona;
    }

    public void setNumLesionadosPersona(String numLesionadosPersona) {
        NumLesionadosPersona = numLesionadosPersona;
    }

    public String getFallecidosAutoridad() {
        return FallecidosAutoridad;
    }

    public void setFallecidosAutoridad(String fallecidosAutoridad) {
        FallecidosAutoridad = fallecidosAutoridad;
    }

    public String getNumFallecidosAutoridad() {
        return NumFallecidosAutoridad;
    }

    public void setNumFallecidosAutoridad(String numFallecidosAutoridad) {
        NumFallecidosAutoridad = numFallecidosAutoridad;
    }

    public String getFallecidosPersona() {
        return FallecidosPersona;
    }

    public void setFallecidosPersona(String fallecidosPersona) {
        FallecidosPersona = fallecidosPersona;
    }

    public String getNumFallecidosPersona() {
        return NumFallecidosPersona;
    }

    public void setNumFallecidosPersona(String numFallecidosPersona) {
        NumFallecidosPersona = numFallecidosPersona;
    }

    public String getReduccionMovimientos() {
        return ReduccionMovimientos;
    }

    public void setReduccionMovimientos(String reduccionMovimientos) {
        ReduccionMovimientos = reduccionMovimientos;
    }

    public String getArmasIncapacitantes() {
        return ArmasIncapacitantes;
    }

    public void setArmasIncapacitantes(String armasIncapacitantes) {
        ArmasIncapacitantes = armasIncapacitantes;
    }

    public String getArmasLetal() {
        return ArmasLetal;
    }

    public void setArmasLetal(String armasLetal) {
        ArmasLetal = armasLetal;
    }

    public String getNarrativaUsoFuerza() {
        return NarrativaUsoFuerza;
    }

    public void setNarrativaUsoFuerza(String narrativaUsoFuerza) {
        NarrativaUsoFuerza = narrativaUsoFuerza;
    }

    public String getAsistenciaMedica() {
        return AsistenciaMedica;
    }

    public void setAsistenciaMedica(String asistenciaMedica) {
        AsistenciaMedica = asistenciaMedica;
    }

    public String getNarrativaAsistenciaMedica() {
        return NarrativaAsistenciaMedica;
    }

    public void setNarrativaAsistenciaMedica(String narrativaAsistenciaMedica) {
        NarrativaAsistenciaMedica = narrativaAsistenciaMedica;
    }

    public String getIdPoliciaPrimerRespondiente() {
        return IdPoliciaPrimerRespondiente;
    }

    public void setIdPoliciaPrimerRespondiente(String idPoliciaPrimerRespondiente) {
        IdPoliciaPrimerRespondiente = idPoliciaPrimerRespondiente;
    }

}
