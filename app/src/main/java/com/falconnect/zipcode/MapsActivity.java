package com.falconnect.zipcode;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
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
import com.falconnect.zipcode.MapModules.GPSTracker;
import com.falconnect.zipcode.SessionManager.Orgin_destination_identy;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.views.MapView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity {

    String errand_ids;
    String destination_id;
    HashMap<String, String> orgin_Datas;
    HashMap<String, String> datas_desti;
    HashMap<Integer, ArrayList<String>> datas_desti_multi;
    ArrayList<String> multi_lat;
    ArrayList<String> multi_long;
    String destination_size;
    Button mNavigation;
    Button red_button, green_button, gray_button;
    LinearLayout buttons_layouts;
    ArrayList<HashMap<Integer, ArrayList<String>>> values;
    TextView address_text, destination_position, address_text1, address_text11, address_text4;
    ImageView call_image, sms_image;

    //MAp REZISE

    RelativeLayout map_first_screen, map_second_screen;

    //
    GPSTracker gps;
    ArrayList<HashMap<Integer, ArrayList<String>>> new_all_datas;

    double latitude;
    double longitude;
    String origin, destination;
    RelativeLayout ruta, question, call_layout;

    String number;

    Orgin_destination_identy orgin_destination_identy;
    HashMap<String, String> identity_string;

    MapView mapView, mapView_full;

    RelativeLayout map_fragment, map_fragment_second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        mapView = (MapView) findViewById(R.id.mapview);
        mapView_full = (MapView) findViewById(R.id.mapview__second);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);
        mapView_full.setStyleUrl(Style.MAPBOX_STREETS);

        orgin_destination_identy = new Orgin_destination_identy(MapsActivity.this);
        identity_string = orgin_destination_identy.getUserDetails();

        ////////////////GET VALUES
        orgin_Datas = (HashMap<String, String>) getIntent().getSerializableExtra("origin_datas");
        datas_desti = (HashMap<String, String>) getIntent().getSerializableExtra("datas_desti");
        datas_desti_multi = (HashMap<Integer, ArrayList<String>>) getIntent().getSerializableExtra("datas_desti_multi");

        multi_lat = getIntent().getStringArrayListExtra("multi_lat");
        multi_long = getIntent().getStringArrayListExtra("multi_long");

        errand_ids = getIntent().getStringExtra("errand_ids");
        destination_id = getIntent().getStringExtra("destination_id");
        destination_size = getIntent().getStringExtra("destination_size");


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

        mNavigation = (Button) findViewById(R.id.mNavigation);
        red_button = (Button) findViewById(R.id.red_button);

        buttons_layouts = (LinearLayout) findViewById(R.id.buttons_layouts);
        call_layout = (RelativeLayout) findViewById(R.id.call_layout);
        green_button = (Button) findViewById(R.id.green_button);
        gray_button = (Button) findViewById(R.id.gray_button);
        ruta = (RelativeLayout) findViewById(R.id.ruta);
        question = (RelativeLayout) findViewById(R.id.question);
        map_first_screen = (RelativeLayout) findViewById(R.id.map_first_screen);
        map_second_screen = (RelativeLayout) findViewById(R.id.map_second_screen);

        map_fragment = (RelativeLayout) findViewById(R.id.map_fragment);
        map_fragment_second = (RelativeLayout) findViewById(R.id.map_fragment_second);


        address_text = (TextView) findViewById(R.id.address_text);
        destination_position = (TextView) findViewById(R.id.destination_position);
        address_text1 = (TextView) findViewById(R.id.address_text1);
        address_text11 = (TextView) findViewById(R.id.address_text11);
        address_text4 = (TextView) findViewById(R.id.address_text4);

        if(identity_string.get("origin").equals("origin")) {

            address_text.setText(orgin_Datas.get("community"));
            address_text1.setText(orgin_Datas.get("references"));
            address_text4.setText("TELEFONO :" + orgin_Datas.get("phone_number1") +
                    " \n" + "Nombre   :" + orgin_Datas.get("contact_name"));
            buttons_layouts.setVisibility(View.GONE);
            red_button.setVisibility(View.VISIBLE);
        }else{
            buttons_layouts.setVisibility(View.VISIBLE);
            red_button.setVisibility(View.GONE);
            address_text.setText(datas_desti_multi.get(0).get(0));
            address_text1.setText(datas_desti_multi.get(0).get(1));
            address_text4.setText("TELEFONO :" + datas_desti_multi.get(0).get(3) +
                    " \n" + "Nombre   :" + datas_desti_multi.get(0).get(2));
        }

        call_image = (ImageView) findViewById(R.id.call_image);
        sms_image = (ImageView) findViewById(R.id.sms_image);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number =  "9999999999";
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
                number = "9999999999";
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
                final String number = "9999999999";

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


        red_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                red_button.setVisibility(View.GONE);
                buttons_layouts.setVisibility(View.VISIBLE);
                orgin_destination_identy.createOrginDatas("destination");
                address_text.setText(datas_desti_multi.get(0).get(0));
                address_text1.setText(datas_desti_multi.get(0).get(1));
                address_text4.setText("TELEFONO :" + datas_desti_multi.get(0).get(3) +
                        " \n" + "Nombre   :" + datas_desti_multi.get(0).get(2));


            }
        });

        ruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MapsActivity.this, RutaActivity.class);
                startActivity(intent);

            }
        });

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


        mapView.setZoomLevel(13);
        mapView.onCreate(savedInstanceState);


    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

    private void sendRequest() {
        gps = new GPSTracker(MapsActivity.this);

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

            //Current Location Marker
            mapView.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions()
                    .position(new com.mapbox.mapboxsdk.geometry.LatLng(latitude, longitude)));

            //Orgin Location
            double orgin_latitude = Double.parseDouble(orgin_Datas.get("latitude"));
            double orgin_longitude = Double.parseDouble(orgin_Datas.get("longitude"));

            mapView.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions()
                    .position(new com.mapbox.mapboxsdk.geometry.LatLng(orgin_latitude, orgin_longitude)));

            final List<com.mapbox.mapboxsdk.geometry.LatLng> mListLatLng = new ArrayList<>();

            mListLatLng.add(new com.mapbox.mapboxsdk.geometry.LatLng(latitude, longitude));
            mListLatLng.add(new com.mapbox.mapboxsdk.geometry.LatLng(orgin_latitude, orgin_longitude));

            com.mapbox.mapboxsdk.annotations.PolylineOptions polylineOptions = new com.mapbox.mapboxsdk.annotations.PolylineOptions()
                    .color(Color.RED)
                    .add(mListLatLng.toArray(new com.mapbox.mapboxsdk.geometry.LatLng[mListLatLng.size()]))
                    .width(2);

            mapView.addPolyline(polylineOptions);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

