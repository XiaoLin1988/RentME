package com.android.jianchen.rentme.activity.search.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.jianchen.rentme.activity.myprojects.adapter.MapMarkerAdapter;
import com.android.jianchen.rentme.activity.root.MainActivity;
import com.android.jianchen.rentme.model.rentme.SkillModel;
import com.android.jianchen.rentme.model.rentme.UserModel;
import com.android.jianchen.rentme.activity.search.TalentDetailActivity;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.helper.network.retrofit.RestClient;
import com.android.jianchen.rentme.helper.network.retrofit.UserClient;
import com.android.jianchen.rentme.helper.utils.BitmapUtil;
import com.android.jianchen.rentme.helper.Constants;
import com.android.jianchen.rentme.helper.utils.DialogUtil;
import com.android.jianchen.rentme.helper.utils.Utils;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by emerald on 6/6/2017.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, Response.Listener<String>, Response.ErrorListener, GoogleMap.OnInfoWindowClickListener {
    private UserModel curUser;
    private Context context;
    private SkillModel skill;
    private ArrayList<UserModel> nearbies;
    private boolean loaded = false;

    @BindString(R.string.error_load)
    String errLoad;

    @BindString(R.string.error_network)
    String errNetwork;

    ArrayMap<Integer, Bitmap> avatars;

    private MapView mMapView;
    private GoogleMap googleMap;

    public static MapFragment newInstance(Context context, SkillModel skill) {
        MapFragment instance = new MapFragment();
        instance.context = context;
        instance.skill = skill;

        return instance;
    }

    public MapFragment() {
        avatars = new ArrayMap<>();
    }

    private void prepareData() {
        if (context == null)
            context = getContext();
        final ProgressDialog dialog = DialogUtil.showProgressDialog(context, "Please wait while searching nearby talents");

        curUser = Utils.retrieveUserInfo(context);

        RestClient<UserClient> restClient = new RestClient<>();
        UserClient userClient = restClient.getAppClient(UserClient.class);

        Call<ArrayList<UserModel>> call = userClient.searchByLocation(curUser.getLatitude(), curUser.getLongitude(), skill.getTitle());
        call.enqueue(new Callback<ArrayList<UserModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserModel>> call, retrofit2.Response<ArrayList<UserModel>> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    nearbies = response.body();

                    if (!loaded && googleMap != null) {
                        putMarkers();
                    }
                } else {
                    Toast.makeText(context, errLoad, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserModel>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, errNetwork, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void putMarkers() {
        Context ctx = null;
        if (context != null)
            ctx = context;
        else if (getContext() != null)
            ctx = getContext();

        for (int i = 0; i < nearbies.size(); i++ ){
            LatLng home = new LatLng(nearbies.get(i).getLatitude(), nearbies.get(i).getLongitude());
            View mark = ((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_map_balloon, null);
            CircularImageView img = (CircularImageView) mark.findViewById(R.id.img_marker);

            if (avatars.containsKey(nearbies.get(i).getId()))
                img.setImageBitmap(avatars.get(nearbies.get(i).getId()));

            Marker marker = googleMap.addMarker(new MarkerOptions().position(home).icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.createDrawableFromView(ctx, mark))).anchor(0.5f, 0.5f));
            marker.setTag(nearbies.get(i));
        }
        loaded = true;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)getContext()).setPageTitel("Select talent");

        prepareData();

        View rootView = inflater.inflate(R.layout.fragment_project_create1, container, false);

        ButterKnife.bind(this, rootView);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;

        googleMap.setInfoWindowAdapter(new MapMarkerAdapter(context));
        googleMap.setOnInfoWindowClickListener(this);

        LatLng home = new LatLng(curUser.getLatitude(), curUser.getLongitude());

        View mark = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_map_balloon, null);
        CircularImageView img = (CircularImageView) mark.findViewById(R.id.img_marker);

        if (avatars.containsKey(curUser.getId()))
            img.setImageBitmap(avatars.get(curUser.getId()));

        Marker marker = googleMap.addMarker(new MarkerOptions().position(home).snippet("ME").icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.createDrawableFromView(getContext(), mark))).anchor(0.5f, 0.5f));
        marker.setTag(curUser);

        if (!loaded && nearbies != null)
            putMarkers();

        CameraPosition cameraPosition = new CameraPosition.Builder().target(home).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onResponse(String response) {
        try {
            nearbies = new ArrayList<>();
            JSONArray result = new JSONArray(response);

            for (int i = 0; i < result.length(); i++) {
                JSONObject obj = result.getJSONObject(i);

                final UserModel u = new UserModel();
                u.setId(obj.getInt("id"));
                u.setAvatar(obj.getString("avatar"));
                u.setName(obj.getString("name"));
                u.setEmail(obj.getString("email"));
                u.setAddress(obj.getString("address"));
                u.setZipcode(obj.getInt("zipcode"));
                u.setPhone(obj.getString("phone"));
                u.setSkills(obj.getString("skills"));
                u.setLatitude(obj.getDouble("latitude"));
                u.setLongitude(obj.getDouble("longitude"));

                u.setDistance(obj.getDouble("distance"));
                nearbies.add(u);
                if (!u.getAvatar().equals("") && u.getAvatar() != null && !u.getAvatar().equals("null")) {
                    
                }
            }

            if (googleMap != null)
                putMarkers();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        UserModel u = (UserModel)marker.getTag();
        if (curUser.equals(u)) {
            marker.hideInfoWindow();
        } else {
            marker.showInfoWindow();
        }
        return true;
        /*
        ((ProjectCreateFragment)getParentFragment()).selectTalent(u);
        */
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        UserModel u = (UserModel)marker.getTag();
        if (curUser.equals(u)) {
            marker.hideInfoWindow();
            return;
        }
        Intent intent = new Intent(context, TalentDetailActivity.class);
        intent.putExtra(Constants.KEY_USER, u);

        startActivityForResult(intent, Constants.REQUEST_PROJECT_CREATE);
        //((ProjectCreateFragment)getParentFragment()).selectTalent(u, skill);
    }
}
