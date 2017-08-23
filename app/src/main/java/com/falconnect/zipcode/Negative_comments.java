package com.falconnect.zipcode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.falconnect.zipcode.Crop.Crop;
import com.falconnect.zipcode.SessionManager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Negative_comments extends AppCompatActivity {

    EditText enviar_opinion_textbox;
    Button enviar_opinion;
    String edit_text_values;
    String errand_ids;
    SessionManager sessionManager;
    HashMap<String, String> user;
    String destination_id;

    //ProgressDialog
    ProgressDialog barProgressDialog;
    ImageView camera_image;
    Bitmap Bgimage;
    File Cameraimagepath;
    byte[] Bgimagebyte = null;
    ByteArrayOutputStream bao;
    public static String imagebase64;
    public static String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negative_comments);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        intilize();

        errand_ids = getIntent().getStringExtra("errand_ids");
        destination_id = getIntent().getStringExtra("destination_id");


        enviar_opinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_text_values = enviar_opinion_textbox.getText().toString();
                if (edit_text_values.equals("") || edit_text_values == null) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Negative_comments.this);
                    builder.setTitle("Error");
                    builder.setMessage("Por favor ingrese la observación");
                    builder.setPositiveButton("De Acuerdo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    negative_comment();
                }

            }
        });

        camera_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(Negative_comments.this, R.style.MaterialDialogSheet);
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
                            if (ContextCompat.checkSelfPermission(Negative_comments.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(Negative_comments.this, new String[]{android.Manifest.permission.CAMERA}, 100);
                            } else {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                Cameraimagepath = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath(),
                                        "File_" + UUID.randomUUID().toString()+".jpg");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Cameraimagepath));
                                startActivityForResult(intent, 200);
                                dialog.dismiss();
                            }
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            Cameraimagepath = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath(),
                                    "File_" + UUID.randomUUID().toString()+".jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Cameraimagepath));
                            startActivityForResult(intent, 200);
                            dialog.dismiss();
                        }
                    }
                });

                LinearLayout select_image = (LinearLayout) dialog.findViewById(R.id.select_image);
                select_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Crop.pickImage(Negative_comments.this);
                        dialog.dismiss();
                    }
                });

                LinearLayout remove = (LinearLayout) dialog.findViewById(R.id.remove);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Glide.with(Negative_comments.this)
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
    }


    public void intilize() {

        sessionManager = new SessionManager(Negative_comments.this);
        user = sessionManager.getUserDetails();
        enviar_opinion_textbox = (EditText) findViewById(R.id.enviar_opinion_textbox);
        enviar_opinion = (Button) findViewById(R.id.enviar_opinion);
        camera_image = (ImageView) findViewById(R.id.camera_image);

    }

    public void negative_comment() {
        final String URL = ConstantAPI.ERRAND_ASSIGN + errand_ids + "/";


        if (imagebase64 == "" || imagebase64 == null) {
            imagebase64 = "";
            Log.e("imagebase64", imagebase64);
        }


        JSONObject thumbs_user = new JSONObject();
        try {
            thumbs_user.put("client_rate", "good");
            thumbs_user.put("client_rate_post_notes", edit_text_values);
            thumbs_user.put("manifest", imagebase64);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH, URL, thumbs_user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response", response.toString());

                Intent intent = new Intent(Negative_comments.this, Finalizar_Activity_three.class);
                intent.putExtra("errand_ids", errand_ids);
                intent.putExtra("destination_id", destination_id);
                startActivity(intent);

                Negative_comments.this.finish();

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
        RequestQueue requestQueue = Volley.newRequestQueue(Negative_comments.this);
        requestQueue.add(req);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (resultCode == 100) {
            if (ContextCompat.checkSelfPermission(Negative_comments.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Cameraimagepath = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath(),
                        "File_" + UUID.randomUUID().toString()+".jpg");                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Cameraimagepath));
                startActivityForResult(intent, 200);
            }
        } else if (requestCode == Crop.REQUEST_PICK) {
            Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
            Crop.of(result.getData(), destination).asSquare().start(this);
        } else if (requestCode == 200) {
            Log.d("imagepath",Cameraimagepath.getAbsolutePath());
            Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
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



}
