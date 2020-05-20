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

import com.example.android.foodtracker.CreateFood;
import com.example.android.foodtracker.EditDailyFood;
import com.example.android.foodtracker.EditFoodActivity;
import com.example.android.foodtracker.FoodRow;
import com.example.android.foodtracker.MyRecommendationsAdapter;
import com.example.android.foodtracker.R;
import com.example.android.foodtracker.RecommendationsAdapter;
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
public class My_RecommendationsFragment extends Fragment implements MyRecommendationsAdapter.OnElementListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    private String mParam1;
    FloatingActionButton FAB;
    private List<FoodRow> mis_recomendaciones;
    RecyclerView lista_recomendaciones;
    FirebaseFirestore db;
    public SwipeRefreshLayout swr;
    TextView txt;

    public My_RecommendationsFragment() {
        // Required empty public constructor
    }


    private void getData(){
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
                            }
                            if(mis_recomendaciones.size() >0){
                                txt.setVisibility(View.GONE);
                            }
                            else{
                                txt.setVisibility(View.VISIBLE);
                            }

                            MyRecommendationsAdapter adapter = new MyRecommendationsAdapter(mis_recomendaciones, My_RecommendationsFragment.this);
                            lista_recomendaciones.setAdapter(adapter);
                            swr.setRefreshing(false);
                        }
                        else{
                            Log.wtf("Error",task.getException());
                        }
                    }
                });
    }

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

        this.getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my__recommendations,container, false);
        swr = root.findViewById(R.id.swipe_layout);
        swr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        txt = root.findViewById(R.id.Text_empty);
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
        startActivityForResult(new_recommendation,1);
    }

    @Override
    public void onElementClick(int position) {
        Intent editRecord = new Intent(getActivity(), EditFoodActivity.class);
        FoodRow current = mis_recomendaciones.get(position);
        editRecord.putExtra("id", current.getId());
        editRecord.putExtra("name", current.getName());
        editRecord.putExtra("description", current.getDescription());
        editRecord.putExtra("price", current.getPrice());
        editRecord.putExtra("img", current.getImg());

        startActivityForResult(editRecord,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

    }
}
