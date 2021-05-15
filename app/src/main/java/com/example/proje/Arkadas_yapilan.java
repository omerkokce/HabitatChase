package com.example.proje;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

public class Arkadas_yapilan extends AppCompatActivity {

    private LinearLayout yapilanLayout;

    //Menu
    private Button btnPaylasim = null;
    private Button btnAnasayfa = null;
    private Button btnYapilan = null;

    //Database
    private Firebase reference1;

    private Aliskanlik aliskanlik;
    String user = null;
    String key = null;
    TextView txtUser=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arkadas_yapilan);

        init();
    }

    public void init() {
        yapilanLayout = findViewById(R.id.arkadasYapilanlayout);
        txtUser = findViewById(R.id.txtUserName);

        //Menu
        btnPaylasim = findViewById(R.id.btnPaylasim);
        btnAnasayfa = findViewById(R.id.btnAnasayfa);
        btnYapilan = findViewById(R.id.btnYapilan);

        Firebase.setAndroidContext(Arkadas_yapilan.this);
        reference1 = new Firebase(
                "https://aliskanlik-1b5a3-default-rtdb.europe-west1.firebasedatabase.app/Kullanicilar/");

        btnAnasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Arkadas_yapilan.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
            }
        });
        btnPaylasim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Arkadas_yapilan.this, PaylasimActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
            }
        });
        btnYapilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Arkadas_yapilan.this, YapilanActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
            }
        });

        user = getIntent().getExtras().getString("user");
        key = getIntent().getExtras().getString("keyValue");

        yuzdeGetir(key);

    }

    public void yuzdeGetir(final String key) {

        txtUser.setText(user);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> keys = dataSnapshot.child(key).child("Aliskanliklar").getChildren();

                if (null != yapilanLayout && yapilanLayout.getChildCount() > 0) {
                    try {
                        yapilanLayout.removeViews (0, yapilanLayout.getChildCount());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                for (final DataSnapshot key : keys) {

                    aliskanlik = key.getValue(Aliskanlik.class);
                    String yazi = aliskanlik.getIsim();

                    //Textview oluşturma
                    TextView txt = new TextView(Arkadas_yapilan.this);
                    txt.setText(yazi);
                    txt.setTextSize(20);

                    //Progressbar oluşturma
                    ProgressBar pb;
                    RelativeLayout relativeLayout = new RelativeLayout(Arkadas_yapilan.this);
                    relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                            , ViewGroup.LayoutParams.WRAP_CONTENT));


                    RelativeLayout.LayoutParams rlIlerleme = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    rlIlerleme.setMarginEnd(20);

                    pb = new ProgressBar(Arkadas_yapilan.this, null,
                            android.R.attr.progressBarStyleHorizontal);
                    pb.setProgress(aliskanlik.getYuzde());
                    pb.setMax(100);
                    pb.setLayoutParams(rlIlerleme);

                    TextView txtIlerleme = new TextView(Arkadas_yapilan.this);
                    txtIlerleme.setText(aliskanlik.getYuzde()+"%");
                    txtIlerleme.setTextSize(15);
                    txtIlerleme.setId(1);

                    relativeLayout.addView(pb);
                    relativeLayout.addView(txtIlerleme);

                    rlIlerleme = (RelativeLayout.LayoutParams) pb.getLayoutParams();
                    rlIlerleme.addRule(RelativeLayout.START_OF, 1);

                    RelativeLayout.LayoutParams rlIlerlemeYuzde = (RelativeLayout.LayoutParams) txtIlerleme.getLayoutParams();
                    rlIlerlemeYuzde.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                    txtIlerleme.setLayoutParams(rlIlerlemeYuzde);
                    pb.setLayoutParams(rlIlerleme);


                    yapilanLayout.addView(txt);
                    yapilanLayout.addView(relativeLayout);

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        reference1.addValueEventListener(listener);
    }
}