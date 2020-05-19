package com.example.android.foodtracker.ui.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.android.foodtracker.FoodRow;
import com.example.android.foodtracker.R;
import com.example.android.foodtracker.RVAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecommendationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecommendationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private List<FoodRow> recomendaciones;
    RecyclerView recommendations_list;
    FirebaseFirestore db;


    public RecommendationsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecommendationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecommendationsFragment newInstance(String username) {
        RecommendationsFragment fragment = new RecommendationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            Log.wtf("user:",mParam1);
        }
        db = FirebaseFirestore.getInstance();
        recomendaciones = new ArrayList<FoodRow>();
        db.collection("recomendaciones")
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
                                byte[] imagedata = null;
                                try{
                                    imagedata= Base64.decode(image_string,Base64.DEFAULT);
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                recomendaciones.add(new FoodRow(document.getId(),name,description,price,imagedata));
                            }
                            RVAdapter adapter = new RVAdapter(recomendaciones);
                            recommendations_list.setAdapter(adapter);
                        }
                        else{
                            Log.w("Error:", task.getException());
                        }
                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recommendations,container, false);
        recommendations_list = root.findViewById(R.id.recommendations_list);
        Log.wtf("Error", ""+recomendaciones.size());
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recommendations_list.setLayoutManager(llm);
        return root;
    }
}
