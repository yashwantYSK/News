package com.example.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.news.database.Appdatabase;
import com.example.news.database.Choise;
import com.example.news.database.Fab;
import com.example.news.database.FabNewsDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
{
    private Context context;
    private JSONArray respose;
    public RecyclerViewAdapter(Context context)
    {
        this.context=context;
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context)
                .inflate(R.layout.viewnews,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        if(respose!=null) {
            try {
                    holder.newstitle.setText(respose.getJSONObject(position).get("title").toString());
                    holder.newsdes.setText(respose.getJSONObject(position).get("description").toString());
                    if (respose.getJSONObject(position).get("source_name").toString().length() > 20)
                        holder.newspub.setText(respose.getJSONObject(position).get("source_name").toString().substring(0, 20));
                    else
                        holder.newspub.setText(respose.getJSONObject(position).get("source_name").toString());
                    holder.newsdate.setText(respose.getJSONObject(position).get("published_datetime").toString().substring(0, 10));
                    Glide.with(context.getApplicationContext())
                        .asBitmap()

                        .load(respose.getJSONObject(position).get("image_url").toString())
                        .into(new SimpleTarget<Bitmap>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResourceReady(Bitmap bitmap,
                                                        Transition<? super Bitmap> transition) {
                                int w = bitmap.getWidth();
                                int h = bitmap.getHeight();
                                if(w!=h) {
                                    if (h>400)
                                        holder.img.setMaxHeight(400);
                                    holder.img.setImageBitmap(bitmap);
                                }

                            }
                        });
                    holder.newstitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = null;
                            try {
                                String url =respose.getJSONObject(position).get("article_url").toString();
                                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                CustomTabsIntent customTabsIntent = builder.build();
                                customTabsIntent.launchUrl(context, Uri.parse(url));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            FabNewsDatabase db= FabNewsDatabase.getDbInstance(context);
            try {
                List<Fab> fabs=db.fabnesDAO().getNews(respose.getJSONObject(position).get("title").toString());
                if(fabs.size()>0)
                {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.gold);
                    holder.fab.setImageBitmap(bitmap);
                }
                else
                {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.white);
                    holder.fab.setImageBitmap(bitmap);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FabNewsDatabase db= FabNewsDatabase.getDbInstance(context);
                    try {
                        List<Fab> fabs=db.fabnesDAO().getNews(respose.getJSONObject(position).get("title").toString());
                        if(fabs.size()>0)
                        {
                            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.white);
                            holder.fab.setImageBitmap(bitmap);
                            db.fabnesDAO().deleteAll(respose.getJSONObject(position).get("title").toString());
                            Toast.makeText(context,"Removed From Fab",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.gold);
                            holder.fab.setImageBitmap(bitmap);
                            Fab fab=new Fab();
                            fab.title=respose.getJSONObject(position).get("title").toString();
                            fab.dec=respose.getJSONObject(position).get("description").toString();
                            fab.source=respose.getJSONObject(position).get("source_name").toString();
                            fab.date=respose.getJSONObject(position).get("published_datetime").toString();
                            fab.img=respose.getJSONObject(position).get("image_url").toString();
                            fab.link=respose.getJSONObject(position).get("article_url").toString();
                            db.fabnesDAO().pushchoise(fab);
                            Toast.makeText(context,"Added to Fab",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }
    @Override
    public int getItemCount() {
        if(respose!=null)
            return respose.length();
        else
            return 0;
    }
    public void setData(JSONArray respose)
    {
        this.respose=respose;
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView newstitle,newsdes,newspub,newsdate;
        View view;
        ImageView img,fab;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            newstitle=(TextView)itemView.findViewById(R.id.newstitle);
            newsdes=(TextView)itemView.findViewById(R.id.newsdes);
            newspub=(TextView)itemView.findViewById(R.id.newspubli);
            newsdate=(TextView)itemView.findViewById(R.id.newsdate);
            fab=itemView.findViewById(R.id.fab);
            img=(ImageView)itemView.findViewById(R.id.newsimg);
        }
    }
}
