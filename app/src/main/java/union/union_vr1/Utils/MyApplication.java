package union.union_vr1.Utils;

import android.app.Application;

/**
 * Created by Usuario on 15/12/2014.
 */
public class MyApplication extends Application{

        private int idAgente;
        private int idEstablecimiento;

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
