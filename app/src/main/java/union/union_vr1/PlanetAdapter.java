package union.union_vr1;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlanetAdapter extends ArrayAdapter<Planet> {

    private List<Planet> planetList;
    private Context context;



    public PlanetAdapter(List<Planet> planetList, Context ctx) {
        super(ctx, R.layout.row_layout, planetList);
        this.planetList = planetList;
        this.context = ctx;
    }

    public int getCount() {
        return planetList.size();
    }

    public Planet getItem(int position) {
        return planetList.get(position);
    }

    public long getItemId(int position) {
        return planetList.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        PlanetHolder holder = new PlanetHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.row_layout, null);
            // Now we can fill the layout with the right values
            TextView tv = (TextView) v.findViewById(R.id.name);

            holder.planetNameView = tv;

            v.setTag(holder);

            //if ( position % 2 == 0)
            //    v.setBackgroundResource(R.drawable.listview_selector_even);
            //else
            //    v.setBackgroundResource(R.drawable.listview_selector_odd);
            Planet ps = planetList.get(position);
            if(ps.getDescr().equals("1")){
                v.setBackgroundColor(Color.BLUE);
            }
            if(ps.getDescr().equals("2")){
                v.setBackgroundColor(Color.GREEN);
            }
            if(ps.getDescr().equals("3")){
                v.setBackgroundColor(Color.RED);
            }
            if(ps.getDescr().equals("4")){
                v.setBackgroundColor(Color.YELLOW);
            }
            //if(idEstEst == 1){
            //    view.setBackgroundColor(Color.BLUE);
            //}
            //if(idEstEst == 2){
            //    view.setBackgroundColor(Color.GREEN);
            //}
            //if(idEstEst == 3){
            //    view.setBackgroundColor(Color.RED);
            //}
            //if(idEstEst == 4){
            //    view.setBackgroundColor(Color.YELLOW);
            //}
        }
        else
            holder = (PlanetHolder) v.getTag();

        Planet p = planetList.get(position);
        holder.planetNameView.setText(p.getName());

        return v;
    }

	/* *********************************
	 * We use the holder pattern
	 * It makes the view faster and avoid finding the component
	 * **********************************/

    private static class PlanetHolder {
        public TextView planetNameView;
    }


}