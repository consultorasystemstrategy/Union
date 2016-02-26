package union.union_vr1.Objects;

/**
 * Created by Usuario on 25/02/2016.
 */
public class ClienteAdded {

    private int idLiquidacion;
    private int idAgente;
    private int idEstablecimiento;


    public ClienteAdded() {
    }


    public ClienteAdded(int idLiquidacion, int idAgente, int idEstablecimiento) {
        this.idLiquidacion = idLiquidacion;
        this.idAgente = idAgente;
        this.idEstablecimiento = idEstablecimiento;
    }

    public int getIdLiquidacion() {
        return idLiquidacion;
    }

    public void setIdLiquidacion(int idLiquidacion) {
        this.idLiquidacion = idLiquidacion;
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
}
