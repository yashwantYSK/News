package com.example.news;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.news.database.Appdatabase;
import com.example.news.database.Choise;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerViewAdapter rva;
    String key="";
    String temp="";
    Animation rotate;
    private FloatingActionButton refrsh;
    private TextView dateTime,language,search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refrsh=findViewById(R.id.refrsh);
        search=findViewById(R.id.search);
        dateTime=findViewById(R.id.dateTime);
        language=findViewById(R.id.language);
       rotate = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setRepeatCount(-1);
        rotate.setDuration(30000);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#44595f")));
        bar.setTitle("Daily News");
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] optionsItems;
                optionsItems = new String[]{"All","India","Bihar","JharKhand","Bhagalpur","Delhi","Job","Result","Army","Technology","Education","Student","Sports"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Select Your Tag");
                builder.setIcon(R.drawable.india);
                builder.setSingleChoiceItems(optionsItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!search.getText().toString().equals(optionsItems[which]))
                        {
                            Appdatabase db=Appdatabase.getDbInstance(MainActivity.this);
                            Choise cho=new Choise();
                            if(optionsItems[which].equals("All"))
                            {
                                cho.key="India";
                            }
                            else
                            {
                                cho.key=optionsItems[which];
                            }
                            cho.language=language.getText().toString();
                            db.choiseDAO().deleteAll();
                            db.choiseDAO().pushchoise(cho);
                            search.setText(optionsItems[which]);
                            dialog.dismiss();
                            changeKey();
                        }
                        else
                            dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] optionsItems;
                optionsItems = new String[]{"Hindi","English"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose Your Language");
                builder.setIcon(R.drawable.india);
                builder.setSingleChoiceItems(optionsItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!language.getText().toString().equals(optionsItems[which]))
                        {
                            Appdatabase db=Appdatabase.getDbInstance(MainActivity.this);
                            Choise cho=new Choise();
                            cho.key=search.getText().toString();
                            cho.language=optionsItems[which];
                            db.choiseDAO().deleteAll();
                            db.choiseDAO().pushchoise(cho);
                            search.setText(optionsItems[which]);
                            dialog.dismiss();
                            changeKey();
                            language.setText(optionsItems[which]);
                            dialog.dismiss();
                            changeKey();
                        }
                        else
                            dialog.dismiss();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        Thread t=new Thread(){
            @Override
            public void run() {
                super.run();
                while (!isInterrupted())
                {
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Date date = new Date();
                                String Sdate=date.toString();
                                String y = Sdate.substring(Sdate.length()-4,Sdate.length());
                                String d=Sdate.substring(8,10);
                                String m=Sdate.substring(4,7).toUpperCase();
                                String time=Sdate.substring(10,19);
                                String day=Sdate.substring(0,3).toUpperCase();
                                dateTime.setText(d+" "+m+" "+y+", "+time+", "+day.toString());
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
        refrsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refrsh.setAnimation(rotate);
                changeKey();
            }
        });
        key="";
        changeKey();
    }
    private void initRecyclerView()
    {
        RecyclerView recyclerView=findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rva=new RecyclerViewAdapter(this);
        recyclerView.setAdapter(rva);
    }
    private void setMessage(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null) {
                            JSONArray data= new JSONArray();
                            try {
                                data=response.getJSONArray("articles");
                                } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            initRecyclerView();
                            rva.setData(data);
                            }
                        }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something Happend Wrong\nIf Possible Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }

        );
        requestQueue.add(objectRequest);
    }
    private void changeKey()
    {
        Appdatabase db=Appdatabase.getDbInstance(this.getApplicationContext());
        List<Choise> cho=db.choiseDAO().getChoise();
        if(cho.size()==1) {
            language.setText(cho.get(0).language);
            search.setText(cho.get(0).key);
            key = search.getText().toString();
        }
        else
        {
            language.setText("Hindi");
            search.setText("India");
            key = search.getText().toString();
        }
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("news");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               String token =snapshot.child("token").getValue(String.class);
                String urlLeft="https://gnewsapi.net/api/search?q=";
                String urlRight="&country=in&api_token=";
                urlRight=urlRight+token;
                if(key.equals("")){
                    key="india";
                    urlLeft=urlLeft+key;
                }
                else
                {
                    urlLeft=urlLeft+key;
                }
                if(language.getText().toString().equals("Hindi"))
                {
                    urlRight="&language=hi"+urlRight;
                }
                else
                {
                    urlRight="&language=en"+urlRight;
                }
                temp=urlLeft+urlRight;
                setMessage(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.about)
        {
            Intent intent=new Intent(MainActivity.this,about.class);
            startActivity(intent);
        }
        else if(id==R.id.fab)
        {
            Intent intent=new Intent(MainActivity.this,FabNewsActi.class);
            startActivity(intent);
        }
        else if(id==R.id.home)
        {

        }
        return super.onOptionsItemSelected(item);
    }
}