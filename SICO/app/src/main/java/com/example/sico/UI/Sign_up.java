package com.example.sico.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sico.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class Sign_up extends AppCompatActivity {

    Button sinin,sinup;
    EditText email,password;
    FirebaseAuth auth;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        storageReference= FirebaseStorage.getInstance().getReference();
        auth=FirebaseAuth.getInstance();


        define_UI();
        sinup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_firebase();
            }
        });
    }

    private void add_firebase() {
        if (!validateEmail() |  !validatePassword()) {
            //  String input = "Email: " + textInputEmail.getEditText().getText().toString();
            return;
        }else {

            String emails=email.getText().toString().trim();
            String passwords=password.getText().toString().trim();
            final ProgressDialog progressDialog = new ProgressDialog(Sign_up.this);
            progressDialog.setTitle("Register User");
            progressDialog.setMessage("Please Waite ........");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            auth.createUserWithEmailAndPassword(emails, passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Toast.makeText(Regester.this, "to caret", Toast.LENGTH_SHORT).show();
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = firebaseUser.getUid();

                        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(uid);
                        HashMap<String, String> usermap = new HashMap<>();
                        usermap.put("email", email.getText().toString().trim());
                        usermap.put("image","default");
                        usermap.put("id", uid);
                        usermap.put("states", "Hi there, Iam use Chat App");
                        databaseReference.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(getApplicationContext(), Home_App.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Toast.makeText(Sign_up.this, "add databse", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    finish();
                                } else {
                                    progressDialog.dismiss();
                                    email.setError("Set your Data must have @ and .com");
                                    password.setError("Must password greater than 6 ");
                                    Toast.makeText(Sign_up.this, "trey agine", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        email.setError("Set your Data must have @ and .com");
                        password.setError("Must password greater than 6 ");
                        Toast.makeText(getApplicationContext(), "You get some error ", Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }
                }
            });

        }
    }





    private void define_UI() {
        sinup=findViewById(R.id.bu_Sinup_signup);
        email=findViewById(R.id.ed_username_signup);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        password=findViewById(R.id.ed_password_signup);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }



    private boolean validatePassword() {
        String passwordInput = password.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            password.setFocusable(true);
            return false;
        }
        else if (passwordInput.length()<6) {
            password.setError("Password length must be more 6 string");
            return false;
        }
        else {
            password.setError(null);
            return true;
        }
    }
    private boolean validateEmail() {
        String emailInput = email.getText().toString().trim();

        if (emailInput.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Please enter a valid email address Like ex@gmail.com");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
