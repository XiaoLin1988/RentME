package com.android.jianchen.rentme.activity.search;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.activity.myprojects.adapter.MapMarkerAdapter;
import com.android.jianchen.rentme.helper.Constants;
import com.android.jianchen.rentme.helper.network.retrofit.RestClient;
import com.android.jianchen.rentme.helper.network.retrofit.UserClient;
import com.android.jianchen.rentme.helper.utils.BitmapUtil;
import com.android.jianchen.rentme.helper.utils.DialogUtil;
import com.android.jianchen.rentme.helper.utils.Utils;
import com.android.jianchen.rentme.model.rentme.SkillModel;
import com.android.jianchen.rentme.model.rentme.UserModel;
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

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by emerald on 12/15/2017.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.mapView)
    MapView mMapView;
    GoogleMap googleMap;

    @BindString(R.string.error_load)
    String errLoad;

    @BindString(R.string.error_network)
    String errNetwork;

    UserModel curUser;
    private SkillModel skill;
    private ArrayList<UserModel> nearbies;
    ArrayMap<Integer, Bitmap> avatars;
    private boolean loaded = false;

    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        skill = (SkillModel)getIntent().getSerializableExtra(Constants.KEY_SKILL);

        curUser = Utils.retrieveUserInfo(this);

        mMapView.onCreate(saveBundle);
        mMapView.onResume();
        MapsInitializer.initialize(this);
        mMapView.getMapAsync(this);

        avatars = new ArrayMap<>();
        nearbies = new ArrayList<>();

        prepareActionBar();

        prepareData();
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

    private void prepareData() {
        final ProgressDialog dialog = DialogUtil.showProgressDialog(this, "Please wait while searching nearby talents");

        curUser = Utils.retrieveUserInfo(this);

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
                    Toast.makeText(MapActivity.this, errLoad, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserModel>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(MapActivity.this, errNetwork, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareActionBar() {
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle("SELECT TALENT");
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;

        googleMap.setInfoWindowAdapter(new MapMarkerAdapter(this));
        googleMap.setOnInfoWindowClickListener(this);

        LatLng home = new LatLng(curUser.getLatitude(), curUser.getLongitude());

        View mark = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_map_balloon, null);
        CircularImageView img = (CircularImageView) mark.findViewById(R.id.img_marker);

        if (avatars.containsKey(curUser.getId()))
            img.setImageBitmap(avatars.get(curUser.getId()));

        Marker marker = googleMap.addMarker(new MarkerOptions().position(home).snippet("ME").icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.createDrawableFromView(this, mark))).anchor(0.5f, 0.5f));
        marker.setTag(curUser);

        if (!loaded && nearbies.size() > 0)
            putMarkers();

        CameraPosition cameraPosition = new CameraPosition.Builder().target(home).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        googleMap.setOnMarkerClickListener(this);
    }

    private void putMarkers() {
        for (int i = 0; i < nearbies.size(); i++ ){
            LatLng home = new LatLng(nearbies.get(i).getLatitude(), nearbies.get(i).getLongitude());
            View mark = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_map_balloon, null);
            CircularImageView img = (CircularImageView) mark.findViewById(R.id.img_marker);

            if (avatars.containsKey(nearbies.get(i).getId()))
                img.setImageBitmap(avatars.get(nearbies.get(i).getId()));

            Marker marker = googleMap.addMarker(new MarkerOptions().position(home).icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.createDrawableFromView(this, mark))).anchor(0.5f, 0.5f));
            marker.setTag(nearbies.get(i));
        }
        loaded = true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        UserModel u = (UserModel)marker.getTag();
        if (curUser.equals(u)) {
            marker.hideInfoWindow();
            return;
        }
        Intent intent = new Intent(this, TalentDetailActivity.class);
        intent.putExtra(Constants.KEY_USER, u);

        startActivityForResult(intent, Constants.REQUEST_PROJECT_CREATE);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
