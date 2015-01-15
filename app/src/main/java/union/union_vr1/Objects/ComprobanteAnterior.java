package union.union_vr1.Objects;

/**
 * Created by Usuario on 15/01/2015.
 */
public class ComprobanteAnterior {

    private int idEstablecimiento;
    private int idProducto;
    private String nombreProducto;
    private int cantidad;
    //Supuestamente es text?
    private String promedioAnterior;
    private String devuelto;
    private int valorUnidad;
    private int idAgente;
    private int estadoSincronizado;


    public ComprobanteAnterior(int idEstablecimiento, int idProducto, String nombreProducto, int cantidad, String promedioAnterior, String devuelto, int valorUnidad, int idAgente, int estadoSincronizado) {
        this.idEstablecimiento = idEstablecimiento;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.promedioAnterior = promedioAnterior;
        this.devuelto = devuelto;
        this.valorUnidad = valorUnidad;
        this.idAgente = idAgente;
        this.estadoSincronizado = estadoSincronizado;
    }

    public int getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(int idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getPromedioAnterior() {
        return promedioAnterior;
    }

    public void setPromedioAnterior(String promedioAnterior) {
        this.promedioAnterior = promedioAnterior;
    }

    public String getDevuelto() {
        return devuelto;
    }

    public void setDevuelto(String devuelto) {
        this.devuelto = devuelto;
    }

    public int getValorUnidad() {
        return valorUnidad;
    }

    public void setValorUnidad(int valorUnidad) {
        this.valorUnidad = valorUnidad;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public int getEstadoSincronizado() {
        return estadoSincronizado;
    }

    public void setEstadoSincronizado(int estadoSincronizado) {
        this.estadoSincronizado = estadoSincronizado;
    }
}
