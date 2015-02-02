package union.union_vr1.Utils;

import android.app.Application;

import union.union_vr1.Vistas.VMovil_Online_Pumovil;

/**
 * Created by Usuario on 15/12/2014.
 */
public class MyApplication extends Application{

        private int idAgente;
        private int idEstablecimiento;
        private boolean cuotasEstablecidas;
        private boolean DisplayedHistorialComprobanteAnterior;
        private int idLiquidacion;
        private int idUsuario;
        private VMovil_Online_Pumovil mainActivity;

    public VMovil_Online_Pumovil getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(VMovil_Online_Pumovil mainActivity) {
        this.mainActivity = mainActivity;
    }

    public boolean isCuotasEstablecidas() {
        return cuotasEstablecidas;
    }

    public void setCuotasEstablecidas(boolean cuotasEstablecidas) {
        this.cuotasEstablecidas = cuotasEstablecidas;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdLiquidacion() {
        return idLiquidacion;
    }

    public void setIdLiquidacion(int idLiquidacion) {
        this.idLiquidacion = idLiquidacion;
    }

    public boolean isDisplayedHistorialComprobanteAnterior() {
        return DisplayedHistorialComprobanteAnterior;
    }

    public void setDisplayedHistorialComprobanteAnterior(boolean displayedHistorialComprobanteAnterior) {
        DisplayedHistorialComprobanteAnterior = displayedHistorialComprobanteAnterior;
    }

    public int getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(int idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public int getIdAgente() {
            return idAgente;
        }

        public void setIdAgente(int idAgente) {
            this.idAgente = idAgente;
        }
}
