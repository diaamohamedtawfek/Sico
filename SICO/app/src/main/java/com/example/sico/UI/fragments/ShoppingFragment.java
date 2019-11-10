package com.example.sico.UI.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sico.Models.ShoppingData;
import com.example.sico.R;
import com.example.sico.UI.Singel_item;
import com.example.sico.ViewHolder.MenuViewHolder;
import com.example.sico.interface_ui.itemOnClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{


    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    View viewShopping;

    SwipeRefreshLayout mSwipeRefreshLayout;

    public ShoppingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewShopping= inflater.inflate(R.layout.fragment_shopping, container, false);
        recyclerView=viewShopping.findViewById(R.id.recycal_frind);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Shopping");
        //databaseReference.keepSynced(true);//to catch data if net offline

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mSwipeRefreshLayout = (SwipeRefreshLayout) viewShopping.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);
                startUI();
            }
        });

        return viewShopping;
    }

    @Override
    public void onRefresh() {
        startUI();
    }
    private void startUI() {
        FirebaseRecyclerAdapter<ShoppingData, MenuViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<ShoppingData, MenuViewHolder>(
                ShoppingData.class,R.layout.item_shopping, MenuViewHolder.class,databaseReference
        ) {
            @Override
            protected void populateViewHolder(final MenuViewHolder viewHolder, final ShoppingData model, int position) {

                final String postke=getRef(position).getKey();
                databaseReference.child(postke).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mSwipeRefreshLayout.setRefreshing(false);
                        final String id=dataSnapshot.child("id").getValue().toString();
                        final String name=dataSnapshot.child("name").getValue().toString();
                        final String image=dataSnapshot.child("image").getValue().toString();
                        final String price=dataSnapshot.child("price").getValue().toString();
                        Log.e("image",image);

                        viewHolder.titeltext.setText("price : "+model.getPrice());
                        viewHolder.posttext.setText("Name : "+model.getName());

                        Picasso.with(getActivity()).load(model.getImage()).into(viewHolder.images);

                        viewHolder.setItemOnClickListener(new itemOnClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {
                                Intent intent=new Intent(getContext(), Singel_item.class);
                                intent.putExtra("id",id);
                                intent.putExtra("image",image);
                                intent.putExtra("name",name);
                                intent.putExtra("price",price);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
