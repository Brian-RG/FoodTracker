package com.example.android.foodtracker.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.foodtracker.CreateFood;
import com.example.android.foodtracker.FoodRow;
import com.example.android.foodtracker.R;
import com.example.android.foodtracker.RVAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link My_RecommendationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class My_RecommendationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private String mParam1;
    FloatingActionButton FAB;
    private List<FoodRow> mis_recomendaciones;
    RecyclerView lista_recomendaciones;
    FirebaseFirestore db;

    public My_RecommendationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment My_RecommendationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static My_RecommendationsFragment newInstance(String param1) {
        My_RecommendationsFragment fragment = new My_RecommendationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        db = FirebaseFirestore.getInstance();
        mis_recomendaciones = new ArrayList<FoodRow>();
        db.collection("recomendaciones")
                .whereEqualTo("USERID", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                String name = document.getData().get("NAME").toString();
                                String description = document.getData().get("DESCRIPTION").toString();
                                float price = Float.parseFloat(document.getData().get("PRICE").toString());
                                String image_string = document.getData().get("IMAGE").toString();
                                byte[] image_data = null;
                                try{
                                    image_data = Base64.decode(image_string, Base64.DEFAULT);
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                                mis_recomendaciones.add(new FoodRow(document.getId(),name,description,price,image_data));
                                Log.wtf("Logger: ", name);
                            }
                            RVAdapter adapter = new RVAdapter(mis_recomendaciones);
                            lista_recomendaciones.setAdapter(adapter);
                        }
                        else{
                            Log.wtf("Error",task.getException());
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my__recommendations,container, false);

        FAB = root.findViewById(R.id.recommendation_fab);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecommendation(v);
            }
        });
        lista_recomendaciones = root.findViewById(R.id.MyRecommendations);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        lista_recomendaciones.setLayoutManager(llm);

        return root;
    }

    public void addRecommendation(View v){
        Intent new_recommendation = new Intent(getActivity(), CreateFood.class);
        startActivity(new_recommendation);
    }
}
