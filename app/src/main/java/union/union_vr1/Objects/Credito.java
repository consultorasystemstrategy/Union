package union.union_vr1.Objects;

/**
 * Created by Usuario on 08/01/2016.
 */
public class Credito {
    private int idAgente;
    private int idEstablecimiento;
    private double montoCredito;
    int diasCredito;
    int estado;
    private String observacion;
    private String fecha;

    public Credito() {
    }

    public Credito(int idAgente, int idEstablecimiento, double montoCredito, int diasCredito, int estado, String observacion, String fecha) {
        this.idAgente = idAgente;
        this.idEstablecimiento = idEstablecimiento;
        this.montoCredito = montoCredito;
        this.diasCredito = diasCredito;
        this.estado = estado;
        this.observacion = observacion;
        this.fecha = fecha;
    }


    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public int getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(int idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public double getMontoCredito() {
        return montoCredito;
    }

    public void setMontoCredito(double montoCredito) {
        this.montoCredito = montoCredito;
    }

    public int getDiasCredito() {
        return diasCredito;
    }

    public void setDiasCredito(int diasCredito) {
        this.diasCredito = diasCredito;
    }


    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
