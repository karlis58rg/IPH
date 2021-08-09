package mx.ssp.iph.delictivo.model;

public class ModeloConocimientoHecho_Delictivo {
    private String IdHechoDelictivo;
    private String IdConocimiento;
    private String Telefono911;
    private String FechaConocimiento;
    private String HoraConocimiento;
    private String FechaArribo;
    private String HoraArribo;

    public ModeloConocimientoHecho_Delictivo(String idHechoDelictivo, String idConocimiento, String telefono911, String fechaConocimiento, String horaConocimiento, String fechaArribo, String horaArribo) {
        IdHechoDelictivo = idHechoDelictivo;
        IdConocimiento = idConocimiento;
        Telefono911 = telefono911;
        FechaConocimiento = fechaConocimiento;
        HoraConocimiento = horaConocimiento;
        FechaArribo = fechaArribo;
        HoraArribo = horaArribo;
    }

    public String getIdHechoDelictivo() {
        return IdHechoDelictivo;
    }

    public void setIdHechoDelictivo(String idHechoDelictivo) {
        IdHechoDelictivo = idHechoDelictivo;
    }

    public String getIdConocimiento() {
        return IdConocimiento;
    }

    public void setIdConocimiento(String idConocimiento) {
        IdConocimiento = idConocimiento;
    }

    public String getTelefono911() {
        return Telefono911;
    }

    public void setTelefono911(String telefono911) {
        Telefono911 = telefono911;
    }

    public String getFechaConocimiento() {
        return FechaConocimiento;
    }

    public void setFechaConocimiento(String fechaConocimiento) {
        FechaConocimiento = fechaConocimiento;
    }

    public String getHoraConocimiento() {
        return HoraConocimiento;
    }

    public void setHoraConocimiento(String horaConocimiento) {
        HoraConocimiento = horaConocimiento;
    }

    public String getFechaArribo() {
        return FechaArribo;
    }

    public void setFechaArribo(String fechaArribo) {
        FechaArribo = fechaArribo;
    }

    public String getHoraArribo() {
        return HoraArribo;
    }

    public void setHoraArribo(String horaArribo) {
        HoraArribo = horaArribo;
    }
}
