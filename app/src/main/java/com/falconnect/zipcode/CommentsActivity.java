package com.falconnect.zipcode;

import android.*;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.falconnect.zipcode.Adapter.BilleteraListViewAdapter;
import com.falconnect.zipcode.Crop.Crop;
import com.falconnect.zipcode.SessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class CommentsActivity extends AppCompatActivity {

    ActionBar actionBar;
    ImageView positive, negative;
    RelativeLayout image;
    SessionManager sessionManager;
    HashMap<String, String> user;
    String destination_id;
    String errand_ids;
    String destination_size;
    HashMap<String, String> orgin_Datas;
    HashMap<String, String> datas_desti;
    HashMap<Integer, ArrayList<String>> datas_desti_multi;
    ArrayList<String> multi_lat;
    ArrayList<String> multi_long;
    ImageView camera_image;
    public static String imagebase64;
    public static String encodedImage;
    public static final int RequestPermissionCode = 1;
    Bitmap Bgimage;
    public static File Cameraimagepath;
    byte[] Bgimagebyte = null;
    ByteArrayOutputStream bao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        actionBar = getSupportActionBar();
        actionBar.hide();

        intialize();

        errand_ids = getIntent().getStringExtra("errand_ids");
        destination_id = getIntent().getStringExtra("destination_id");
        destination_size = getIntent().getStringExtra("destination_size");

        ////////////////GET VALUES
        orgin_Datas = (HashMap<String, String>) getIntent().getSerializableExtra("origin_datas");
        datas_desti = (HashMap<String, String>) getIntent().getSerializableExtra("datas_desti");
        datas_desti_multi = (HashMap<Integer, ArrayList<String>>) getIntent().getSerializableExtra("datas_desti_multi");

        multi_lat = getIntent().getStringArrayListExtra("multi_lat");
        multi_long = getIntent().getStringArrayListExtra("multi_long");

        if (datas_desti == null) {
        } else {
        }
        if (orgin_Datas == null) {
        } else {
        }
        if (datas_desti_multi == null) {
        } else {

        }

        if (multi_lat == null) {

        } else {
        }

        if (multi_long == null) {

        } else {
        }

        if (destination_size == null) {

        } else {

        }

        camera_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(CommentsActivity.this, R.style.MaterialDialogSheet);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.image_pickup_alert);
                dialog.setCancelable(true);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.show();

                LinearLayout take_image = (LinearLayout) dialog.findViewById(R.id.take_image);
                take_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(CommentsActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(CommentsActivity.this, new String[]{android.Manifest.permission.CAMERA}, 100);
                            } else {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                Cameraimagepath = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath(),
                                        "File_" + UUID.randomUUID().toString()+".jpg");
                                Log.d("imagepath",Cameraimagepath.getAbsolutePath());
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Cameraimagepath));
                                startActivityForResult(intent, 200);
                                dialog.dismiss();
                            }
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            Cameraimagepath = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath(),
                                    "File_" + UUID.randomUUID().toString()+".jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Cameraimagepath));
                            Log.d("imagepath",Cameraimagepath.getAbsolutePath());
                            startActivityForResult(intent, 200);
                            dialog.dismiss();
                        }
                    }
                });

                LinearLayout select_image = (LinearLayout) dialog.findViewById(R.id.select_image);
                select_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Crop.pickImage(CommentsActivity.this);
                        dialog.dismiss();
                    }
                });

                LinearLayout remove = (LinearLayout) dialog.findViewById(R.id.remove);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Glide.with(CommentsActivity.this)
                                .load(R.drawable.camerared)
                                .into(camera_image);

                        dialog.dismiss();
                    }
                });

                RelativeLayout cancelar = (RelativeLayout) dialog.findViewById(R.id.cancelar);
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postive_thumbs();
            }
        });

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                negative_thumbs();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (resultCode == 100) {
            if (ContextCompat.checkSelfPermission(CommentsActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Cameraimagepath = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath(),
                        "File_"+ UUID.randomUUID().toString()+".jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Cameraimagepath));
                startActivityForResult(intent, 200);
            }
        } else if (requestCode == Crop.REQUEST_PICK) {
            Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
            Crop.of(result.getData(), destination).asSquare().start(this);
        } else if (requestCode == 200) {
            Log.d("imagepath",Cameraimagepath.getAbsolutePath());
            Uri destination = Uri.fromFile(new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath(),
                    "File_"+ UUID.randomUUID().toString()+".jpg"));
            Crop.of(Uri.fromFile(Cameraimagepath), destination).asSquare().start(this);
            camera_image.setImageURI(destination);
            camera_image.buildDrawingCache();
            Bgimage = camera_image.getDrawingCache();
            bao = new ByteArrayOutputStream();
            Bgimage.compress(Bitmap.CompressFormat.JPEG, 50, bao);
            Bgimagebyte = bao.toByteArray();
            encodedImage = Base64.encodeToString(Bgimagebyte, Base64.NO_WRAP);
            imagebase64 = encodedImage;
            Log.e("imagebase64", imagebase64);

        } else if (requestCode == Crop.REQUEST_CROP) {
            camera_image.setImageURI(Crop.getOutput(result));
            camera_image.buildDrawingCache();
            Bgimage = camera_image.getDrawingCache();

            bao = new ByteArrayOutputStream();
            Bgimage.compress(Bitmap.CompressFormat.JPEG, 50, bao);
            Bgimagebyte = bao.toByteArray();
            encodedImage = Base64.encodeToString(Bgimagebyte, Base64.NO_WRAP);
            imagebase64 = encodedImage;
            Log.e("imagebase64", imagebase64);
        }
    }


    private void negative_thumbs() {
        Intent intent = new Intent(CommentsActivity.this, Negative_comments.class);
        intent.putExtra("errand_ids", errand_ids);
        intent.putExtra("destination_id", destination_id);
        startActivity(intent);
        CommentsActivity.this.finish();
    }

    private void postive_thumbs() {
        final ProgressDialog barProgressDialog = ProgressDialog.show(CommentsActivity.this, "Cargando...", "Por Favor Espera...", true);
        final String URL = ConstantAPI.ERRAND_ASSIGN + errand_ids + "/";

        if (imagebase64 == "" || imagebase64 == null) {
            imagebase64 = "";
            Log.e("imagebase64", imagebase64);
        }

        JSONObject thumbs_user = new JSONObject();
        try {
            thumbs_user.put("client_rate", "good");
            thumbs_user.put("manifest", imagebase64);
            Log.e("asdasdas", thumbs_user.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH, URL, thumbs_user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                barProgressDialog.dismiss();

                String charged_cost_messenger = response.optString("charged_cost_messenger");

                Intent intent = new Intent(CommentsActivity.this, Finalizar_Activity_three.class);
                intent.putExtra("errand_ids", errand_ids);
                intent.putExtra("destination_id", destination_id);
                intent.putExtra("charged_cost_messenger", charged_cost_messenger);
                startActivity(intent);
                CommentsActivity.this.finish();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("Error", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                user = sessionManager.getUserDetails();
                headers.put("Authorization", "Token" + " " + user.get("token"));
                Log.e("params", headers.toString());
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CommentsActivity.this);
        requestQueue.add(req);

    }

    public void intialize() {
        sessionManager = new SessionManager(CommentsActivity.this);
        user = sessionManager.getUserDetails();
        positive = (ImageView) findViewById(R.id.image_thumbsup);
        negative = (ImageView) findViewById(R.id.image_thumbsdown);
        image = (RelativeLayout) findViewById(R.id.image_pickup);
        camera_image = (ImageView) findViewById(R.id.camera_image);


    }


}
