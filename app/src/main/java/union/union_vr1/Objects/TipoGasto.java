package union.union_vr1.Objects;

/**
 * Created by Usuario on 15/01/2015.
 */
public class TipoGasto {

    private int idTipoGasto;
    private String nombreTipoGasto;
    private int estadoSincronizacion;

    public TipoGasto(int idTipoGasto, String nombreTipoGasto, int estadoSincronizacion) {
        this.idTipoGasto = idTipoGasto;
        this.nombreTipoGasto = nombreTipoGasto;
        this.estadoSincronizacion = estadoSincronizacion;
    }

    public int getIdTipoGasto() {
        return idTipoGasto;
    }

    public void setIdTipoGasto(int idTipoGasto) {
        this.idTipoGasto = idTipoGasto;
    }

    public String getNombreTipoGasto() {
        return nombreTipoGasto;
    }

    public void setNombreTipoGasto(String nombreTipoGasto) {
        this.nombreTipoGasto = nombreTipoGasto;
    }

    public int getEstadoSincronizacion() {
        return estadoSincronizacion;
    }

    public void setEstadoSincronizacion(int estadoSincronizacion) {
        this.estadoSincronizacion = estadoSincronizacion;
    }
}
