package union.union_vr1.Objects;

/**
 * Created by Usuario on 10/02/2016.
 */
public class FormaPago {

    private int _id_forma_pago;
    private String detalle;
    private int selected;
    private int estadoSincronizacion;
    private int liquidacion;
    private String fecha;

    public FormaPago(int _id_forma_pago, String detalle, int selected, int estadoSincronizacion, int liquidacion, String fecha) {
        this._id_forma_pago = _id_forma_pago;
        this.detalle = detalle;
        this.selected = selected;
        this.estadoSincronizacion = estadoSincronizacion;
        this.liquidacion = liquidacion;
        this.fecha = fecha;
    }

    public FormaPago() {
    }

    public int get_id_forma_pago() {
        return _id_forma_pago;
    }

    public void set_id_forma_pago(int _id_forma_pago) {
        this._id_forma_pago = _id_forma_pago;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getEstadoSincronizacion() {
        return estadoSincronizacion;
    }

    public void setEstadoSincronizacion(int estadoSincronizacion) {
        this.estadoSincronizacion = estadoSincronizacion;
    }

    public int getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(int liquidacion) {
        this.liquidacion = liquidacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
