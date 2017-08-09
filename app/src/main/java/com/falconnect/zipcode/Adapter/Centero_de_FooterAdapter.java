package com.falconnect.zipcode.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.falconnect.zipcode.MapsActivity;
import com.falconnect.zipcode.Model.Centero_de_footer_model;
import com.falconnect.zipcode.R;

import java.util.ArrayList;
import java.util.List;

public class Centero_de_FooterAdapter extends RecyclerView.Adapter<Centero_de_FooterAdapter.MyViewHolder> {

    private ArrayList<Centero_de_footer_model> selldataSet;
    private Context mContext;

    public Centero_de_FooterAdapter(Context context, ArrayList<Centero_de_footer_model> selldata) {
        this.mContext = context;
        this.selldataSet = selldata;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_single_item, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final Centero_de_footer_model singleItem_sell = selldataSet.get(listPosition);

        holder.text_ViewName.setText(singleItem_sell.getSellname());
        holder.image_ViewIcon.setImageResource(singleItem_sell.getSellimage());
        holder.category_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (singleItem_sell.getSellfooterid_() == 0) {

                    ///call
                    final String num = "+56951341300";
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + num));
                    mContext.startActivity(callIntent);

                } else if (singleItem_sell.getSellfooterid_() == 1) {
                    ///message - sms
                    final String number = "+56951341300";
                    Uri uri = Uri.parse("smsto:" + number);
                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    mContext.startActivity(intent);
                }else if (singleItem_sell.getSellfooterid_() == 2) {
                    ///whatsapp
                    PackageManager pm=mContext.getPackageManager();
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
                    mContext.startActivity(Intent.createChooser(waIntent, "Share with"));

                } else if (singleItem_sell.getSellfooterid_() == 3) {
                    //email
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"info@zhipcode.com"});
                    emailIntent.setType("text/plain");
                    final PackageManager pm = mContext.getPackageManager();
                    final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                    ResolveInfo best = null;
                    for(final ResolveInfo info : matches)
                        if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                            best = info;
                    if (best != null)
                        emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                    mContext.startActivity(emailIntent);

                } else {

                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return (null != selldataSet ? selldataSet.size() : 0);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text_ViewName;
        ImageView image_ViewIcon;
        LinearLayout category_item;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.text_ViewName = (TextView) itemView.findViewById(R.id.mtitle);
            this.image_ViewIcon = (ImageView) itemView.findViewById(R.id.image_footer);
            this.category_item = (LinearLayout) itemView.findViewById(R.id.category_item);

        }
    }


}