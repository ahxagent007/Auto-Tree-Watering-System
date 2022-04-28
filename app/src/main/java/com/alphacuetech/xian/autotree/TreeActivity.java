package com.alphacuetech.xian.autotree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.alphacuetech.xian.autotree.Models.Tree;
import com.alphacuetech.xian.autotree.functions.SharedPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class TreeActivity extends AppCompatActivity {

    String TAG = "XIAN";
    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);

        databaseRef = FirebaseDatabase.getInstance().getReference("TREE");
        String uid = "123456";
        ArrayList<Tree> trees = getTreesFirebase(uid);

    }

    public ArrayList<Tree> getTreesFirebase(String uid){
        Query query = databaseRef.orderByChild("userId").equalTo(new SharedPref(getApplicationContext()).getUID());
        Log.i(TAG, new SharedPref(getApplicationContext()).getUID());

        ArrayList<Tree> trees = new ArrayList<>();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trees.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Tree tree = ds.getValue(Tree.class);
                    //Log.i(TAG, ds.toString());
                    trees.add(tree);
                }

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {

                        /*RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        RV_mix.setLayoutManager(mLayoutManager);

                        Collections.reverse(FoodMixDB);

                        RecyclerView.Adapter mRecycleAdapter = new RecycleViewAdapterForFoodMix(getApplicationContext(), FoodMixDB);
                        RV_mix.setAdapter(mRecycleAdapter);*/

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Internet Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}