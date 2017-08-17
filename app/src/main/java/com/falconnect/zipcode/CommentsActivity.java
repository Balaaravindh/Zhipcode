package com.falconnect.zipcode;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import com.falconnect.zipcode.SessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    boolean clicked = false;
    public static String imagebase64;
    public static String encodedImage;


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

        if(destination_size == null){

        }else{

        }

        camera_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> photo_list = new ArrayList<>();
                photo_list.add("Tomar foto");
                photo_list.add("Galería");
                photo_list.add("Eliminar foto");
                final AlertDialog.Builder builder = new AlertDialog.Builder(CommentsActivity.this);
                final ArrayAdapter<String> aa1 = new ArrayAdapter<String>(CommentsActivity.this,
                        R.layout.sort_single_item, R.id.list, photo_list);
                builder.setSingleChoiceItems(aa1, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (photo_list.get(item) == "Tomar foto") {
                            clicked = true;
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, 0);
                            dialog.dismiss();
                        }else if (photo_list.get(item) == "Galería") {
                            clicked = true;
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, 1);
                            dialog.dismiss();
                        } else if (photo_list.get(item) == "Eliminar foto") {
                            clicked = true;
                            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.camera);
                            scaleDown(largeIcon, 200, true);
                            imagebase64 = encodedImage;
                            Log.e("imagebase64", imagebase64);
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {

            if (clicked == true) {

                clicked = false;

                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor == null) {

                } else {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Glide.with(getApplicationContext())
                            .load(selectedImage)
                            .asBitmap()
                            .into(camera_image);

                    Bitmap bitmap = null;

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //convert(bitmap);
                    scaleDown(bitmap, 400, true);
                }
            }
        }else if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

            if (clicked == true) {

                clicked = false;

                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor == null) {

                } else {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Glide.with(getApplicationContext())
                            .load(selectedImage)
                            .asBitmap()
                            .into(camera_image);

                    Bitmap bitmap = null;

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //convert(bitmap);
                    scaleDown(bitmap, 400, true);
                }
            }
        } else {
            Log.i("SonaSys", "resultCode: " + resultCode);
        }
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);

        convert(newBitmap);

        return newBitmap;
    }

    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, outputStream);

        encodedImage = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        imagebase64 = encodedImage;
        Log.e("imagebase64", imagebase64);

        return encodedImage;
    }

    private void negative_thumbs() {

            Intent intent = new Intent(CommentsActivity.this, Negative_comments.class);
            intent.putExtra("errand_ids", errand_ids);
            intent.putExtra("destination_id", destination_id);
            startActivity(intent);
            CommentsActivity.this.finish();

    }

    private void postive_thumbs() {

        final String URL = ConstantAPI.ERRAND_ASSIGN + errand_ids + "/";

        JSONObject thumbs_user = new JSONObject();
        try {
            thumbs_user.put("client_rate", "good");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH, URL, thumbs_user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response", response.toString());

                Intent intent = new Intent(CommentsActivity.this, Finalizar_Activity_three.class);
                intent.putExtra("errand_ids", errand_ids);
                intent.putExtra("destination_id", destination_id);
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
