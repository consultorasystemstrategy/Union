package union.union_vr1.Utils;

import android.content.Context;
import android.widget.Toast;

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
        Toast.makeText(mContext, mText, Toast.LENGTH_SHORT).show();
    }
}
