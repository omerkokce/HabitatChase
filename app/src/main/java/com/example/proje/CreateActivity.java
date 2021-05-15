package com.example.proje;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import petrov.kristiyan.colorpicker.ColorPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateActivity extends AppCompatActivity {

    int colorcik = -945014;

    private EditText txtIsim = null;
    private EditText txtHedef = null;
    private Button fab = null;
    private Button btnKaydet = null;

    private GridLayout mainLayout = null;
    private Aliskanlik aliskanlik = null;

    //Database
    private Firebase reference1;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    //Menu
    private Button btnYapilan = null;
    private Button btnPaylasim = null;
    private Button btnAnasayfa = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        colorPicker();

        init();
    }

    public void init() {
        txtIsim = findViewById(R.id.txtName);
        txtHedef = findViewById(R.id.txtHedef);
        btnKaydet = findViewById(R.id.btnKaydet);
        mainLayout = findViewById(R.id.main_layout);

        //Database
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        Firebase.setAndroidContext(CreateActivity.this);
        reference1 = new Firebase(
                "https://aliskanlik-1b5a3-default-rtdb.europe-west1.firebasedatabase.app/Kullanicilar");

        aliskanlik = new Aliskanlik();

        //Menu
        btnYapilan = findViewById(R.id.btnYapilan);
        btnPaylasim = findViewById(R.id.btnPaylasim);
        btnAnasayfa = findViewById(R.id.btnAnasayfa);


        btnYapilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, YapilanActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
            }
        });
        btnAnasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
            }
        });
        btnPaylasim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, PaylasimActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
            }
        });

        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aliskanlikEkle();
            }
        });

    }

    public void aliskanlikEkle() {
        String isim = txtIsim.getText().toString();
        int hedef;
        int yuzde;

        Pattern pattern = Pattern.compile("^[0-9]+$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txtHedef.getText().toString());
        boolean matchFound = matcher.find();

        if (!matchFound) {
            Toast.makeText(this, "Lütfen sadece sayı giriniz", Toast.LENGTH_SHORT).show();
        } else if (isim.isEmpty()) {
            Toast.makeText(this, "Lütfen bir isim giriniz", Toast.LENGTH_SHORT).show();
        } else {
            hedef = Integer.parseInt(txtHedef.getText().toString());
            yuzde = 0;

            aliskanlik.setIsim(isim);
            aliskanlik.setHedef(hedef);
            aliskanlik.setYuzde(yuzde);
            aliskanlik.setRenk(colorcik);

            reference1.child(currentUser.getUid()).child("Aliskanliklar").push().setValue(aliskanlik);
            reference1.child(currentUser.getUid()).child("tarih").setValue(tarihAl());

            Intent intent = new Intent(CreateActivity.this, MainActivity.class);
            startActivity(intent);

            finish();
        }
    }

    public void colorPicker() {
        fab = findViewById(R.id.fab);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ColorPicker colorPicker = new ColorPicker(CreateActivity.this);
                    ArrayList<String> colors = new ArrayList<>();
                    colors.add("#F1948A");
                    colors.add("#BB8FCE");
                    colors.add("#85C1E9");
                    colors.add("#73C6B6");
                    colors.add("#82E0AA");
                    colors.add("#F8C471");
                    colors.add("#E59866");
                    colors.add("#D7DBDD");
                    colors.add("#808B96");

                    colorPicker.setDefaultColorButton(Color.parseColor("#f84c44"))
                            .setColors(colors)
                            .setColumns(5)
                            .setRoundColorButton(true)
                            .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                                @Override
                                public void onChooseColor(int position, int color) {
                                    fab.setBackgroundColor(color);
                                    colorcik = color;
                                }

                                @Override
                                public void onCancel() {

                                }
                            }).show();
                }
            });
        }
    }

    public String tarihAl() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        return df.format(c);
    }
}