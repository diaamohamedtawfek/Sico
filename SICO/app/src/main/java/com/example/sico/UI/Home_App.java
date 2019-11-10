package com.example.sico.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.sico.R;
import com.example.sico.SectionPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Home_App extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    ViewPager viewPager;
    TabLayout tabLayout;
    SectionPagerAdapter sectionPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__app);

        viewPager=findViewById(R.id.main_viewpager);
        sectionPagerAdapter=new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionPagerAdapter);

        tabLayout=findViewById(R.id.main_tablayout);
        tabLayout.setupWithViewPager(viewPager);


//        for (int i=0 ; i<10 ;i++) {
//            sendmagetofirbase();
//        }
    }



    private void sendmagetofirbase() {


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Shopping");
        databaseReference.keepSynced(true);
        String id = String.valueOf(System.currentTimeMillis());
        final Map masedegs = new HashMap<>();
        masedegs.put("name", "ddsdd");
        masedegs.put("id", id);
        masedegs.put("price", "text");
        masedegs.put("image", "defult");


                databaseReference.child(id).setValue(masedegs).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(Home_App.this, "Send successful", Toast.LENGTH_SHORT).show();

//                            //get data about user and sent to  reqest_tybe
//                            DatabaseReference databaseReferenceuser = FirebaseDatabase.getInstance().getReference().child("massage");
//                            databaseReferenceuser.child(userid).child(firebaseUser.getUid()).child(id).setValue(masedegs).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//
//
//
//                                        Toast.makeText(MasedgeChat.this, "Send successful", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
                        } else {
                           // Toast.makeText(MasedgeChat.this, "Send not successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}
