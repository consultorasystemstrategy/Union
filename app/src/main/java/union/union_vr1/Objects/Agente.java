package union.union_vr1.Objects;

/**
 * Created by Usuario on 15/01/2015.
 */
public class Agente {

    private int idAgenteVenta;
    private int idEmpresa;
    private int idUsuario;
    private String nombreAgente;
    private String nombreUsuario;
    private String passUsuario;
    private int liquidacion;
    private Double kmInicial;
    private Double kmFinal;
    private String nombreRuta;
    private int nroBodegas;
    private String serieBoleta;
    private String serieFactura;
    private String serieRrpp;
    private int correlativoBoleta;
    private int correlativoFactura;
    private int correlativoRrpp;
    private int estadoSincronizado;
    private String MAC;

    public int getRutaId() {
        return rutaId;
    }

    public void setRutaId(int rutaId) {
        this.rutaId = rutaId;
    }

    private int rutaId;
    private int rolid;

    public Agente(int idAgenteVenta, int idEmpresa, int idUsuario, String nombreAgente, String nombreUsuario, String passUsuario, int liquidacion, Double kmInicial, Double kmFinal, String nombreRuta, int nroBodegas, String serieBoleta, String serieFactura, String serieRrpp, int correlativoBoleta, int correlativoFactura, int correlativoRrpp, String MAC, int rolid, int estadoSincronizado,int rutaId) {
        this.idAgenteVenta = idAgenteVenta;
        this.idEmpresa = idEmpresa;
        this.idUsuario = idUsuario;
        this.nombreAgente = nombreAgente;
        this.nombreUsuario = nombreUsuario;
        this.passUsuario = passUsuario;
        this.liquidacion = liquidacion;
        this.kmInicial = kmInicial;
        this.kmFinal = kmFinal;
        this.nombreRuta = nombreRuta;
        this.nroBodegas = nroBodegas;
        this.serieBoleta = serieBoleta;
        this.serieFactura = serieFactura;
        this.serieRrpp = serieRrpp;
        this.correlativoBoleta = correlativoBoleta;
        this.correlativoFactura = correlativoFactura;
        this.correlativoRrpp = correlativoRrpp;
        this.estadoSincronizado = estadoSincronizado;
        this.MAC = MAC;
        this.rolid = rolid;
        this.rutaId = rutaId;
    }


    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public int getRolid() {
        return rolid;
    }

    public void setRolid(int rolid) {
        this.rolid = rolid;
    }

    public int getIdAgenteVenta() {
        return idAgenteVenta;
    }

    public void setIdAgenteVenta(int idAgenteVenta) {
        this.idAgenteVenta = idAgenteVenta;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreAgente() {
        return nombreAgente;
    }

    public void setNombreAgente(String nombreAgente) {
        this.nombreAgente = nombreAgente;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassUsuario() {
        return passUsuario;
    }

    public void setPassUsuario(String passUsuario) {
        this.passUsuario = passUsuario;
    }

    public int getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(int liquidacion) {
        this.liquidacion = liquidacion;
    }

    public Double getKmInicial() {
        return kmInicial;
    }

    public void setKmInicial(Double kmInicial) {
        this.kmInicial = kmInicial;
    }

    public Double getKmFinal() {
        return kmFinal;
    }

    public void setKmFinal(Double kmFinal) {
        this.kmFinal = kmFinal;
    }

    public String getNombreRuta() {
        return nombreRuta;
    }

    public void setNombreRuta(String nombreRuta) {
        this.nombreRuta = nombreRuta;
    }

    public int getNroBodegas() {
        return nroBodegas;
    }

    public void setNroBodegas(int nroBodegas) {
        this.nroBodegas = nroBodegas;
    }

    public String getSerieBoleta() {
        return serieBoleta;
    }

    public void setSerieBoleta(String serieBoleta) {
        this.serieBoleta = serieBoleta;
    }

    public String getSerieFactura() {
        return serieFactura;
    }

    public void setSerieFactura(String serieFactura) {
        this.serieFactura = serieFactura;
    }

    public String getSerieRrpp() {
        return serieRrpp;
    }

    public void setSerieRrpp(String serieRrpp) {
        this.serieRrpp = serieRrpp;
    }

    public int getCorrelativoBoleta() {
        return correlativoBoleta;
    }

    public void setCorrelativoBoleta(int correlativoBoleta) {
        this.correlativoBoleta = correlativoBoleta;
    }

    public int getCorrelativoFactura() {
        return correlativoFactura;
    }

    public void setCorrelativoFactura(int correlativoFactura) {
        this.correlativoFactura = correlativoFactura;
    }

    public int getCorrelativoRrpp() {
        return correlativoRrpp;
    }

    public void setCorrelativoRrpp(int correlativoRrpp) {
        this.correlativoRrpp = correlativoRrpp;
    }

    public int getEstadoSincronizado() {
        return estadoSincronizado;
    }

    public void setEstadoSincronizado(int estadoSincronizado) {
        this.estadoSincronizado = estadoSincronizado;
    }
}
