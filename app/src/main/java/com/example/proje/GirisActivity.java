package com.example.proje;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GirisActivity extends AppCompatActivity {

    public Toolbar actionBarLogin;
    private EditText txtMailGiris, txtParolaGiris;
    private Button btnGirisYap;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        init();

        btnGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uygulamayaGiris();
            }

        });

    }

    public void init() {
        actionBarLogin = (Toolbar) findViewById(R.id.action_barGiris);
        setSupportActionBar(actionBarLogin);
        getSupportActionBar().setTitle("Giriş Yap");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        txtMailGiris = findViewById(R.id.txtMailGiris);
        txtParolaGiris = findViewById(R.id.txtParolaGiris);
        btnGirisYap = findViewById(R.id.btnGirisYap);
    }

    private void uygulamayaGiris() {

        String mail = txtMailGiris.getText().toString();
        String parola = txtParolaGiris.getText().toString();

        if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(parola)) {
            Toast.makeText(this, "Lütfen bütün alanları doldurunuz", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(mail, parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        Intent login = new Intent(GirisActivity.this, MainActivity.class);
                        startActivity(login);
                        overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
                        finish();
                    } else
                        Toast.makeText(GirisActivity.this, "Bir hata oluştu", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}