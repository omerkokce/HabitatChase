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
import com.google.firebase.auth.FirebaseUser;

public class YapilanActivity extends AppCompatActivity {

    private LinearLayout yapilanLayout;

    //Menu
    private Button btnPaylasim = null;
    private Button btnAnasayfa = null;

    //Database
    private Firebase reference1;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    private Aliskanlik aliskanlik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yapilan);

        init();
        yuzdeGetir();
    }

    public void init() {

        yapilanLayout = findViewById(R.id.yapilanlayout);

        //Menu
        btnPaylasim = findViewById(R.id.btnPaylasim);
        btnAnasayfa = findViewById(R.id.btnAnasayfa);

        //Database
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        Firebase.setAndroidContext(YapilanActivity.this);
        reference1 = new Firebase(
                "https://aliskanlik-1b5a3-default-rtdb.europe-west1.firebasedatabase.app/Kullanicilar/"
                        + currentUser.getUid());

        btnAnasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YapilanActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
            }
        });
        btnPaylasim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YapilanActivity.this, PaylasimActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
            }
        });
    }

    public void yuzdeGetir() {

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> keys = dataSnapshot.child("Aliskanliklar").getChildren();

                for (final DataSnapshot key : keys) {

                    aliskanlik = key.getValue(Aliskanlik.class);
                    String yazi = aliskanlik.getIsim();

                    //Textview oluşturma
                    TextView txt = new TextView(YapilanActivity.this);
                    txt.setText(yazi);
                    txt.setTextSize(20);

                    //Progressbar oluşturma
                    ProgressBar pb;
                    RelativeLayout relativeLayout = new RelativeLayout(YapilanActivity.this);
                    relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                            , ViewGroup.LayoutParams.WRAP_CONTENT));


                    RelativeLayout.LayoutParams rlIlerleme = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    rlIlerleme.setMarginEnd(20);

                    pb = new ProgressBar(YapilanActivity.this, null,
                            android.R.attr.progressBarStyleHorizontal);
                    pb.setProgress(aliskanlik.getYuzde());
                    pb.setMax(100);
                    pb.setLayoutParams(rlIlerleme);

                    TextView txtIlerleme = new TextView(YapilanActivity.this);
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