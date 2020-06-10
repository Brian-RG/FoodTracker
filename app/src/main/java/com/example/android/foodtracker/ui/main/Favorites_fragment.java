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
import android.widget.TextView;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Favorites_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Favorites_fragment extends Fragment implements RecommendationsAdapter.onFoodListener, RecommendationsAdapter.FoodLikeListener{


    public SwipeRefreshLayout swipeRefreshLayout;
    TextView text_empty;
    RecyclerView lista_favoritos;
    FirebaseFirestore db;
    DocumentReference userfavorites;
    public int counter;
    private List<FoodRow> my_favorites;
    private boolean hasFavs;

    public Favorites_fragment() {
        // Required empty public constructor
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
                        text_empty.setVisibility(View.INVISIBLE);
                        my_favorites = new ArrayList<>();
                        counter = 0;
                        for(String ref : userFavs){
                            db.collection("recomendaciones").document(ref)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()){
                                                counter+=1;
                                                DocumentSnapshot doc = task.getResult();
                                                String name = doc.getData().get("NAME").toString();
                                                String description = doc.getData().get("DESCRIPTION").toString();
                                                float price = Float.parseFloat(doc.getData().get("PRICE").toString());
                                                String image_string = doc.getData().get("IMAGE").toString();
                                                byte[] imagedata = null;
                                                try{
                                                    imagedata= Base64.decode(image_string,Base64.DEFAULT);
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                                my_favorites.add(new FoodRow(doc.getId(),name,description,price,imagedata));
                                                if(counter == userFavs.size()){
                                                    RecommendationsAdapter radapter = new RecommendationsAdapter(my_favorites, Favorites_fragment.this, userFavs, Favorites_fragment.this);
                                                    lista_favoritos.setAdapter(radapter);
                                                    swipeRefreshLayout.setRefreshing(false);
                                                }

                                            }
                                            else{
                                                Log.wtf("Status", "Query failed");
                                            }
                                        }
                                    });
                        }

                    }
                    else{
                        Log.wtf("Document", "no exists");
                        text_empty.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    Log.wtf("Something", "failed");
                }
            }
        });
        return userFavs;
    }


    private void fetchData(){
        ArrayList<String> favorites_records = getUsersFavs();
    }



    public static Favorites_fragment newInstance() {
        Favorites_fragment fragment = new Favorites_fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_favorites_fragment, container, false);
        swipeRefreshLayout = root.findViewById(R.id.favorite_swipe_layout);
        text_empty = root.findViewById(R.id.Favorites_text_empty);
        lista_favoritos = root.findViewById(R.id.MyFavorites);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        lista_favoritos.setLayoutManager(llm);
        lista_favoritos.setAdapter(new RecommendationsAdapter(new ArrayList<FoodRow>(), this, new ArrayList<String>(), this));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        fetchData();
        return root;
    }

    @Override
    public void onFoodClick(int position) {
        Intent seeRecord = new Intent(getActivity(), DetailedRecommendation.class);
        FoodRow current = my_favorites.get(position);
        seeRecord.putExtra("name", current.getName());
        seeRecord.putExtra("description", current.getDescription());
        seeRecord.putExtra("price",current.getPrice());
        seeRecord.putExtra("img",current.getImg());
        startActivity(seeRecord);
    }

    @Override
    public void onFoodLike(int position, boolean value) {
        String foodId = my_favorites.get(position).getId();
        if(value){
            if(hasFavs){
                userfavorites.update("favs", FieldValue.arrayUnion(foodId));
            }
            else{
                Map<String,Object> data = new HashMap<>();
                data.put("favs", Arrays.asList(foodId));
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
