package com.swachtaapp.ui.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.swachtaapp.MainActivity;
import com.swachtaapp.R;
import com.swachtaapp.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private RecyclerView mList;
    private List<HomeViewModel> Lists;
    private RecyclerView.Adapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Lists = new ArrayList<>();
        adapter = new HomeAdapter(getActivity(), Lists);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        mList= (RecyclerView)root.findViewById(R.id.recyclerView);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList.setAdapter(adapter);

        displayData();
        return root;
    }

    public void displayData() {
        //final HomeViewModel[] homeViewModels =new HomeViewModel[]{};


        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            //if no error in response
                            if (obj.getBoolean("status")) {

                                Log.e(URLs.URL_GET, response.toString());
                                //getting the user from the response
                                progressDialog.dismiss();
                                //Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                JSONArray array = obj.getJSONArray("data");

                                if (array != null) {
                                    for (int j = 0; j <=array.length() - 1; j++) {
                                        JSONObject innerElem = array.getJSONObject(j);

                                        Log.e("testData", innerElem.getString("binNo"));
                                        if (innerElem != null) {
                                            HomeViewModel Std = new HomeViewModel();
                                            Std.setBinNumber(innerElem.getString("binNo"));
                                            Std.setAddress(innerElem.getString("address"));
                                            Std.setStatus(innerElem.getString("status"));
                                            Std.setRemark(innerElem.getString("remark"));
                                            Std.setDate(innerElem.getString("createdOn"));
                                            Lists.add(Std);
                                            adapter.notifyDataSetChanged();


                                        }

                                    }
                                }


                            } else {
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Network Error ", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("bin", "");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}