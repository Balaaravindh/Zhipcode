package com.falconnect.zipcode;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.falconnect.zipcode.MapModules.DirectionFinder;
import com.falconnect.zipcode.MapModules.DirectionFinderListener;
import com.falconnect.zipcode.MapModules.GPSTracker;
import com.falconnect.zipcode.MapModules.Route;
import com.falconnect.zipcode.SessionManager.Orgin_destination_identy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.views.MapView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    LatLng hcmus = null;
    String errand_ids;
    List<Route> routes = new ArrayList<>();
    ArrayList<String> destination_arrays = new ArrayList<>();
    String destination_id;
    HashMap<String, String> orgin_Datas;
    HashMap<String, String> datas_desti;
    HashMap<Integer, ArrayList<String>> datas_desti_multi;
    ArrayList<String> multi_lat;
    ArrayList<String> multi_long;
    String destination_size;
    ///Button to navigation map
    Button mNavigation;
    Button red_button, green_button, gray_button;
    LinearLayout buttons_layouts;
    ArrayList<HashMap<Integer, ArrayList<String>>> values;
    TextView address_text, destination_position, address_text1, address_text11, address_text4;
    ImageView call_image, sms_image;
    //
    GPSTracker gps;

    ArrayList<HashMap<Integer, ArrayList<String>>> new_all_datas;

    double latitude;
    double longitude;
    String origin, destination;
    RelativeLayout ruta, question, call_layout;
    ArrayList<String> total_desti = new ArrayList<>();
    private GoogleMap mMap;
    private Button btnFindPath;
    private EditText etOrigin;
    int valuesss;
    private EditText etDestination;
    String number;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();

    Orgin_destination_identy orgin_destination_identy;
    HashMap<String, String> identity_string;

    HashMap<String, ArrayList<String>> all_datas;
    MapView mapView;
    //Relativi
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

      /*  SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);

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
                    String uri = "http://maps.google.com/maps?saddr=" + latitude + "," + longitude
                            + "&daddr=" + datas_desti_multi.get("latitude_desti").get(6) + "," +
                            datas_desti_multi.get("longitude_desti").get(7);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
            }
        });

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

       /* Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null) {
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder();
                    for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i));
                    }
                origin = strReturnedAddress.toString();
            } else {
                Log.e("sdasdas", "No Address returned!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //origin = latitude + "," + longitude;
        //origin = orgin_Datas.get("community");

        Log.e("origin", origin);
        if (destination_size.equals("1")) {
            destination = datas_desti_multi.get(0).get(0);
        }else {
            destination = datas_desti_multi.get(0).get(0);
        }

        Log.e("destination", destination);

        if (origin == null) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination == null) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (Route route : routes) {
            hcmus = new LatLng(route.Start_Lat, route.Start_Lng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
            originMarkers.add(mMap.addMarker(new MarkerOptions().title("Đại học Khoa học tự nhiên").position(hcmus)));
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.", "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 12.6f));

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_addr))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_zero))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.parseColor("#691A99")).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

}