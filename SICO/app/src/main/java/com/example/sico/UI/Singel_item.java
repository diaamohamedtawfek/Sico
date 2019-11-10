package com.example.sico.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sico.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.kashier.sdk.Core.model.Response.Error.ErrorData;
import io.kashier.sdk.Core.model.Response.Payment.Card;
import io.kashier.sdk.Core.model.Response.Payment.PaymentResponse;
import io.kashier.sdk.Core.model.Response.UserCallback;
import io.kashier.sdk.Core.network.SDK_MODE;
import io.kashier.sdk.KASHIER_DISPLAY_LANG;
import io.kashier.sdk.Kashier;
import io.kashier.sdk.UserDailogUtils.PaymentActivityConfig;
import retrofit2.Response;

public class Singel_item extends AppCompatActivity {

    String id,name,price,image;
    CircleImageView imageView;
    TextView name_te,price_te,id_te;
    Button buy;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singel_item);
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();

        String currency = "EGP";
        String ApiKeyId = "36d8b127-151c-4302-a6f2-0edaa0e91145";
        String merchantId = "MID-640-970";
        Kashier.init(Singel_item.this, merchantId, ApiKeyId, currency, SDK_MODE.DEVELOPMENT, KASHIER_DISPLAY_LANG.AR);

        Bundle extras = getIntent().getExtras(); // to get move intent
        if (extras != null) {
            String a = extras.getString("id");
            String names = extras.getString("name");
            String images = extras.getString("image");
            String pic = extras.getString("price");
            if (a!=null){
                id= a;
                name=names;
                price=pic;
                image=images;
            }
        }
        define_UI();

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendmagetofirbase();
            }
        });

    }

    private void define_UI() {
        name_te=findViewById(R.id.text_name_singelitem);
        name_te.setText("Name : " +name);
        price_te=findViewById(R.id.text_price_singelitem);
        price_te.setText("Price : "+price);
        id_te=findViewById(R.id.text_id_singelitem);
        id_te.setText("Id : "+id);

        imageView=findViewById(R.id.circilimage_contextfrind);
        Picasso.with(Singel_item.this).load(image).into(imageView);

        buy=findViewById(R.id.bu_buy_singelitem);

    }

    String idspo = String.valueOf(System.currentTimeMillis());
    private void sendmagetofirbase() {

        Kashier.startPaymentActivity(
                Singel_item.this,
                firebaseUser.getUid(),
                idspo,
                price,
                new UserCallback<PaymentResponse>() {

                    @Override
                    public void onResponse(Response<PaymentResponse> response) {
                        Log.e("jhjh","done");
                        addtofirebase();
                    }

                    @Override
                    public void onFailure(ErrorData<PaymentResponse> errorData) {

                    }
                },
                new PaymentActivityConfig()
                        .setActivityTitle("Kashier Payment")
                        /**
                         *  %1$s => Transfer Amount
                         *  %2$s => Kashier Currency
                         */
                        .setButtonLabel("PAY NOW %2$s %1$s")

        );

    }

    private void addtofirebase() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("History").child(firebaseUser.getUid());
        databaseReference.keepSynced(true);
        //String id = String.valueOf(System.currentTimeMillis());
        final Map masedegs = new HashMap<>();
        masedegs.put("id_user", firebaseUser.getUid());
        masedegs.put("id_item", id);
        masedegs.put("date", currentDateandTime);


        databaseReference.child(id).setValue(masedegs).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Singel_item.this, "Send successful", Toast.LENGTH_SHORT).show();

                } else {
                    // Toast.makeText(MasedgeChat.this, "Send not successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
