package sh.slst.anroidtv;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by liuchengran on 2018/6/25.
 */

public class DebugMessageAdapter extends BaseAdapter {

    private List<String> mListData;
    private Context mContext;

    public DebugMessageAdapter(Context context, List<String> listData) {
        mListData = listData;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mListData == null ? 0 : mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_debug_info, null);
            holder = new ViewHolder();
            holder.txtDebug =(TextView) convertView.findViewById(R.id.txt_debug);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.txtDebug.setText(mListData.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView txtDebug;
    }
}
