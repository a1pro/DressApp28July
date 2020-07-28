package com.dressapplication.ui.discover;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.AllPost;
import com.dressapplication.Model.TrendData;
import com.dressapplication.Model.TrendingHashTag;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoverFragment extends Fragment {
    private DiscoverViewModel discoverViewModel;
    private RvDiscoverAdapter rvDiscoverAdapter;
    private RecyclerView rvDiscover;
    private ProgressBar progressBar;
    List<TrendData> list=new ArrayList<>();
    List<AllPost> list2=new ArrayList<>();
    private EditText et_search;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_discover, container, false);
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        initUI(root);
        getAlldataApi();

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        return root;
    }

    private void filter(String text) {
        ArrayList<TrendData> filteredList = new ArrayList<>();
        for (TrendData item : list) {
            if (item.getHashtag().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        rvDiscoverAdapter.filterList(filteredList);
    }

    private void initUI(View root) {
        et_search=root.findViewById(R.id.et_search);
        progressBar=root.findViewById(R.id.progressBar);
        rvDiscover=root.findViewById(R.id.rv_discover);
    }


    private void getAlldataApi() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(getContext());
            apiInteface.getTrendHash().enqueue(new Callback<TrendingHashTag>() {
                @Override
                public void onResponse(Call<TrendingHashTag> call, Response<TrendingHashTag> response) {
                    if (response.isSuccessful()){
                        list.clear();
                        list2.clear();
                        progressBar.setVisibility(View.GONE);
                        TrendingHashTag data=response.body();
                        if (data!=null){
                            if (data.getCode().equalsIgnoreCase("201")){
                                //  Toast.makeText(getContext(), ""+data.getStatus(), Toast.LENGTH_SHORT).show();
                                list.addAll(data.getData());
                                for (int i=0;i<data.getData().size();i++){
                                    list2.addAll(data.getData().get(i).getAllPost());
                                }


                                if (list.size()>0){
                                    rvDiscoverAdapter=new RvDiscoverAdapter(getContext(),list);
                                    rvDiscover.setLayoutManager(new LinearLayoutManager(getContext()));
                                    rvDiscover.setAdapter(rvDiscoverAdapter);
                                }
                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), ""+data.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<TrendingHashTag> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("register failure",""+t.getMessage());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
