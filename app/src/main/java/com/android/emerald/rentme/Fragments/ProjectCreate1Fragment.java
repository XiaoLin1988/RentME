package com.android.emerald.rentme.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.emerald.rentme.Adapter.MapMarkerAdapter;
import com.android.emerald.rentme.AppController;
import com.android.emerald.rentme.MainActivity;
import com.android.emerald.rentme.Models.SkillModel;
import com.android.emerald.rentme.Models.UserModel;
import com.android.emerald.rentme.R;
import com.android.emerald.rentme.Task.APIStringRequester;
import com.android.emerald.rentme.Utils.BitmapUtil;
import com.android.emerald.rentme.Utils.Constants;
import com.android.emerald.rentme.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
//import com.github.siyamed.shapeimageview.CircularImageView;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by emerald on 6/6/2017.
 */
public class ProjectCreate1Fragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, Response.Listener<String>, Response.ErrorListener, GoogleMap.OnInfoWindowClickListener {

    private UserModel curUser;
    private Context context;
    private SkillModel skill;
    private ArrayList<UserModel> nearbies;

    ArrayMap<Integer, Bitmap> avatars;

    private MapView mMapView;
    private GoogleMap googleMap;

    public static ProjectCreate1Fragment newInstance(Context context, SkillModel skill) {
        ProjectCreate1Fragment instance = new ProjectCreate1Fragment();
        instance.context = context;
        instance.skill = skill;
        instance.prepareData();
        return instance;
    }

    public ProjectCreate1Fragment() {
        avatars = new ArrayMap<>();
    }

    private void prepareData() {
        curUser = Utils.retrieveUserInfo(context);
        if (!curUser.getAvatar().equals("") && !curUser.getAvatar().equals("null") && curUser.getAvatar() != null) {
            Picasso.with(context).load(curUser.getAvatar()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    avatars.put(curUser.getId(), bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }

        String url = Constants.API_ROOT_URL + Constants.API_USER_SEARCH_BY_LOCATION;

        Map<String, String> params = new HashMap<>();
        params.put("latitude", Double.toString(curUser.getLatidue()));
        params.put("longitude", Double.toString(curUser.getLongitude()));
        params.put("skill", skill.getTitle());
        params.put("radius", Integer.toString(Constants.SEARCH_RADIUS));

        APIStringRequester requester = new APIStringRequester(Request.Method.POST, url, params, this, this);
        AppController.getInstance().addToRequestQueue(requester, Constants.JSON_REQUEST);
    }

    public Bitmap resizeMapIcons(Bitmap bmp, int width, int height) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmp, width, height, false);
        return resizedBitmap;
    }

    public Bitmap resizeMapIcons(int rid, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile_empty);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    private void putMarkers() {
        Context ctx = null;
        if (context != null)
            ctx = context;
        else if (getContext() != null)
            ctx = getContext();

        for (int i = 0; i < nearbies.size(); i++ ){
            LatLng home = new LatLng(nearbies.get(i).getLatidue(), nearbies.get(i).getLongitude());
            View mark = ((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_map_balloon, null);
            //CircularImageView img = (CircularImageView)mark.findViewById(R.id.img_marker);
            CircleImageView img = (CircleImageView)mark.findViewById(R.id.img_marker);

            if (avatars.containsKey(nearbies.get(i).getId()))
                img.setImageBitmap(avatars.get(nearbies.get(i).getId()));

            Marker marker = googleMap.addMarker(new MarkerOptions().position(home).icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.createDrawableFromView(ctx, mark))).anchor(0.5f, 0.5f));
            marker.setTag(nearbies.get(i));
        }
    }

    private void replaceWithAvatar(Bitmap bmp, UserModel u) {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)getContext()).setPageTitel("Select talent");

        View rootView = inflater.inflate(R.layout.fragment_project_create1, container, false);

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

        //prepareData();

        googleMap.setInfoWindowAdapter(new MapMarkerAdapter(context));
        googleMap.setOnInfoWindowClickListener(this);

        LatLng home = new LatLng(curUser.getLatidue(), curUser.getLongitude());

        View mark = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_map_balloon, null);
        //CircularImageView img = (CircularImageView)mark.findViewById(R.id.img_marker);
        CircleImageView img = (CircleImageView)mark.findViewById(R.id.img_marker);

        if (avatars.containsKey(curUser.getId()))
            img.setImageBitmap(avatars.get(curUser.getId()));

        Marker marker = googleMap.addMarker(new MarkerOptions().position(home).snippet("ME").icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.createDrawableFromView(getContext(), mark))).anchor(0.5f, 0.5f));
        marker.setTag(curUser);


        if (nearbies != null)
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
                u.setWorkday(obj.getString("workday"));
                u.setWorktime(obj.getInt("worktime"));
                u.setRate(obj.getDouble("rate"));
                u.setLatidue(obj.getDouble("latitude"));
                u.setLongitude(obj.getDouble("longitude"));
                u.setDistance(obj.getDouble("distance"));
                nearbies.add(u);
                if (!u.getAvatar().equals("") && u.getAvatar() != null && !u.getAvatar().equals("null")) {
                    Picasso.with(context).load(u.getAvatar()).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            avatars.put(u.getId(), bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
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
        ((ProjectCreateFragment)getParentFragment()).selectTalent(u, skill);
    }
}
