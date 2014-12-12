package union.union_vr1.Utils;


import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import union.union_vr1.R;
import union.union_vr1.Vistas.VMovil_Evento_Indice;


/**
 * Created by CCIE on 11/12/2014.
 */
public class DialogSincronizarOffLine extends DialogFragment implements View.OnClickListener{

    Button aceptarSinc,cancelarSinc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.infor_dialog_sinc_offline,null);
        aceptarSinc = (Button)view.findViewById(R.id.BTN_aceptarDialogSinc);
        cancelarSinc = (Button)view.findViewById(R.id.BTN_cancelarDialogSinc);
        aceptarSinc.setOnClickListener(this);
        cancelarSinc.setOnClickListener(this);
        setCancelable(false);
        return view;
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.BTN_aceptarDialogSinc){
            dismiss();
            Intent i = new Intent(getActivity(),VMovil_Evento_Indice.class);
            startActivity(i);
            Toast.makeText(getActivity(),"Sincronizando Datos de Online a Ofline . . .",Toast.LENGTH_LONG).show();
        }else{
            dismiss();
            Toast.makeText(getActivity(),"Cancelado",Toast.LENGTH_LONG).show();
        }

    }



}
