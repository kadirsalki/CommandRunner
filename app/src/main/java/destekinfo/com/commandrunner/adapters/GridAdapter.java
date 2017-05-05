package destekinfo.com.commandrunner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import destekinfo.com.commandrunner.R;
import destekinfo.com.commandrunner.host.AppList;


public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private AppList appList;
    public GridAdapter(Context context, AppList appList) {
        mContext = context;
        this.appList = appList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return appList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_item, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_item_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_item_image);

            if(appList.get(position).getType() == 0){
                textView.setText(appList.get(position).getHostName() +" "+appList.get(position).getAppName());
                imageView.setImageResource(R.drawable.host);
            }else{
                textView.setText(appList.get(position).getAppName());
                imageView.setImageResource(R.drawable.deploy);
            }

        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}