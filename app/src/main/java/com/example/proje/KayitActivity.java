package com.example.proje;

import android.content.Intent;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class KayitActivity extends AppCompatActivity {

    public Toolbar actionBarKayit;
    private EditText txtKullaniciAdi, txtMailKayit, txtParolaKayit;
    private Button btnKayitOl;

    //Database
    private FirebaseAuth auth;
    private Firebase reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);

        init();

        btnKayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yeniHesapOlustur();
            }

        });
    }


    public void init() {


        actionBarKayit = (Toolbar) findViewById(R.id.action_barKayit);
        setSupportActionBar(actionBarKayit);
        getSupportActionBar().setTitle("Giriş Yap");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Database
        auth = FirebaseAuth.getInstance();
        reference = new Firebase(
                "https://aliskanlik-1b5a3-default-rtdb.europe-west1.firebasedatabase.app/Kullanicilar/");

        txtKullaniciAdi = findViewById(R.id.txtKullaniciAdi);
        txtMailKayit = findViewById(R.id.txtMailKayit);
        txtParolaKayit = findViewById(R.id.txtParolaKayit);
        btnKayitOl = findViewById(R.id.btnKayitOl);
    }

    private void yeniHesapOlustur() {

        final String kullaniciAdi = txtKullaniciAdi.getText().toString();
        final String mail = txtMailKayit.getText().toString();
        String parola = txtParolaKayit.getText().toString();

        if (TextUtils.isEmpty(kullaniciAdi) || TextUtils.isEmpty(mail) || TextUtils.isEmpty(parola)) {
            Toast.makeText(this, "Lütfen bütün alanları doldurunuz", Toast.LENGTH_SHORT).show();
        } else {
            auth.createUserWithEmailAndPassword(mail, parola).
                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                /*ValueEventListener vl = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        boolean x = true;
                                        Iterable<DataSnapshot> keys = dataSnapshot.child("KullaniciBilgi").getChildren();

                                        for (DataSnapshot key : keys) {
                                            if (key.getKey().equals(kullaniciAdi)) {
                                                x = false;
                                                Toast.makeText(KayitActivity.this, "Bu kullanıcı adı " +
                                                        "halihazırda kullanılmaktadır.", Toast.LENGTH_SHORT).show();
                                                break;
                                            }
                                        }
                                        if (x) {
                                            Toast.makeText(KayitActivity.this, "Hesap başarıyla oluşturuldu",
                                                    Toast.LENGTH_SHORT).show();

                                            reference.child("KullaniciBilgi").child(kullaniciAdi).setValue(auth.getUid());

                                            Intent login = new Intent(KayitActivity.this, GirisActivity.class);
                                            startActivity(login);
                                            overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                };
                                reference.addListenerForSingleValueEvent(vl);*/

                                reference.child("KullaniciBilgi").child(kullaniciAdi).setValue(auth.getUid());

                                Intent login = new Intent(KayitActivity.this, GirisActivity.class);
                                startActivity(login);
                                overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
                                finish();

                            } else {
                                Toast.makeText(KayitActivity.this, "Bir Hata Oluştu",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}