package me.donggu.model;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import me.donggu.electongji.R;

import java.util.List;

/**
 * Created by Donggu on 2016/11/20.
 */
public class ListIthemAdapter extends ArrayAdapter<ListItem> {
    private int resourceTextId;
    public ListIthemAdapter(Context context, @LayoutRes int resource, @NonNull List<ListItem> objects) {
        super(context, resource, objects);
        resourceTextId=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItem item = (ListItem)getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceTextId,null);
            viewHolder=new ViewHolder();
            viewHolder.text=(TextView)view.findViewById(R.id.list_item);
            view.setTag(viewHolder);
        }
        else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }
        viewHolder.text.setText(item.getContent());
        return view;
    }

    private static class ViewHolder{
        TextView text;
    }
}
