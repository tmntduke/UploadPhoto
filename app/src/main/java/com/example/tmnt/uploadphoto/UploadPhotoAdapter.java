package com.example.tmnt.uploadphoto;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by tmnt on 2016/8/19.
 */
public class UploadPhotoAdapter extends BaseAdapter {

    private Context mContext;
    private List<Drawable> lists;
    private static final String TAG = "UploadPhotoAdapter";

    public UploadPhotoAdapter(Context context, List<Drawable> lists) {
        mContext = context;
        this.lists = lists;
    }

    public void getData(List<Drawable> lists) {
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        Log.i(TAG, "getView: " + lists);

        if (convertView == null) {

            view = LayoutInflater.from(mContext).inflate(R.layout.gv_photo_item_lay, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.photo = (ImageView) view.findViewById(R.id.picture);
            viewHolder.delete = (Button) view.findViewById(R.id.delete);

            view.setTag(viewHolder);

        } else {

            view = convertView;
            viewHolder = (ViewHolder) view.getTag();

        }

        if ((position == lists.size()) || lists.size() == 0) {
            viewHolder.photo.setImageResource(R.drawable.pic_addtype);
            viewHolder.delete.setVisibility(View.GONE);
        }

        if (lists.size() != 0 && position < lists.size()) {
            viewHolder.photo.setImageDrawable(lists.get(position));
            viewHolder.delete.setVisibility(View.VISIBLE);
        }


        if (sOnClickDelete != null) {
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sOnClickDelete.onClickDelete(v,position);
                }
            });
        }

        return view;
    }

    public OnClickDelete sOnClickDelete;

    public void setOnClickDelete(OnClickDelete onClickDelete) {
        this.sOnClickDelete = onClickDelete;
    }

    public interface OnClickDelete {
        void onClickDelete(View view,int position);
    }

    class ViewHolder {
        ImageView photo;
        Button delete;

    }

}
