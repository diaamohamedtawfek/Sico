package com.example.sico.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sico.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    Button sinin,sinup;
    EditText email,password;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        if(firebaseUser != null){
            startActivity(new Intent(getApplicationContext(), Home_App.class));
            finish();
        }


        define_UI();

        sinin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Register User");
                progressDialog.setMessage("Please Waite ........");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                String emales=email.getText().toString();
                String passswords=password.getText().toString();

                if (!TextUtils.isEmpty(emales)&&!TextUtils.isEmpty(passswords)) {
                    auth.signInWithEmailAndPassword(emales, passswords).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getApplicationContext(), Home_App.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                progressDialog.dismiss();
                                finish();
                            } else {
                                email.setError("Check your email");
                                password.setError("Check your password ");
                                progressDialog.hide();
                            }
                        }
                    });
                }
                else {
                    progressDialog.hide();
                    //lenthpass.setTextColor(R.color.read);
                    email.setError("Set your email");
                    password.setError("Set your password ");
                }
            }
        });

    }

    private void define_UI() {
        sinup=findViewById(R.id.bu_Sinup_login);
        sinup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            startActivity(new Intent(getApplicationContext(), Sign_up.class));
            finish();
            }
        });

        email=findViewById(R.id.ed_username_login);
        password=findViewById(R.id.ed_password_login);
        sinin=findViewById(R.id.bu_login_login);
    }
}
