package com.example.proje;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.*;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Menu
    private Button btnYapilan = null;
    private Button btnPaylasim = null;
    private Button btnCikis = null;
    private Button btnEdit = null;

    private Button btnYeni = null;
    private GridLayout mainLayout = null;

    //Database
    private Firebase reference1;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = auth.getCurrentUser();
    private String userID = "";

    private Aliskanlik aliskanlik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        butonOlustur();
        yuzdeSifirla();
    }

    @Override
    protected void onStart() {

        if (currentUser == null) {
            Intent welcomeIntent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(welcomeIntent);
            finish();
        } else
            userID = currentUser.getUid();

        super.onStart();
    }

    public void init() {

        //Menu
        btnYapilan = findViewById(R.id.btnYapilan);
        btnPaylasim = findViewById(R.id.btnPaylasim);
        btnCikis = findViewById(R.id.btnCikis);
        btnEdit = findViewById(R.id.btnEdit);

        btnYeni = findViewById(R.id.btnYeni);

        mainLayout = findViewById(R.id.main_layout);


        Firebase.setAndroidContext(MainActivity.this);
        reference1 = new Firebase(
                "https://aliskanlik-1b5a3-default-rtdb.europe-west1.firebasedatabase.app/Kullanicilar/");

        btnYapilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, YapilanActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
            }
        });
        btnPaylasim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PaylasimActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
            }
        });

        btnYeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
            }
        });

        btnCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("Çıkış yapmak istiyor musunuz?");
                adb.setIcon(android.R.drawable.ic_dialog_alert);
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        auth.signOut();
                        Intent loginIntent = new Intent(MainActivity.this, WelcomeActivity.class);
                        startActivity(loginIntent);
                        finish();
                        overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                adb.show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnYeni.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_container_view, ListFragment.class, null)
                        .commit();
            }
        });
    }

    public void butonOlustur() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> keys = dataSnapshot.child(userID).child("Aliskanliklar").getChildren();

                for (final DataSnapshot key : keys) {

                    aliskanlik = key.getValue(Aliskanlik.class);
                    String yazi = aliskanlik.getIsim();

                    Button button = new Button(MainActivity.this);

                    button.setTextSize(15);
                    button.setBackgroundResource(R.drawable.shapedbutton);

                    int displayWidth = getResources().getDisplayMetrics().widthPixels;
                    int boyut = (displayWidth - 500) / 3;

                    if (boyut < 100)
                        button.setTextSize(10);
                    if (yazi.length() > 6)
                        yazi = yazi.substring(0, 6) + "..";

                    button.setText(yazi);

                    //Buton renkleri
                    button = butonRengi(button, boyut);

                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(boyut,
                            boyut);
                    lp2.setMargins(50, 0, 50, 10);

                    button.setLayoutParams(lp2);


                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ValueEventListener vl = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Aliskanlik aliskanlik1 = dataSnapshot
                                            .child(userID)
                                            .child("Aliskanliklar").child(key.getKey())
                                            .getValue(Aliskanlik.class);
                                    yuzdeArttir(aliskanlik1, key.getKey());
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            };
                            reference1.addListenerForSingleValueEvent(vl);
                        }
                    });
                    buttonEffect(button, aliskanlik);

                    mainLayout.addView(button);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        reference1.addListenerForSingleValueEvent(listener);
    }

    public Button butonRengi(Button button, int boyut) {

        StateListDrawable drawable = (StateListDrawable) button.getBackground();
        DrawableContainer.DrawableContainerState dcs = (DrawableContainer.DrawableContainerState) drawable
                .getConstantState();
        Drawable[] drawable1 = dcs.getChildren();
        GradientDrawable gr = (GradientDrawable) drawable1[0];
        gr.setColor(aliskanlik.getRenk());


        switch (aliskanlik.getRenk()) {
            case -945014:
                gr.setStroke(boyut / 30, Color.parseColor("#E74C3C"));
                break;
            case -4485170:
                gr.setStroke(boyut / 30, Color.parseColor("#8E44AD"));
                break;
            case -8011287:
                gr.setStroke(boyut / 30, Color.parseColor("#3498DB"));
                break;
            case -9189706:
                gr.setStroke(boyut / 30, Color.parseColor("#16A085"));
                break;
            case -8200022:
                gr.setStroke(boyut / 30, Color.parseColor("#2ECC71"));
                break;
            case -473999:
                gr.setStroke(boyut / 30, Color.parseColor("#F39C12"));
                break;
            case -1730458:
                gr.setStroke(boyut / 30, Color.parseColor("#DC7633"));
                break;
            case -2630691:
                gr.setStroke(boyut / 30, Color.parseColor("#7F8C8D"));
                break;
            case -8352874:
                gr.setStroke(boyut / 30, Color.parseColor("#34495E"));
                break;
        }


        button.setBackground(gr);


        return button;
    }

    public static void buttonEffect(View button, final Aliskanlik aliskanlik) {
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(aliskanlik.getRenk(), PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    public void yuzdeArttir(Aliskanlik aliskanlik, String child) {

        int yeniYuzde = aliskanlik.getYuzde() + 100 / aliskanlik.getHedef();

        aliskanlik.setYuzde(Math.min(yeniYuzde, 100));
        reference1.child(userID).child("Aliskanliklar").child(child).setValue(aliskanlik);
    }

    public void yuzdeSifirla() {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        final String formattedDate = df.format(c);

        ValueEventListener vl = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String str = formattedDate;
                if (currentUser != null){
                    try {
                        str = dataSnapshot.child(userID).child("tarih").getValue().toString();
                    }catch (Exception e){
                        System.out.println(e);
                    }
                }

                if (!str.equals(formattedDate)) {
                    sifirla(dataSnapshot);
                    reference1.child(userID).child("tarih").setValue(formattedDate);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        reference1.addListenerForSingleValueEvent(vl);

    }

    public void sifirla(DataSnapshot dataSnapshot) {

        Iterable<DataSnapshot> keys = dataSnapshot.child(userID).child("Aliskanliklar").getChildren();

        for (DataSnapshot key : keys) {
            aliskanlik = key.getValue(Aliskanlik.class);
            aliskanlik.setYuzde(0);
            reference1.child(userID).child("Aliskanliklar").child(key.getKey()).setValue(aliskanlik);
        }
    }


}