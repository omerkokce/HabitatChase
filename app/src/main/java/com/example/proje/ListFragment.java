package com.example.proje;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ListFragment extends Fragment {

    private Button btnKapat;
    private View view;
    private LinearLayout layoutAliskanliklar;
    FragmentManager fragmentManager;

    //Database
    private Firebase reference1;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = auth.getCurrentUser();
    String userId = currentUser.getUid();

    Aliskanlik aliskanlik;

    public ListFragment() {
        super(R.layout.fragment_list);
    }

    /*public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_list, container, false);

        init(view);
        aliskanlikCek();
        return view;
    }

    public void init(final View view) {

        btnKapat = view.findViewById(R.id.btnKapat);
        layoutAliskanliklar = view.findViewById(R.id.layout);

        Firebase.setAndroidContext(getContext());
        reference1 = new Firebase("https://aliskanlik-1b5a3-default-rtdb.europe-west1.firebasedatabase.app/Kullanicilar/");

        btnKapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
            }
        });

    }

    public void aliskanlikCek() {

        ValueEventListener vl = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> keys = dataSnapshot.child(userId).child("Aliskanliklar").getChildren();

                for (final DataSnapshot key : keys) {

                    aliskanlik = key.getValue(Aliskanlik.class);
                    String yazi = aliskanlik.getIsim();

                    RelativeLayout rl = new RelativeLayout(getActivity());
                    rl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                            , ViewGroup.LayoutParams.WRAP_CONTENT));
                    rl.setBackgroundResource(R.drawable.textboxes);
                    RelativeLayout.LayoutParams rlcik = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                            , ViewGroup.LayoutParams.WRAP_CONTENT);
                    rlcik.setMargins(0, 0, 0, 50);
                    rl.setLayoutParams(rlcik);

                    RelativeLayout.LayoutParams rlName = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    rlName.setMargins(20, 25, 0, 0);

                    TextView txt = new TextView(getActivity());
                    txt.setText(yazi);
                    txt.setTextSize(20);

                    txt.setLayoutParams(rlName);

                    RelativeLayout.LayoutParams rlEdit = new RelativeLayout.LayoutParams(
                            100, 100);
                    rlEdit.setMargins(0, 15, 15, 0);

                    Button btnEdit = new Button(getActivity());
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

                    layoutAliskanliklar.addView(rl);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        reference1.addListenerForSingleValueEvent(vl);

    }

    public void sil(final DataSnapshot key) {
        ValueEventListener vl = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Bu alışkanlığı silmek istiyor msunuz?");
                adb.setIcon(android.R.drawable.ic_dialog_alert);
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        reference1.child(userId).child("Aliskanliklar").child(key.getKey()).removeValue();

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(ListFragment.this).attach(ListFragment.this).commit();

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