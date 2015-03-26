package union.union_vr1.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

import union.union_vr1.R;

/**
 * Created by Usuario on 24/03/2015.
 */
public class Keyboard  extends LinearLayout {

    private Context context;
        public Keyboard(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.context = context;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.princ_venta_cabecera, this);
        }

    /*
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            Log.d("Search Layout", "Handling Keyboard Window shown");

            final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
            final int actualHeight = getHeight();

            if (actualHeight > proposedheight){
                // Keyboard is shown

                Log.d("KEYBOARD", "SHOW");
                //Toast.makeText(context,"KEYBOARD SHOW",Toast.LENGTH_LONG).show();
            } else {
                // Keyboard is hidden
                Log.d("KEYBOARD", "HIDE");
                //Toast.makeText(context,"KEYBOARD HIDE",Toast.LENGTH_LONG).show();
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
*/
}
