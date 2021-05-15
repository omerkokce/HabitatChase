package com.example.proje;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PaylasimActivity extends AppCompatActivity {


    //Menu
    private Button btnYapilan = null;
    private Button btnAnasayfa = null;

    private Button btnArkadas = null;
    private EditText txtArkadas = null;
    private LinearLayout layoutarkadaslar = null;

    private Firebase reference1;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = auth.getCurrentUser();
    String userID = currentUser.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paylasim);

        init();
        arkadascek();
    }

    public void init() {

        Firebase.setAndroidContext(PaylasimActivity.this);
        reference1 = new Firebase(
                "https://aliskanlik-1b5a3-default-rtdb.europe-west1.firebasedatabase.app/Kullanicilar/");

        //Menu
        btnYapilan = findViewById(R.id.btnYapilan);
        btnAnasayfa = findViewById(R.id.btnAnasayfa);

        btnArkadas = findViewById(R.id.btnArkadas);
        txtArkadas = findViewById(R.id.txtArkadas);
        layoutarkadaslar = findViewById(R.id.layoutarkadas);

        btnYapilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaylasimActivity.this, YapilanActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
            }
        });
        btnAnasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaylasimActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
            }
        });

        btnArkadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kontrol();
            }
        });

    }

    public void arkadascek() {

        ValueEventListener vl = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> keys = dataSnapshot.child(userID).child("Arkadaslar").getChildren();
                for (final DataSnapshot key : keys) {

                    String name = key.getKey();

                    RelativeLayout rl = new RelativeLayout(PaylasimActivity.this);
                    rl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                            , ViewGroup.LayoutParams.WRAP_CONTENT));
                    //rl.setBackgroundResource(R.drawable.textboxes);
                    RelativeLayout.LayoutParams rlcik = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                            , ViewGroup.LayoutParams.WRAP_CONTENT);
                    rlcik.setMargins(0, 0, 0, 50);
                    rl.setLayoutParams(rlcik);

                    RelativeLayout.LayoutParams rlName = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    rlName.setMargins(20, 25, 0, 0);

                    final TextView txt = new TextView(PaylasimActivity.this);
                    txt.setText(name);
                    txt.setTextSize(20);

                    txt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PaylasimActivity.this,Arkadas_yapilan.class);

                            intent.putExtra("keyValue",key.getValue().toString());
                            intent.putExtra("user",txt.getText());

                            startActivity(intent);
                            overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
                        }
                    });

                    txt.setLayoutParams(rlName);

                    RelativeLayout.LayoutParams rlEdit = new RelativeLayout.LayoutParams(
                            100, 100);
                    rlEdit.setMargins(0, 15, 15, 0);

                    Button btnEdit = new Button(PaylasimActivity.this);
                    btnEdit.setBackgroundResource(R.drawable.delete_icon);
                    btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sil(key);
                        }
                    });

                    btnEdit.setLayoutParams(rlEdit);

                    rl.addView(txt);
                    rl.addView(btnEdit);

                    rlEdit = (RelativeLayout.LayoutParams) btnEdit.getLayoutParams();
                    rlEdit.addRule(RelativeLayout.ALIGN_PARENT_END);

                    rlName = (RelativeLayout.LayoutParams) txt.getLayoutParams();
                    rlName.addRule(RelativeLayout.ALIGN_PARENT_START);

                    txt.setLayoutParams(rlName);
                    btnEdit.setLayoutParams(rlEdit);

                    layoutarkadaslar.addView(rl);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        reference1.addListenerForSingleValueEvent(vl);
    }

    public void kontrol() {

        final String kullaniciAdi = txtArkadas.getText().toString();

        if (!kullaniciAdi.isEmpty()) {
            ValueEventListener vl = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Iterable<DataSnapshot> keys = dataSnapshot.child("KullaniciBilgi").getChildren();
                    boolean x = true;
                    for (DataSnapshot key : keys) {
                        if (kullaniciAdi.equals(key.getKey())) {
                            arkadasekle(key);
                            txtArkadas.setText("");
                            x = false;
                            break;
                        }
                    }
                    if (x)
                        Toast.makeText(PaylasimActivity.this, "Bu isimde bir kullanıcı bulunmamaktadır."
                                , Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            };
            reference1.addListenerForSingleValueEvent(vl);
        }
    }

    public void arkadasekle(DataSnapshot key) {
        reference1.child(currentUser.getUid()).child("Arkadaslar").child(key.getKey()).setValue(key.getValue());

        finish();
        startActivity(getIntent());
        overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
    }

    public void sil(final DataSnapshot key) {
        ValueEventListener vl = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final AlertDialog.Builder adb = new AlertDialog.Builder(PaylasimActivity.this);
                adb.setTitle("Arkadaşlıktan çıkarmak istiyor msunuz?");
                adb.setIcon(android.R.drawable.ic_dialog_alert);
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        reference1.child(userID).child("Arkadaslar").child(key.getKey()).removeValue();
                        Toast.makeText(PaylasimActivity.this, key.getKey()+" kullanıcı silindi."
                                , Toast.LENGTH_SHORT).show();

                        finish();
                        startActivity(getIntent());
                        overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);


                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                adb.show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        reference1.addListenerForSingleValueEvent(vl);
    }
}