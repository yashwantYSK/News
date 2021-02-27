package com.example.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.news.database.Fab;
import com.example.news.database.FabNewsDatabase;

import org.json.JSONException;

import java.util.List;

public class recyclerFavAdopter extends RecyclerView.Adapter<recyclerFavAdopter.MyViewHolder>
{
    public List<Fab> fabs;
    private Context context;
    public recyclerFavAdopter(Context context)
    {
        this.context=context;
    }
    @NonNull
    @Override
    public recyclerFavAdopter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context)
                .inflate(R.layout.viewnews,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerFavAdopter.MyViewHolder holder, int position) {
        holder.newstitle.setText(fabs.get(position).title);
        holder.newsdes.setText(fabs.get(position).dec);
        if (fabs.get(position).source.length() > 20)
            holder.newspub.setText(fabs.get(position).source.substring(0, 20));
        else
            holder.newspub.setText(fabs.get(position).source);
        holder.newsdate.setText(fabs.get(position).date.substring(0, 10));
        Glide.with(context.getApplicationContext())
                .asBitmap()

                .load(fabs.get(position).img)
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
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.gold);
        holder.fab.setImageBitmap(bitmap);
        holder.newstitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = null;
                    String url =fabs.get(position).link;
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(context, Uri.parse(url));
            }
        });
        holder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FabNewsDatabase db= FabNewsDatabase.getDbInstance(context);
                if(fabs.size()>0)
                {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.white);
                    holder.fab.setImageBitmap(bitmap);
                    db.fabnesDAO().deleteAll(fabs.get(position).title);
                    List<Fab> fabs=db.fabnesDAO().getAllNews();
                    setData(fabs);
                    Toast.makeText(context,"Removed From Fab",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return fabs.size();
    }
    public void setData(List<Fab> respose)
    {
        this.fabs=respose;
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
