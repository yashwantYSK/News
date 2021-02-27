package com.example.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.TextView;

import com.example.news.database.Appdatabase;
import com.example.news.database.Choise;
import com.example.news.database.Fab;
import com.example.news.database.FabNewsDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

public class FabNewsActi extends AppCompatActivity {

    private recyclerFavAdopter rva;
    private TextView dateTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab_news);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#44595f")));
        bar.setTitle("Daily News");
        dateTime=findViewById(R.id.dateTime);
        initRecyclerView();
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
        FabNewsDatabase db=FabNewsDatabase.getDbInstance(this.getApplicationContext());
        List<Fab> fabs=db.fabnesDAO().getAllNews();
        rva.setData(fabs);

    }
    private void initRecyclerView()
    {
        RecyclerView recyclerView=findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rva=new recyclerFavAdopter(this);
        recyclerView.setAdapter(rva);
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
            Intent intent=new Intent(FabNewsActi.this,about.class);
            startActivity(intent);
        }
        else if(id==R.id.fab)
        {

        }
        else if(id==R.id.home)
        {
            Intent intent=new Intent(FabNewsActi.this,MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}