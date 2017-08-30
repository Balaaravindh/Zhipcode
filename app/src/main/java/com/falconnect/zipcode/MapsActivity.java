package com.falconnect.zipcode;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.falconnect.zipcode.MapModules.GPSTracker;
import com.falconnect.zipcode.SessionManager.JsonObject;
import com.falconnect.zipcode.SessionManager.Orgin_destination_identy;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.Sprite;
import com.mapbox.mapboxsdk.annotations.SpriteFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MapsActivity extends AppCompatActivity {

    String errand_ids;
    String destination_id;
    HashMap<String, String> orgin_Datas;
    HashMap<String, String> datas_desti;
    HashMap<Integer, ArrayList<String>> datas_desti_multi;
    ArrayList<String> multi_lat;
    ArrayList<String> multi_long;
    String destination_size;
    RelativeLayout mNavigation , mNavigation_second;
    Button red_button, green_button, gray_button;
    LinearLayout buttons_layouts;
    ArrayList<HashMap<Integer, ArrayList<String>>> values;
    TextView address_text, destination_position, address_text1, address_text4;
    TextView address_text4_second;
    ImageView call_image, sms_image, whatsapp_image;
    TextView positions;
    LinearLayout zhipcode, contacto;

    String json_object;

    JsonObject jsonObjects;
    HashMap<String, String> jsonObject_data;

    //MAp REZISE
    RelativeLayout map_first_screen;

    String was_picked, status;


    //
    GPSTracker gps;
    ArrayList<HashMap<Integer, ArrayList<String>>> new_all_datas;

    double latitude;
    double longitude;
    String origin, destination;
    RelativeLayout ruta, call_layout;

    String number;

    Orgin_destination_identy orgin_destination_identy;
    HashMap<String, String> identity_string;

    MapView mapView;

    RelativeLayout image_blue;
    //Permission
    public static final int RequestPermissionCode = 1;
    RelativeLayout map_fragment, indexs, indexs_second;

    TextView address_text11, contacto_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ActivityCompat.requestPermissions(MapsActivity.this, new String[]
                {
                        WRITE_EXTERNAL_STORAGE,
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION,
                        CALL_PHONE
                }, RequestPermissionCode);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);

        orgin_destination_identy = new Orgin_destination_identy(MapsActivity.this);
        identity_string = orgin_destination_identy.getUserDetails();

        ////////////////GET VALUES
        orgin_Datas = (HashMap<String, String>) getIntent().getSerializableExtra("origin_datas");
        datas_desti = (HashMap<String, String>) getIntent().getSerializableExtra("datas_desti");
        int j =0;
        String Storedata = PreferenceManager.getDefaultSharedPreferences(this).getString("destinations", "null");
        Log.e("Storedata", Storedata.toString());
        if(Storedata == "null") {
            datas_desti_multi = (HashMap<Integer, ArrayList<String>>) getIntent().getSerializableExtra("datas_desti_multi");
        } else {
            HashMap<Integer, ArrayList<String>>  datas_desti_multi2 = (HashMap<Integer, ArrayList<String>>) getIntent().getSerializableExtra("datas_desti_multi");
            datas_desti_multi = new HashMap<Integer, ArrayList<String>>();
            for(int i=0;i<datas_desti_multi2.size();i++)
            {
                Log.e("ID", datas_desti_multi2.get(i).get(0).toString());
                if(!Storedata.contains(datas_desti_multi2.get(i).get(0)))
                {
                    datas_desti_multi.put(j,datas_desti_multi2.get(i));
                    j++;
                }
            }
        }


        multi_lat = getIntent().getStringArrayListExtra("multi_lat");
        multi_long = getIntent().getStringArrayListExtra("multi_long");

        errand_ids = getIntent().getStringExtra("errand_ids");
        destination_id = getIntent().getStringExtra("destination_id");
        destination_size = getIntent().getStringExtra("destination_size");

        was_picked = getIntent().getStringExtra("was_picked");
        status = getIntent().getStringExtra("status");
        json_object = getIntent().getStringExtra("json_object");

        Log.e("datas_desti_multi", datas_desti_multi.toString());


        if (json_object == null){
            jsonObjects = new JsonObject(MapsActivity.this);
            jsonObject_data = jsonObjects.getjson_object();
            json_object = jsonObject_data.get("json_object");
        }else{
            json_object = getIntent().getStringExtra("json_object");
        }

        if(values == null){

        }else{

        }

        if (datas_desti == null) {
        } else {
            //destination = datas_desti.get("community_desti");
        }
        if (orgin_Datas == null) {
        } else {
            origin = orgin_Datas.get("community");
        }
        if (datas_desti_multi == null) {

        } else {

        }

        if (multi_lat == null) {

        } else {
            Log.e("multi_lat", multi_lat.get(0));
        }

        if (multi_long == null) {

        } else {
            Log.e("multi_long", multi_long.get(0));
        }

        if (destination_size == null) {

        } else {

        }

        gps = new GPSTracker(MapsActivity.this);

        mNavigation = (RelativeLayout) findViewById(R.id.mNavigation);
        mNavigation_second = (RelativeLayout) findViewById(R.id.mNavigation_second);
        red_button = (Button) findViewById(R.id.red_button);

        buttons_layouts = (LinearLayout) findViewById(R.id.buttons_layouts);
        call_layout = (RelativeLayout) findViewById(R.id.call_layout);
        image_blue = (RelativeLayout) findViewById(R.id.image_blue);
        green_button = (Button) findViewById(R.id.green_button);
        gray_button = (Button) findViewById(R.id.gray_button);
        ruta = (RelativeLayout) findViewById(R.id.ruta);

        map_first_screen = (RelativeLayout) findViewById(R.id.map_first_screen);

        map_fragment = (RelativeLayout) findViewById(R.id.map_fragment);

        indexs = (RelativeLayout) findViewById(R.id.indexs);
        indexs_second = (RelativeLayout) findViewById(R.id.indexs_second);

        address_text = (TextView) findViewById(R.id.address_text);
        destination_position = (TextView) findViewById(R.id.destination_position);
        address_text1 = (TextView) findViewById(R.id.address_text1);
        address_text11 = (TextView) findViewById(R.id.address_text11);
        address_text4 = (TextView) findViewById(R.id.address_text4);
        address_text4_second = (TextView) findViewById(R.id.address_text4_second);

        contacto_name  = (TextView) findViewById(R.id.contacto_name);
        positions = (TextView) findViewById(R.id.positions);

        contacto = (LinearLayout) findViewById(R.id.contacto);
        zhipcode = (LinearLayout) findViewById(R.id.zhipcode);

        destination_position.setText(datas_desti_multi.get(0).get(9));
        positions.setText(datas_desti_multi.get(0).get(9));
        address_text11.setText(datas_desti_multi.get(0).get(10));

        if (indexs.getVisibility() == View.VISIBLE) {
            if(identity_string.get("origin").equals("origin")) {
                address_text.setText(orgin_Datas.get("community"));
                address_text1.setText(orgin_Datas.get("references"));
                address_text4.setText("TELEFONO :" +
                        orgin_Datas.get("phone_number1") + "\n" + "Nombre   :" +
                        orgin_Datas.get("contact_name"));
                buttons_layouts.setVisibility(View.GONE);
                contacto_name.setText(orgin_Datas.get("contact_name"));
                address_text4_second.setText(orgin_Datas.get("community"));
                red_button.setVisibility(View.VISIBLE);
            }else{
                buttons_layouts.setVisibility(View.VISIBLE);
                red_button.setVisibility(View.GONE);
                address_text.setText(datas_desti_multi.get(0).get(1));
                address_text1.setText(datas_desti_multi.get(0).get(2));
                contacto_name.setText(datas_desti_multi.get(0).get(3));
                address_text4_second.setText(datas_desti_multi.get(0).get(1));
                address_text4.setText("TELEFONO :" + datas_desti_multi.get(0).get(4) +
                        " \n" + "Nombre   :" + datas_desti_multi.get(0).get(3));
            }
        }else if (indexs_second.getVisibility() == View.VISIBLE) {
            if(identity_string.get("origin").equals("origin")) {
                address_text4_second.setText(orgin_Datas.get("community"));
                buttons_layouts.setVisibility(View.GONE);
                red_button.setVisibility(View.VISIBLE);
                positions.setText("0");
            }else{
                buttons_layouts.setVisibility(View.VISIBLE);
                red_button.setVisibility(View.GONE);
                address_text4_second.setText(datas_desti_multi.get(0).get(1));
            }
        }



        call_image = (ImageView) findViewById(R.id.call_image);
        sms_image = (ImageView) findViewById(R.id.sms_image);

        whatsapp_image = (ImageView) findViewById(R.id.whatsapp_image);

        whatsapp_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getPackageManager();
                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                String text = "YOUR TEXT HERE";
                try {
                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                waIntent.setPackage("com.whatsapp");
                waIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(waIntent, "Share with"));
            }
        });

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number =  address_text4.getText().toString();
                number = number.replace("TELEFONO :", "");
                number = number.replace("Nombre   :", "");
                number = number.replace(datas_desti_multi.get(0).get(3), "");
                AlertDialog alertbox = new AlertDialog.Builder(MapsActivity.this)
                        .setMessage("Do you want to Call to this "  + number)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + number));
                                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                startActivity(callIntent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                        .show();
            }
        });

        call_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number =  address_text4.getText().toString();
                number = number.replace("TELEFONO :", "");
                number = number.replace("Nombre   :", "");
                number = number.replace(datas_desti_multi.get(0).get(3), "");
                AlertDialog alertbox = new AlertDialog.Builder(MapsActivity.this)
                        .setMessage("Do you want to Call to this " + number)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + number));
                                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                startActivity(callIntent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                        .show();
            }
        });

        sms_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number =  address_text4.getText().toString();
                number = number.replace("TELEFONO :", "");
                number = number.replace("Nombre   :", "");
                number = number.replace(datas_desti_multi.get(0).get(3), "");

                AlertDialog alertbox = new AlertDialog.Builder(MapsActivity.this)
                        .setMessage("Do you want to Message to this " + number)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Uri uri = Uri.parse("smsto:" + number);
                                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                        .show();
            }
        });

        if (status == null || was_picked == null || status.equals("null") || was_picked.equals("null")){

        }else{
            red_button.setVisibility(View.GONE);
            buttons_layouts.setVisibility(View.VISIBLE);
            orgin_destination_identy.createOrginDatas("destination");
            address_text.setText(datas_desti_multi.get(0).get(1));
            address_text1.setText(datas_desti_multi.get(0).get(2));
            destination_position.setText(datas_desti_multi.get(0).get(9));
            address_text4_second.setText(datas_desti_multi.get(0).get(1));
        }


        red_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                red_button.setVisibility(View.GONE);
                buttons_layouts.setVisibility(View.VISIBLE);
                orgin_destination_identy.createOrginDatas("destination");
                address_text.setText(datas_desti_multi.get(0).get(1));
                address_text4_second.setText(datas_desti_multi.get(0).get(1));
                address_text1.setText(datas_desti_multi.get(0).get(2));
                destination_position.setText(datas_desti_multi.get(0).get(9));
            }
        });

        ruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, RutaActivity.class);
                intent.putExtra("destination_size", destination_size);
                intent.putExtra("json_object", json_object);
                startActivity(intent);
            }
        });


        contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsapp_image.setVisibility(View.GONE);
            }
        });
        zhipcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsapp_image.setVisibility(View.VISIBLE);
            }
        });


        green_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (destination_size.equals("1")) {
                    Intent intent = new Intent(MapsActivity.this, ComprobanteActivity.class);
                    intent.putExtra("errand_ids", errand_ids);
                    intent.putExtra("destination_id", destination_id);
                    intent.putExtra("destination_size", destination_size);
                    intent.putExtra("origin_datas", orgin_Datas);
                    intent.putExtra("datas_desti", datas_desti);
                    intent.putExtra("datas_desti_multi", datas_desti_multi);
                    startActivity(intent);
                    MapsActivity.this.finish();
                } else {
                    Intent intent = new Intent(MapsActivity.this, ComprobanteActivity.class);
                    intent.putExtra("origin_datas", orgin_Datas);
                    intent.putExtra("datas_desti_multi", datas_desti_multi);
                    intent.putExtra("multi_lat", multi_lat);
                    intent.putExtra("multi_long", multi_long);
                    intent.putExtra("errand_ids", errand_ids);
                    intent.putExtra("destination_size", destination_size);
                    intent.putExtra("destination_id", destination_id);
                    startActivity(intent);
                    MapsActivity.this.finish();
                }
            }
        });

        gray_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (destination_size.equals("1")) {
                    Intent intent = new Intent(MapsActivity.this, No_EntregadoActivity.class);
                    intent.putExtra("errand_ids", errand_ids);
                    intent.putExtra("destination_id", destination_id);
                    intent.putExtra("destination_size", destination_size);
                    intent.putExtra("origin_datas", orgin_Datas);
                    intent.putExtra("datas_desti", datas_desti);
                    intent.putExtra("datas_desti_multi", datas_desti_multi);
                    startActivity(intent);
                    MapsActivity.this.finish();
                } else {
                    Intent intent = new Intent(MapsActivity.this, No_EntregadoActivity.class);
                    intent.putExtra("origin_datas", orgin_Datas);
                    intent.putExtra("datas_desti_multi", datas_desti_multi);
                    intent.putExtra("multi_lat", multi_lat);
                    intent.putExtra("multi_long", multi_long);
                    intent.putExtra("errand_ids", errand_ids);
                    intent.putExtra("destination_size", destination_size);
                    intent.putExtra("destination_id", destination_id);
                    startActivity(intent);
                    MapsActivity.this.finish();
                }

            }
        });

        sendRequest();

        mNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (destination_size.equals("1")) {
                    String uri = "http://maps.google.com/maps?saddr=" + latitude + "," + longitude
                            + "&daddr=" + orgin_Datas.get("latitude") + "," + orgin_Datas.get("longitude");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    Log.e("url", uri);
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                } else {
                    String uri = "http://maps.google.com/maps?saddr=" +  orgin_Datas.get("latitude") + "," +  orgin_Datas.get("longitude")
                            + "&daddr=" + datas_desti_multi.get(0).get(6) + "," +
                            datas_desti_multi.get(0).get(7);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
            }
        });

        mNavigation_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (destination_size.equals("1")) {
                    String uri = "http://maps.google.com/maps?saddr=" + latitude + "," + longitude
                            + "&daddr=" + orgin_Datas.get("latitude") + "," + orgin_Datas.get("longitude");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    Log.e("url", uri);
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                } else {
                    String uri = "http://maps.google.com/maps?saddr=" + latitude + "," + longitude
                            + "&daddr=" + datas_desti_multi.get(0).get(7) + "," +
                            datas_desti_multi.get(0).get(8);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
            }
        });



        mapView.setZoomLevel(13);
        mapView.onCreate(savedInstanceState);

        mapView.setOnMapClickListener(new MapView.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {

                if (indexs.getVisibility() == View.VISIBLE) {
                    detail_map();
                }else if (indexs_second.getVisibility() == View.VISIBLE) {
                    map();
                }

            }
        });
    }

    private void map() {
        indexs.setVisibility(View.VISIBLE);
        indexs_second.setVisibility(View.GONE);

       //detail_sendRequest();
    }

    public void detail_map(){

        indexs.setVisibility(View.GONE);
        indexs_second.setVisibility(View.VISIBLE);
        detail_sendRequest();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

    private void sendRequest() {

            SpriteFactory spriteFactory = new SpriteFactory(mapView);

            int[] images = {
                    R.drawable.blue_one,
                    R.drawable.blue_two,
                    R.drawable.blue_three,
                    R.drawable.blue_four,
                    R.drawable.blue_five,
                    R.drawable.blue_six,
                    R.drawable.blue_seven,
                    R.drawable.blue_eight,
                    R.drawable.blue_nine,
                    R.drawable.blue_ten
            };

            List<Integer> new_image = new ArrayList<>();

            for (int i = 0; i < Integer.parseInt(destination_size); i++){
                new_image.add(images[i]);
            }

            if (gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            } else {
                //gps.showSettingsAlert();

                final Dialog dialog = new Dialog(MapsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.status_active_custom_alert);

                TextView message = (TextView) dialog.findViewById(R.id.message);
                message.setText("Location Disabled. If You Want to Enable it?");

                final Button positive_button = (Button) dialog.findViewById(R.id.possitive_button);
                positive_button.setText("Yes");
                positive_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                Button negative_button = (Button) dialog.findViewById(R.id.negative_button);
                negative_button.setText("NO");
                negative_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

            gps = new GPSTracker(MapsActivity.this);

            if (gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            } else {
                gps.showSettingsAlert();
            }

            Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

            List<Address> addresses = null;
            try {

                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                mapView.setCenterCoordinate(new com.mapbox.mapboxsdk.geometry.LatLng(latitude, longitude));

                Sprite icon_str_Address = spriteFactory.fromResource(R.drawable.start_addr);

                //Current Location Marker
                mapView.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions()
                        .position(new com.mapbox.mapboxsdk.geometry.LatLng(latitude, longitude))
                        .icon(icon_str_Address));

                //Orgin Location
                double orgin_latitude = Double.parseDouble(orgin_Datas.get("latitude"));
                double orgin_longitude = Double.parseDouble(orgin_Datas.get("longitude"));

                Sprite icon_orgin = spriteFactory.fromResource(R.drawable.blue_zero);

                mapView.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions()
                        .position(new com.mapbox.mapboxsdk.geometry.LatLng(orgin_latitude, orgin_longitude))
                        .icon(icon_orgin));

                final List<com.mapbox.mapboxsdk.geometry.LatLng> mListLatLng = new ArrayList<>();

                mListLatLng.add(new com.mapbox.mapboxsdk.geometry.LatLng(latitude, longitude));
                mListLatLng.add(new com.mapbox.mapboxsdk.geometry.LatLng(orgin_latitude, orgin_longitude));

                for (int i = 0; i < datas_desti_multi.size(); i++){
                    double lat = Double.valueOf(datas_desti_multi.get(i).get(7));
                    double lng = Double.valueOf(datas_desti_multi.get(i).get(8));
                    mListLatLng.add(new com.mapbox.mapboxsdk.geometry.LatLng(lat, lng));

                    Sprite icon = spriteFactory.fromResource(new_image.get(i));

                    mapView.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions()
                            .position(new com.mapbox.mapboxsdk.geometry.LatLng(lat, lng))
                            .icon(icon));
                }

                com.mapbox.mapboxsdk.annotations.PolylineOptions polylineOptions = new com.mapbox.mapboxsdk.annotations.PolylineOptions()
                        .color(Color.RED)
                        .add(mListLatLng.toArray(new com.mapbox.mapboxsdk.geometry.LatLng[mListLatLng.size()]))
                        .width(2);

                mapView.addPolyline(polylineOptions);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    private void detail_sendRequest() {

        SpriteFactory spriteFactory = new SpriteFactory(mapView);

        int[] images = {
                R.drawable.blue_one,
                R.drawable.blue_two,
                R.drawable.blue_three,
                R.drawable.blue_four,
                R.drawable.blue_five,
                R.drawable.blue_six,
                R.drawable.blue_seven,
                R.drawable.blue_eight,
                R.drawable.blue_nine,
                R.drawable.blue_ten
        };

        List<Integer> new_image = new ArrayList<>();

        for (int i = 0; i < Integer.parseInt(destination_size); i++){
            new_image.add(images[i]);
        }

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            //gps.showSettingsAlert();

            final Dialog dialog = new Dialog(MapsActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.status_active_custom_alert);

            TextView message = (TextView) dialog.findViewById(R.id.message);
            message.setText("Location Disabled. If You Want to Enable it?");

            final Button positive_button = (Button) dialog.findViewById(R.id.possitive_button);
            positive_button.setText("Yes");
            positive_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            Button negative_button = (Button) dialog.findViewById(R.id.negative_button);
            negative_button.setText("NO");
            negative_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        gps = new GPSTracker(MapsActivity.this);

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }

        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

        List<Address> addresses = null;
        try {

            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            mapView.setCenterCoordinate(new com.mapbox.mapboxsdk.geometry.LatLng(latitude, longitude));

            Sprite icon_str_Address = spriteFactory.fromResource(R.drawable.start_addr);

            //Current Location Marker
            mapView.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions()
                    .position(new com.mapbox.mapboxsdk.geometry.LatLng(latitude, longitude))
                    .icon(icon_str_Address));

            //Orgin Location
            double orgin_latitude = Double.parseDouble(orgin_Datas.get("latitude"));
            double orgin_longitude = Double.parseDouble(orgin_Datas.get("longitude"));

            Sprite icon_orgin = spriteFactory.fromResource(R.drawable.blue_zero);

            mapView.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions()
                    .position(new com.mapbox.mapboxsdk.geometry.LatLng(orgin_latitude, orgin_longitude))
                    .icon(icon_orgin));

            final List<com.mapbox.mapboxsdk.geometry.LatLng> mListLatLng = new ArrayList<>();

            mListLatLng.add(new com.mapbox.mapboxsdk.geometry.LatLng(latitude, longitude));
            mListLatLng.add(new com.mapbox.mapboxsdk.geometry.LatLng(orgin_latitude, orgin_longitude));

            for (int i = 0; i < datas_desti_multi.size(); i++){
                double lat = Double.valueOf(datas_desti_multi.get(i).get(7));
                double lng = Double.valueOf(datas_desti_multi.get(i).get(8));
                mListLatLng.add(new com.mapbox.mapboxsdk.geometry.LatLng(lat, lng));

                Sprite icon = spriteFactory.fromResource(new_image.get(i));

                mapView.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions()
                        .position(new com.mapbox.mapboxsdk.geometry.LatLng(lat, lng))
                        .icon(icon));
            }

            com.mapbox.mapboxsdk.annotations.PolylineOptions polylineOptions = new com.mapbox.mapboxsdk.annotations.PolylineOptions()
                    .color(Color.RED)
                    .add(mListLatLng.toArray(new com.mapbox.mapboxsdk.geometry.LatLng[mListLatLng.size()]))
                    .width(2);

            mapView.addPolyline(polylineOptions);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {
                    boolean Camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneStatePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean Network_state = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean Phone_state = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                }

                break;
        }
    }

}

