package com.example.android.foodtracker.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.foodtracker.DetailedRecommendation;
import com.example.android.foodtracker.FoodRow;
import com.example.android.foodtracker.R;
import com.example.android.foodtracker.RecommendationsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecommendationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecommendationsFragment extends Fragment implements RecommendationsAdapter.onFoodListener, RecommendationsAdapter.FoodLikeListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private String mParam1;
    private List<FoodRow> recomendaciones;
    RecyclerView recommendations_list;
    FirebaseFirestore db;
    private boolean hasFavs;
    DocumentReference userfavorites;
    public SwipeRefreshLayout swr;
    Spinner drop, sort, fliterPrice;

    String categ, sortOption;
    public double lower_bound, upper_bound;

    public RecommendationsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecommendationsFragment.
     */
    public static RecommendationsFragment newInstance() {
        RecommendationsFragment fragment = new RecommendationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<String> getUsersFavs(){
        final ArrayList<String> userFavs = new ArrayList<String>();
        String uid = FirebaseAuth.getInstance().getUid();
        userfavorites = db.collection("favoritos").document(uid);
        userfavorites.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();

                    if((hasFavs=document.exists())){
                        userFavs.addAll((ArrayList<String>) document.get("favs"));
                    }
                    else{
                        Log.wtf("Document", "no exists");
                    }
                }
                else{
                    Log.wtf("Something", "failed");
                }
            }
        });
        return userFavs;
    }

    private void getData(){
        final ArrayList<String> userFavorites =getUsersFavs();
        recomendaciones = new ArrayList<>();
        db.collection("recomendaciones")
                .orderBy(sortOption.toUpperCase(), Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                String name = document.getData().get("NAME").toString();
                                String description = document.getData().get("DESCRIPTION").toString();
                                float price = Float.parseFloat(document.getData().get("PRICE").toString());
                                if(price> upper_bound || price < lower_bound){
                                    continue;
                                }
                                String image_string = document.getData().get("IMAGE").toString();
                                byte[] imagedata = null;
                                try{
                                    imagedata= Base64.decode(image_string,Base64.DEFAULT);
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                recomendaciones.add(new FoodRow(document.getId(),name,description,price,imagedata));
                            }
                            RecommendationsAdapter adapter = new RecommendationsAdapter(recomendaciones, RecommendationsFragment.this, userFavorites, RecommendationsFragment.this);
                            recommendations_list.setAdapter(adapter);
                            swr.setRefreshing(false);
                        }
                        else{
                            Log.w("Error from:", task.getException());
                        }
                    }
                });
    }

    private void getCategoryFood(String category){
        final ArrayList<String> userFavorites =getUsersFavs();
        recomendaciones = new ArrayList<>();
        db.collection("recomendaciones")
                .orderBy(sortOption.toUpperCase(), Query.Direction.DESCENDING)
                .whereEqualTo("categoria", category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                String name = document.getData().get("NAME").toString();
                                String description = document.getData().get("DESCRIPTION").toString();
                                float price = Float.parseFloat(document.getData().get("PRICE").toString());
                                if(price> upper_bound || price < lower_bound){
                                    continue;
                                }
                                String image_string = document.getData().get("IMAGE").toString();
                                byte[] imagedata = null;
                                try{
                                    imagedata= Base64.decode(image_string,Base64.DEFAULT);
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                recomendaciones.add(new FoodRow(document.getId(),name,description,price,imagedata));
                            }
                            RecommendationsAdapter adapter = new RecommendationsAdapter(recomendaciones, RecommendationsFragment.this, userFavorites, RecommendationsFragment.this);
                            recommendations_list.setAdapter(adapter);
                            swr.setRefreshing(false);
                        }
                        else{
                            Log.w("Error from:", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        db = FirebaseFirestore.getInstance();
        sortOption = "Price";
        categ="Todo";
        this.lower_bound = Double.NEGATIVE_INFINITY;
        this.upper_bound = Double.POSITIVE_INFINITY;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_recommendations,container, false);
        recommendations_list = root.findViewById(R.id.recommendations_list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recommendations_list.setLayoutManager(llm);
        recommendations_list.setAdapter(new RecommendationsAdapter(new ArrayList<FoodRow>(), this, new ArrayList<String>(), this ));
        swr = root.findViewById(R.id.recommendations_swipe_layout);
        swr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(categ.compareTo("Todo")==0){
                    getData();
                }
                else{
                    getCategoryFood(categ);
                }
            }
        });
        drop = root.findViewById(R.id.spinner);
        final String[] items = new String[]{"Todo", "Mexicana", "Vegana", "Internacional", "Otro"};
        ArrayAdapter<String>  ad= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1 ,items);
        drop.setAdapter(ad);
        drop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categ = items[position];
                if(position==0){
                    getData();
                }
                else{
                    Log.wtf("Log", categ);
                    getCategoryFood(categ);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sort = root.findViewById(R.id.sortspinner);
        final String[] sortOptions = new String[]{"Price", "Likes"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, sortOptions);
        sort.setAdapter(spinnerAdapter);
        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortOption = sortOptions[position];
                if(categ.compareTo("Todo")==0){
                    getData();
                }
                else{
                    getCategoryFood(categ);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fliterPrice = root.findViewById(R.id.filterPrice);
        final String[] filterOptions = new String[]{"No filter", "0-100" , "101-200", "201-300","300+"};
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, filterOptions);
        fliterPrice.setAdapter(filterAdapter);
        fliterPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        lower_bound = Double.NEGATIVE_INFINITY;
                        upper_bound = Double.POSITIVE_INFINITY;
                        break;
                    case 1:
                        lower_bound = 0;
                        upper_bound = 100;
                        break;
                    case 2:
                        lower_bound = 100;
                        upper_bound = 200;
                        break;
                    case 3:
                        lower_bound = 200;
                        upper_bound = 300;
                        break;
                    case 4:
                        lower_bound = 300;
                        upper_bound = Double.POSITIVE_INFINITY;
                        break;
                }
                if(categ.compareTo("Todo")==0){
                    getData();
                }
                else{
                    getCategoryFood(categ);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }

    @Override
    public void onFoodClick(int position) {
        Intent seeRecord = new Intent(getActivity(), DetailedRecommendation.class);
        FoodRow current = recomendaciones.get(position);
        seeRecord.putExtra("name", current.getName());
        seeRecord.putExtra("description", current.getDescription());
        seeRecord.putExtra("price",current.getPrice());
        seeRecord.putExtra("img",current.getImg());
        startActivity(seeRecord);
    }

    @Override
    public void onFoodLike(int position, boolean value) {
        String foodId = recomendaciones.get(position).getId();
        if(value){
            if(hasFavs){
                userfavorites.update("favs", FieldValue.arrayUnion(foodId));
            }
            else{
                Map<String,Object> data = new HashMap<>();
                data.put("favs",Arrays.asList(foodId));
                userfavorites.set(data);
                hasFavs=true;
            }
            db.collection("recomendaciones").document(foodId).update("LIKES", FieldValue.increment(1));
        }
        else{
            if(hasFavs){
                userfavorites.update("favs",FieldValue.arrayRemove(foodId));
                db.collection("recomendaciones").document(foodId).update("LIKES", FieldValue.increment(-1));
            }
        }
    }
}
