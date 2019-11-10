package com.example.sico.UI.fragments;


import android.content.Context;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    String Userid_fromstrin;
    View viewShopping;

    SwipeRefreshLayout mSwipeRefreshLayout;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewShopping= inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView=viewShopping.findViewById(R.id.recycal_frind);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("History").child(firebaseUser.getUid());
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
        FirebaseRecyclerAdapter<ShoppingData, ShoppingRecycalvew> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<ShoppingData, ShoppingRecycalvew>(
                ShoppingData.class,R.layout.item_history, ShoppingRecycalvew.class,databaseReference
        ) {
            @Override
            protected void populateViewHolder(final ShoppingRecycalvew viewHolder, final ShoppingData model, int position) {

                final String postke=getRef(position).getKey();
                databaseReference.child(postke).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String iditems=dataSnapshot.child("id_item").getValue().toString();
                        final String date=dataSnapshot.child("date").getValue().toString();

                        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Shopping").child(iditems);
                        databaseReference.keepSynced(true);//to catch data if net offline
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                mSwipeRefreshLayout.setRefreshing(false);
//                                final String id = dataSnapshot.child("id").getValue().toString();
                                final String name = dataSnapshot.child("name").getValue().toString();
                                final String image = dataSnapshot.child("image").getValue().toString();
                                final String price = dataSnapshot.child("price").getValue().toString();
                                Log.e("image", image);

                                viewHolder.setStates(name);
                                viewHolder.setdate(date);
                                viewHolder.setAdd(price);
                                viewHolder.setImage(getContext(), image);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
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

    public static class ShoppingRecycalvew extends RecyclerView.ViewHolder{

        View mview;
        public ShoppingRecycalvew(View itemView) {
            super(itemView);
            mview=itemView;
        }
        public void setAdd(String add){
            //if i want on clic move text ,finview in conctructou and and on clic
            TextView titeltext=(TextView) mview.findViewById(R.id.text_sates_contestfrind);
            titeltext.setText("Price: "+add);
        }
        public void setStates(String postdesc){
            TextView posttext=(TextView) mview.findViewById(R.id.text_name_contestfrind);
            posttext.setText("Name: "+postdesc);
        }

        public void setdate(String date){
            TextView posttext=(TextView) mview.findViewById(R.id.text_date_contestfrind);
            posttext.setText("Date buy : "+date);
        }

        public void setImage(Context context, String imageoost){
            ImageView images= (ImageView) mview.findViewById(R.id.imagefone_contextfrind);
//            ImageView images= (ImageView) mview.findViewById(R.id.circilimage_contextfrind);
            Picasso.with(context).load(imageoost).into(images);
        }
    }

}
