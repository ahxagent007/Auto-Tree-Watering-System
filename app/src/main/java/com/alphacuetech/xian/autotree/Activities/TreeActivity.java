package com.alphacuetech.xian.autotree.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alphacuetech.xian.autotree.ItemClickListener;
import com.alphacuetech.xian.autotree.Models.DailyWeatherData;
import com.alphacuetech.xian.autotree.Models.Tree;
import com.alphacuetech.xian.autotree.R;
import com.alphacuetech.xian.autotree.functions.SharedPref;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TreeActivity extends AppCompatActivity {

    String TAG = "XIAN";
    String uid;

    DatabaseReference databaseRefTree, databaseRefUserTree;
    RecyclerView RV_tree;
    FloatingActionButton FBTN_add;

    ArrayList<Tree> trees = new ArrayList<>();
    DailyWeatherData daily;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);

        RV_tree = findViewById(R.id.RV_tree);
        FBTN_add = findViewById(R.id.FBTN_add);

        daily = (DailyWeatherData) getIntent().getSerializableExtra("DATA");

        Log.i(TAG, daily.toString());

        databaseRefTree = FirebaseDatabase.getInstance().getReference("TREE");
        databaseRefUserTree = FirebaseDatabase.getInstance().getReference("USER");

        getTreesFirebase();


        FBTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTree();
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
                    //Log.i(TAG, ds.toString());
                }

                Log.i(TAG, "ALL TREE SIZE == "+trees.size());

                uid = new SharedPref(getApplicationContext()).getUID();
                
                getUserTreesFirebase(uid);

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
        Query query = databaseRefUserTree.child(uid);

        ArrayList<Tree> user_trees = new ArrayList<>();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_trees.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Tree tree = ds.getValue(Tree.class);
                    user_trees.add(tree);
                }

                ArrayList<Tree> RVTrees = new ArrayList<>();

                for (int i=0; i <user_trees.size(); i++){
                    Tree t = user_trees.get(i);

                    for (Tree tree : trees){
                        if (tree.getTree_id() == t.getTree_id()){
                            RVTrees.add(tree);
                        }
                    }
                }

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        RV_tree.setLayoutManager(mLayoutManager);

                        //Collections.reverse(FoodMixDB);

                        RecyclerView.Adapter mRecycleAdapter = new CustomAdapterRVTree(RVTrees);
                        RV_tree.setAdapter(mRecycleAdapter);

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

    public class CustomAdapterRVTree extends RecyclerView.Adapter<CustomAdapterRVTree.ViewHolder> {

        private ArrayList<Tree> trees;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
            private TextView TV_name, TV_water;
            ImageView IV_img;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                TV_name = (TextView) view.findViewById(R.id.TV_name);
                TV_water = (TextView) view.findViewById(R.id.TV_water);
                IV_img = (ImageView) view.findViewById(R.id.IV_img);

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

            DecimalFormat df = new DecimalFormat("0.00");

            double w_min = (trees.get(position).getWater_min()/trees.get(position).getTemp_min())*daily.getTemp().getMin();
            double w_max = (trees.get(position).getWater_max()/trees.get(position).getTemp_max())*daily.getTemp().getMax();

            viewHolder.TV_water.setText(df.format(w_min)+"-"+df.format(w_max)+" ml");


            Glide.with(getApplicationContext()).load(trees.get(position).getImg()).into(viewHolder.IV_img);

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

    public void addTree(){
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(TreeActivity.this);
        View myView = getLayoutInflater().inflate(R.layout.dialog_select_tree, null);

        RecyclerView RV_add_tree;
        Button btn_done;

        RV_add_tree = myView.findViewById(R.id.RV_add_tree);
        btn_done = myView.findViewById(R.id.btn_done);


        myBuilder.setView(myView);
        final AlertDialog dialog = myBuilder.create();
        dialog.show();

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                RV_add_tree.setLayoutManager(mLayoutManager);

                RecyclerView.Adapter mRecycleAdapter = new CustomAdapterRVSelectTree(trees);
                RV_add_tree.setAdapter(mRecycleAdapter);

            }
        });


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }


    public class CustomAdapterRVSelectTree extends RecyclerView.Adapter<CustomAdapterRVSelectTree.ViewHolder> {

        private ArrayList<Tree> trees;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
            private TextView TV_name, TV_water;
            ImageView IV_img;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                TV_name = (TextView) view.findViewById(R.id.TV_name);
                TV_water = (TextView) view.findViewById(R.id.TV_water);
                IV_img = (ImageView) view.findViewById(R.id.IV_img);

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

        public CustomAdapterRVSelectTree(ArrayList<Tree> trees) {
            this.trees = trees;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_tree, viewGroup, false);

            return new CustomAdapterRVSelectTree.ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            viewHolder.TV_name.setText(trees.get((position)).getTree_name());

            DecimalFormat df = new DecimalFormat("0.00");

            double w_min = (trees.get(position).getWater_min()/trees.get(position).getTemp_min())*daily.getTemp().getMin();
            double w_max = (trees.get(position).getWater_max()/trees.get(position).getTemp_max())*daily.getTemp().getMax();

            //viewHolder.TV_water.setText(df.format(w_min)+"-"+df.format(w_max)+" ml");

            Glide.with(getApplicationContext())
                    .load(trees.get(position).getImg())
                    .apply(new RequestOptions().override(60, 80))
                    .into(viewHolder.IV_img);


            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, final int position, boolean isLongClick) {
                    if (isLongClick) {

                    } else {
                        viewHolder.TV_name.setBackgroundColor(getResources().getColor(R.color.design_default_color_secondary_variant));
                        addTreeToFirebase(trees.get(position).getTree_id(), uid);
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

    public void addTreeToFirebase(int id, String uid){
        databaseRefUserTree.child(uid).child(""+id).child("tree_id").setValue(id);
        Toast.makeText(getApplicationContext(), "Tree added", Toast.LENGTH_LONG).show();
    }
}