package sh.slst.anroidtv;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class ToiletAdapter extends BaseAdapter {

    private List<ToiletSite> sites;
    private Context context;

    public ToiletAdapter(Context context, List<ToiletSite> sites) {
        /**
        * Created by SH on 2018/3/1.
        */
        this.context = context;
        this.sites = new ArrayList<>();
        this.sites.addAll(sites);
    }

    @Override
    public int getCount() {
        return sites.size();
    }

    @Override
    public Object getItem(int position) {
        return sites.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToiletSite site = sites.get(position);
        ViewTag viewTag;
        if (convertView == null) {
            viewTag = new ViewTag();
            convertView = View.inflate(context, R.layout.item_gv, null);
            viewTag.ivUsing =(ImageView)convertView.findViewById(R.id.iv_using);
            viewTag.tvNumber =(TextView)convertView.findViewById(R.id.tv_number);
            convertView.setTag(viewTag);
        } else {
            viewTag = (ViewTag) convertView.getTag();
        }
        viewTag.ivUsing.setImageResource(site.getImageId());
        String number = (site.getNumber()) + "";
        viewTag.tvNumber.setText(number);
        return convertView;
    }
    class ViewTag {
        ImageView ivUsing;
        TextView tvNumber;
    }
}
