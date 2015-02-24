package union.union_vr1.Utils;


import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.MySQL.DbManager_Agente_GET;
import union.union_vr1.R;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;


/**
 * Created by CCIE on 11/12/2014.
 */
public class DialogSincronizarOffLine extends DialogFragment implements View.OnClickListener {

    Button aceptarSinc, cancelarSinc;
    DbAdapter_Agente dbHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.infor_dialog_sinc_offline, null);
        aceptarSinc = (Button) view.findViewById(R.id.BTN_aceptarDialogSinc);
        cancelarSinc = (Button) view.findViewById(R.id.BTN_cancelarDialogSinc);
        aceptarSinc.setOnClickListener(this);
        cancelarSinc.setOnClickListener(this);
        setCancelable(false);
        dbHelper = new DbAdapter_Agente(this.getActivity());
        dbHelper.open();
        return view;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.BTN_aceptarDialogSinc) {
            dismiss();
            Intent i = new Intent(getActivity(), DbManager_Agente_GET.class);
            i.putExtra("putIdAgenteVenta", displayAgenteById());
            startActivity(i);
            Toast.makeText(getActivity(), "Sincronizando Datos de Online a Offline . . .", Toast.LENGTH_LONG).show();
        } else {
            dismiss();
            Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_LONG).show();
        }

    }

    public String displayAgenteById() {
        Cursor cursor = dbHelper.fetchAllAgentesVenta();
        String id_agente_venta = cursor.getString(cursor.getColumnIndex(DbAdapter_Agente.AG_id_agente_venta));
        return id_agente_venta;
    }


}
