package union.union_vr1.Utils;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import union.union_vr1.R;

/**
 * Created by Usuario on 27/12/2015.
 */
public class DisplayToast implements Runnable {

    private final Context mContext;
    String mText;

    public DisplayToast(Context mContext, String text){
        this.mContext = mContext;
        mText = text;
    }

    public void run(){
        Toast toast = Toast.makeText(mContext, mText, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundColor(mContext.getResources().getColor(R.color.verde));
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(mContext.getResources().getColor(R.color.Blanco));
        toast.show();
    }
}
