package com.swachtaapp.ui.gallery;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import com.android.volley.toolbox.Volley;
import com.swachtaapp.MainActivity;
import com.swachtaapp.R;
import com.swachtaapp.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GalleryFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private GalleryViewModel galleryViewModel;
    LocationManager locationManager;
    String latitude, longitude;
    private static final int REQUEST_LOCATION = 1;
    private Button getlocationBtn;
    private EditText editTextBin, editTextAddress, editTextStatus, editTextRemark;
    Geocoder geocoder;
    String item;
    List<Address> addresses;

    //private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        //Add permission

        ActivityCompat.requestPermissions(getActivity(), new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        getlocationBtn = root.findViewById(R.id.btnSubmit);
        //editTextAddress=root.findViewById(R.id.txtAdd);
        editTextBin = root.findViewById(R.id.txtBin);
        //editTextStatus=root.findViewById(R.id.txtStatus);
        editTextRemark = root.findViewById(R.id.txtRemark);
        Spinner spinner = (Spinner) root.findViewById(R.id.spnStatus);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.status_arrays, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        item = String.valueOf(spinner.getSelectedItem());
        getlocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

                //Check gps is enable or not

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //Write Function To enable gps

                    OnGPS();
                } else {
                    //GPS is already On then

                    try {
                        getLocation();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        /*final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    private void getLocation() throws IOException {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),

                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            geocoder = new Geocoder(getContext(), Locale.getDefault());


            if (LocationGps != null) {
                double lat = LocationGps.getLatitude();
                double longi = LocationGps.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                addresses = geocoder.getFromLocation(lat, longi, 1);

                Log.e("Lon:", longitude);
                Log.e("Lat:", latitude);
                saveBin(lat, longi, item);
            } else if (LocationNetwork != null) {
                double lat = LocationNetwork.getLatitude();
                double longi = LocationNetwork.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                addresses = geocoder.getFromLocation(lat, longi, 1);
                Log.e("Lon:", longitude);
                Log.e("Lat:", latitude);
                saveBin(lat, longi, item);
            } else if (LocationPassive != null) {
                double lat = LocationPassive.getLatitude();
                double longi = LocationPassive.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                Log.e("Lon:", longitude);
                Log.e("Lat:", latitude);
                saveBin(lat, longi, item);
            } else {
                Toast.makeText(getActivity(), "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

            //Thats All Run Your App
        }

    }

    private void OnGPS() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveBin(double lat, double longi, String item) throws IOException {

        final String bin = editTextBin.getText().toString();
        final String status = item;
        final String remark = editTextRemark.getText().toString();
        addresses = geocoder.getFromLocation(lat, longi, 1);
        String address = addresses.get(0).getAddressLine(0);
        Log.e("address:", address);
        //validating inputs

        if (TextUtils.isEmpty(bin)) {
            editTextBin.setError("Please enter your Bin number");
            editTextBin.requestFocus();
            return;
        }
       /* if (TextUtils.isEmpty(address)) {
            editTextAddress.setError("Please enter your Address");
            editTextAddress.requestFocus();
            return;
        }*/
      /*  if (TextUtils.isEmpty(status)) {
            editTextStatus.setError("Please enter your Bin Status");
            editTextStatus.requestFocus();
            return;
        }*/
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (obj.getBoolean("status")) {

                                Log.e(URLs.URL_ADD, response.toString());
                                //getting the user from the response
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                                startActivity(new Intent(getActivity(), MainActivity.class));
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
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String address = addresses.get(0).getAddressLine(0);
                Map<String, String> params = new HashMap<>();
                params.put("address", address);
                params.put("bin", bin);
                params.put("lat", latitude);
                params.put("lon", longitude);
                params.put("status", status);
                params.put("remark", remark);
                return params;
            }
        };
        Log.e("param", stringRequest.toString());
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}