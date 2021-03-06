package com.jianchen.rentme.activity.myprojects.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jianchen.rentme.model.rentme.UserModel;
import com.jianchen.rentme.R;
import com.jianchen.rentme.helper.utils.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by emerald on 6/9/2017.
 */
public class MapMarkerAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    private View mView;

    public MapMarkerAdapter(Context context){
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
        mView = inflater.inflate(R.layout.row_map_marker, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        UserModel curUser = Utils.retrieveUserInfo(context);
        final UserModel user = (UserModel) marker.getTag();
        if (curUser.equals(user)) {
            return null;
        } else {
            TextView txtName = (TextView)mView.findViewById(R.id.txt_marker_name);
            txtName.setText(user.getName());

            TextView txtDistance = (TextView)mView.findViewById(R.id.txt_marker_distance);
            txtDistance.setText(Double.toString(user.getDistance()));
        }

        return mView;
    }
}
