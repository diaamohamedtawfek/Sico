package com.example.sico;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by Diaa on 1/10/2018.
 */

public class LibtChat extends Application {

   public DatabaseReference databaseuser;
    public FirebaseAuth auth;
    @Override
    public void onCreate() {
        super.onCreate();

        // use to catch data if net offline
        //must add in manifest in application

         FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //to catch image
        Picasso.Builder builder=new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso build=builder.build();
        build.setIndicatorsEnabled(true);
        build.setLoggingEnabled(true);
        Picasso.setSingletonInstance(build);

      auth= FirebaseAuth.getInstance();
     /* databaseuser=FirebaseDatabase.getInstance().getReference().child("User").child(auth.getCurrentUser().getUid());
      databaseuser.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {

              if (dataSnapshot !=null){
              databaseuser.child("online").onDisconnect().setValue(false);
             databaseuser.child("online").setValue(true);
          }
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });*/
    }
}
