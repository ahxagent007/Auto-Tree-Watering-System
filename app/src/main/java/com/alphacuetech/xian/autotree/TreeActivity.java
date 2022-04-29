package com.alphacuetech.xian.autotree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alphacuetech.xian.autotree.Models.DailyWeatherData;
import com.alphacuetech.xian.autotree.Models.Tree;
import com.alphacuetech.xian.autotree.functions.SharedPref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    DatabaseReference databaseRefTree, databaseRefUserTree;
    RecyclerView RV_tree;
    FloatingActionButton FBTN_add;

    ArrayList<Tree> trees = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);

        RV_tree = findViewById(R.id.RV_tree);
        FBTN_add = findViewById(R.id.FBTN_add);

        DailyWeatherData daily = (DailyWeatherData) getIntent().getSerializableExtra("DATA");

        Log.i(TAG, daily.toString());

        databaseRefTree = FirebaseDatabase.getInstance().getReference("TREE");
        databaseRefUserTree = FirebaseDatabase.getInstance().getReference("USER");

        String uid = "123456"; //new SharedPref(getApplicationContext()).getUID()

        getTreesFirebase();
        getUserTreesFirebase(uid);

        FBTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

    public void getTreesFirebase(){
        //Query query = databaseRef.orderByChild("tree_id").equalTo(new SharedPref(getApplicationContext()).getUID());
        Query query = databaseRefTree.orderByKey();


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trees.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Tree tree = ds.getValue(Tree.class);
                    trees.add(tree);
                }

                /*new Handler().post(new Runnable() {
                    @Override
                    public void run() {

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        RV_tree.setLayoutManager(mLayoutManager);

                        //Collections.reverse(FoodMixDB);

                        RecyclerView.Adapter mRecycleAdapter = new CustomAdapterRVTree(trees);
                        RV_tree.setAdapter(mRecycleAdapter);

                    }
                });*/

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

    public void getUserTreesFirebase(String uid){
        //Query query = databaseRef.orderByChild("tree_id").equalTo(new SharedPref(getApplicationContext()).getUID());
        Query query = databaseRefUserTree.orderByKey().equalTo(uid);
        Log.i(TAG, new SharedPref(getApplicationContext()).getUID());

        ArrayList<Tree> user_trees = new ArrayList<>();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_trees.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Tree tree = ds.getValue(Tree.class);
                    Log.i(TAG, ""+tree.getTree_id());
                }

                /*new Handler().post(new Runnable() {
                    @Override
                    public void run() {

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        RV_tree.setLayoutManager(mLayoutManager);

                        //Collections.reverse(FoodMixDB);

                        RecyclerView.Adapter mRecycleAdapter = new CustomAdapterRVTree(trees);
                        RV_tree.setAdapter(mRecycleAdapter);

                    }
                });*/

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


    public class CustomAdapterRVTree extends RecyclerView.Adapter<CustomAdapterRVTree.ViewHolder> {

        private ArrayList<Tree> trees;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
            private TextView TV_name, TV_water;
            ImageView TV_img;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                TV_name = (TextView) view.findViewById(R.id.TV_name);
                TV_water = (TextView) view.findViewById(R.id.TV_water);
                TV_img = (ImageView) view.findViewById(R.id.TV_img);

                view.setOnClickListener(this);
                view.setOnLongClickListener(this);

            }

            private ItemClickListener clickListener;

            public void setClickListener(ItemClickListener itemClickListener) {
                this.clickListener = itemClickListener;
            }

            @Override
            public void onClick(View view) {
                clickListener.onClick(view, getPosition(), false);

            }

            @Override
            public boolean onLongClick(View view) {
                clickListener.onClick(view, getPosition(), true);
                return true;
            }
        }

        public CustomAdapterRVTree(ArrayList<Tree> trees) {
            this.trees = trees;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_tree, viewGroup, false);

            return new CustomAdapterRVTree.ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            viewHolder.TV_name.setText(trees.get((position)).getTree_name());
            viewHolder.TV_water.setText(trees.get((position)).getWater_max());


            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, final int position, boolean isLongClick) {
                    if (isLongClick) {

                    } else {

                    }
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return trees.size();
        }
    }
}