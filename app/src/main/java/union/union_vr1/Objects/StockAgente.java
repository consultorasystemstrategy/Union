package union.union_vr1.Objects;

/**
 * Created by Usuario on 15/01/2015.
 */
public class StockAgente {

    private int idProducto;
    private String nombre;
    private String codigo;
    private String codigoBarras;
    private int stockInicial;
    private int stockFinal;
    private int disponible;
    private int ventas;
    private int canjes;
    private int devoluciones;
    private int buenos;
    private int malos;
    private int fisico;
    private int idAgente;
    private int estadoSincronizacion;

    public StockAgente(int idProducto, String nombre, String codigo, String codigoBarras, int stockInicial, int stockFinal, int disponible, int ventas, int canjes, int devoluciones, int buenos, int malos, int fisico, int idAgente, int estadoSincronizacion) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.codigo = codigo;
        this.codigoBarras = codigoBarras;
        this.stockInicial = stockInicial;
        this.stockFinal = stockFinal;
        this.disponible = disponible;
        this.ventas = ventas;
        this.canjes = canjes;
        this.devoluciones = devoluciones;
        this.buenos = buenos;
        this.malos = malos;
        this.fisico = fisico;
        this.idAgente = idAgente;
        this.estadoSincronizacion = estadoSincronizacion;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public int getStockInicial() {
        return stockInicial;
    }

    public void setStockInicial(int stockInicial) {
        this.stockInicial = stockInicial;
    }

    public int getStockFinal() {
        return stockFinal;
    }

    public void setStockFinal(int stockFinal) {
        this.stockFinal = stockFinal;
    }

    public int getDisponible() {
        return disponible;
    }

    public void setDisponible(int disponible) {
        this.disponible = disponible;
    }

    public int getVentas() {
        return ventas;
    }

    public void setVentas(int ventas) {
        this.ventas = ventas;
    }

    public int getCanjes() {
        return canjes;
    }

    public void setCanjes(int canjes) {
        this.canjes = canjes;
    }

    public int getDevoluciones() {
        return devoluciones;
    }

    public void setDevoluciones(int devoluciones) {
        this.devoluciones = devoluciones;
    }

    public int getBuenos() {
        return buenos;
    }

    public void setBuenos(int buenos) {
        this.buenos = buenos;
    }

    public int getMalos() {
        return malos;
    }

    public void setMalos(int malos) {
        this.malos = malos;
    }

    public int getFisico() {
        return fisico;
    }

    public void setFisico(int fisico) {
        this.fisico = fisico;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public int getEstadoSincronizacion() {
        return estadoSincronizacion;
    }

    public void setEstadoSincronizacion(int estadoSincronizacion) {
        this.estadoSincronizacion = estadoSincronizacion;
    }
}
