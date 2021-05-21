package mx.ssp.iph.administrativo.model;

public class ModeloProbableInfraccion_Administrativo {
    private String IdConocimiento;
    private String Telefono911;
    private String Otro;

    public void setIdConocimiento(String idConocimiento) {
        IdConocimiento = idConocimiento;
    }

    public void setTelefono911(String telefono911) {
        Telefono911 = telefono911;
    }

    public void setOtro(String otro) {
        Otro = otro;
    }

    public String getIdConocimiento() {
        return IdConocimiento;
    }

    public String getTelefono911() {
        return Telefono911;
    }

    public String getOtro() {
        return Otro;
    }



    public ModeloProbableInfraccion_Administrativo(String telefono911, String otro) {
        Telefono911 = telefono911;
        Otro = otro;
    }
    /* public ModeloProbableInfraccion_Administrativo(String idConocimiento, String telefono911, String otro) {
        IdConocimiento = idConocimiento;
        Telefono911 = telefono911;
        Otro = otro;
    }*/
}
