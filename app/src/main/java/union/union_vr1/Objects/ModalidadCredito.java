package union.union_vr1.Objects;

/**
 * Created by Usuario on 12/01/2016.
 */
public class ModalidadCredito {

    private int id;
    private String descripcion;
    private String diasCredito;
    private int estadoSincronizacion;

    public ModalidadCredito(int id, String descripcion, String diasCredito, int estadoSincronizacion) {
        this.id = id;
        this.descripcion = descripcion;
        this.diasCredito = diasCredito;
        this.estadoSincronizacion = estadoSincronizacion;
    }

    public ModalidadCredito() {
    }

    public String getDiasCredito() {
        return diasCredito;
    }

    public void setDiasCredito(String diasCredito) {
        this.diasCredito = diasCredito;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEstadoSincronizacion() {
        return estadoSincronizacion;
    }

    public void setEstadoSincronizacion(int estadoSincronizacion) {
        this.estadoSincronizacion = estadoSincronizacion;
    }
}
