package guru.pmvungu.example.com.guru;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
public class LstViewAdapter extends ArrayAdapter<String> {
    int groupid;
    String[] item_list;
    ArrayList<String> desc;
    Context context;
    public LstViewAdapter(Context context, int vg, int id, String[] item_list){
        super(context,vg, id, item_list);
        this.context=context;
        groupid=vg;
        this.item_list=item_list;

    }
    // Hold views of the ListView to improve its scrolling performance
    static class ViewHolder {
        public TextView vismsi;
        public TextView vsmsisdn;
        public TextView vdate;
         public TextView vnombre;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        // Inflate the rowlayout.xml file if convertView is null
        if(rowView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView= inflater.inflate(groupid, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.vdate= (TextView) rowView.findViewById(R.id.tvdate);
            viewHolder.vsmsisdn= (TextView) rowView.findViewById(R.id.tvmsisdn);
            viewHolder.vismsi= (TextView) rowView.findViewById(R.id.tvimsi);
            rowView.setTag(viewHolder);

        }
        // Set text to each TextView of ListView item
        String[] items=item_list[position].split(";");
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.vdate.setText(items[0]);
        holder.vsmsisdn.setText(items[1]);
        holder.vismsi.setText(items[2]);

        return rowView;
    }

}