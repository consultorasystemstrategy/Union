package union.union_vr1.Objects;

/**
 * Created by Usuario on 08/01/2016.
 */
public class Credito {
    private int idAgente;
    private int idEstablecimiento;
    private double montoCredito;
    int diasCredito;
    boolean estado;

    public Credito() {
    }

    public Credito(int idAgente, int idEstablecimiento, double montoCredito, int diasCredito, boolean estado) {
        this.idAgente = idAgente;
        this.idEstablecimiento = idEstablecimiento;
        this.montoCredito = montoCredito;
        this.diasCredito = diasCredito;
        this.estado = estado;
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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
