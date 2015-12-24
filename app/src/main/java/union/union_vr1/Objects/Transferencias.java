package union.union_vr1.Objects;

/**
 * Created by Usuario on 23/12/2015.
 */
public class Transferencias {

    private String idTransferencia;
    private String codigo;
    private String producto;
    private int cantidad;
    private String descripcionTransferencia;
    private int almacenId;

    public Transferencias(String idTransferencia, String codigo, String producto, int cantidad, String descripcionTransferencia, int almacenId) {
        this.idTransferencia = idTransferencia;
        this.codigo = codigo;
        this.producto = producto;
        this.cantidad = cantidad;
        this.descripcionTransferencia = descripcionTransferencia;
        this.almacenId = almacenId;
    }

    public String getIdTransferencia() {
        return idTransferencia;
    }

    public void setIdTransferencia(String idTransferencia) {
        this.idTransferencia = idTransferencia;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcionTransferencia() {
        return descripcionTransferencia;
    }

    public void setDescripcionTransferencia(String descripcionTransferencia) {
        this.descripcionTransferencia = descripcionTransferencia;
    }

    public int getAlmacenId() {
        return almacenId;
    }

    public void setAlmacenId(int almacenId) {
        this.almacenId = almacenId;
    }
}
