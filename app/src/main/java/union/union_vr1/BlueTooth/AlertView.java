package union.union_vr1.BlueTooth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Usuario on 10/11/2015.
 */
public class AlertView
{
    public static void showAlert(String message, Context ctx)
    {
        //Create a builder
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Alert");
        builder.setMessage(message);
        //add buttons and listener
        //PromptListener pl = new EmptyListener();
        EmptyListener pl = new EmptyListener();
        builder.setPositiveButton("OK", pl);
        //Create the dialog
        AlertDialog ad = builder.create();
        //show
        ad.show();
    }
}

class EmptyListener implements android.content.DialogInterface.OnClickListener
{
    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        // TODO Auto-generated method stub
    }
}
