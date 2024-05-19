package com.example.eventfinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.List;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Spinner spinnerCategory;
    private EditText edtTxtLocation,edtTxtDistance;
    private AutoCompleteTextView edtTxtKeyword;
    private Switch chkAutodetect;
    private ArrayList<String> autoList = new ArrayList<>();
    private String lat=" ", lng=" ";
    private String[] segmentIds={"default","KZFzniwnSyZfZ7v7nJ","KZFzniwnSyZfZ7v7nE","KZFzniwnSyZfZ7v7na","KZFzniwnSyZfZ7v7nn","KZFzniwnSyZfZ7v7n1"};
    private Button searchButton,clearButton;

    private String[] categoryStr={"All","Music","Sports","Art & Theatre","Film","Miscellaneous"};

    private Boolean noLocationResults=false;

    private static final String eventSearchUrl = "https://event-app-8.wl.r.appspot.com/eventsearch?";

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search, container, false);
//        Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_blankFragment);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinnerCategory = view.findViewById(R.id.category);
        edtTxtKeyword = view.findViewById(R.id.keyword);
        edtTxtDistance = view.findViewById(R.id.distance);
        System.out.println(spinnerCategory);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(getContext(),R.array.categories,R.layout.spinner_text);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinnerCategory.setAdapter(adapter1);

        System.out.println(spinnerCategory.getSelectedItem().toString());
        edtTxtLocation = view.findViewById(R.id.location);
        chkAutodetect = view.findViewById(R.id.autodetect);
        Bundle resBundle=getArguments();
        if(resBundle!=null) {
            System.out.println(resBundle.getInt("category"));
            edtTxtKeyword.setText(resBundle.getString("keyword"));
            edtTxtDistance.setText(resBundle.getString("distance"));
            spinnerCategory.setSelection(resBundle.getInt("category"));
            chkAutodetect.setChecked(resBundle.getBoolean("autoDetect"));
            if(chkAutodetect.isChecked()){
                edtTxtLocation.setVisibility(GONE);
            }
            edtTxtLocation.setText(resBundle.getString("location"));
            lat=resBundle.getString("lat");
            lng=resBundle.getString("lng");
        }
        System.out.println(lat+" "+lng);
        chkAutodetect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked==true){
                    System.out.println("isTrue");
                    getIpInfo(new VolleyCallBack() {
                        @Override
                        public void onSuccess() {
                            edtTxtLocation.setVisibility(GONE);
                        }
                    });
                }
                else{
                    edtTxtLocation.setVisibility(VISIBLE);
                }
            }
        });
        edtTxtKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println("here in text change"+charSequence);
                RequestQueue autoCQueue;
                String autoC_Url = "https://event-app-8.wl.r.appspot.com/autocomplete?keyword=" + charSequence;
                System.out.println(autoC_Url);
                autoCQueue = Volley.newRequestQueue(getContext());
                autoCQueue.start();

                StringRequest autoCRequest = new StringRequest(Request.Method.GET, autoC_Url,
                        response -> {
                            try {
                                Log.d("AutoComplete json-------------->", response);
                                //Create a JSON object containing information from the API.
                                JSONObject myJsonObject = new JSONObject(response);
                                // Toast.makeText(MainActivity.this, "Got autocomplete", Toast.LENGTH_SHORT).show();
                                if(myJsonObject.has("_embedded")) {
                                    JSONArray events = myJsonObject.getJSONObject("_embedded").getJSONArray("events");
                                    autoList.clear();
                                    for (int k = 0; k < events.length(); k++) {
                                        JSONObject input = events.getJSONObject(k);
                                        System.out.println("events------------------------>" + input);
                                        autoList.add(input.getString("name"));
                                    }
//                                JSONArray terms = myJsonObject.getJSONArray("terms");
//                                for(int k=0;k<terms.length();k++){
//                                    JSONObject input = terms.getJSONObject(k);
//                                    System.out.println("terms------------------------>"+input);
//                                    autoList.add(input.getString("text"));
//                                }
//                                System.out.println(autoList);
//                                String[] autoArray = new String[autoList.size()];
//                                for(int l=0;l<autoList.size();l++){
//                                    autoArray[l] = autoList.get(l);
//                                    System.out.println(autoArray[l]);
//                                }

                                    ArrayAdapter<String> autocomplete = new ArrayAdapter<String>
                                            (getContext(), android.R.layout.select_dialog_item, autoList);
                                    //Getting the instance of AutoCompleteTextView
                                    //edtTxtKeyword.setThreshold(1);//will start working from first character
                                    edtTxtKeyword.setAdapter(autocomplete);
                                }


                                } catch (JSONException e) {
                                    System.out.println("Some Exception");
                                    e.printStackTrace();
                                }
                        },
                        volleyError -> System.out.println(volleyError.getMessage())
//                            Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show()
                );
                autoCQueue.add(autoCRequest);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println("here in after text change"+autoList);
                ArrayAdapter<String> autocomplete = new ArrayAdapter<String>
                        (getContext(), android.R.layout.select_dialog_item, autoList);
                //Getting the instance of AutoCompleteTextView
                //edtTxtKeyword.setThreshold(1);//will start working from first character
                edtTxtKeyword.setAdapter(autocomplete);
            };
        });

        searchButton = view.findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = edtTxtKeyword.getText().toString();
                String distance = edtTxtDistance.getText().toString();
                String category = segmentIds[spinnerCategory.getSelectedItemPosition()];
                int distanceValue=10;
                if(!distance.equals("")){
                    distanceValue = Integer.parseInt(distance);
                }
                String location = edtTxtLocation.getText().toString();
                Log.d("search", "keyWordValue is : " + keyword +
                        " category_chosen: " + category +
                        " distanceValue_int: " + distanceValue +
                        " distance_chosen: " + distance +
                        " location_chosen: " + location
                );
                if (keyword.trim().equals("") || (!chkAutodetect.isChecked() && location.trim().equals(""))) {
                    Snackbar snackbar = Snackbar.make(view, "Please fill all fields",Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
//                    snackbar.setActionTextColor(android.R.color.black);
                    snackbar.show();
                    return;
                }
                if(!chkAutodetect.isChecked()){
                    getLatLng(location,new VolleyCallBack() {
                                   @Override
                                   public void onSuccess() {
                                       // this is where you will call the geofire, here you have the response from the volley.
                                       String searchUrl = eventSearchUrl + "keyword=" + keyword + "&category=" + category + "&distance=" + distance + "&latitude=" + lat + "&longitude=" + lng;
                                       System.out.println(searchUrl);
                                       Bundle bundle=new Bundle();
                                       bundle.putString("eventSearchUrl",searchUrl);
                                       bundle.putBoolean("noLocResults",noLocationResults);
                                       bundle.putString("keyword",keyword);
                                       bundle.putString("distance",distance);
                                       bundle.putInt("category",spinnerCategory.getSelectedItemPosition());
                                       bundle.putBoolean("autoDetect",chkAutodetect.isChecked());
                                       bundle.putString("location",location);
                                       bundle.putString("lat",lat);
                                       bundle.putString("lng",lng);
                                       Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_resultsFragment,bundle);
                                   };
                    });
                }else{
                    String searchUrl = eventSearchUrl + "keyword=" + keyword + "&category=" + category + "&distance=" + distance + "&latitude=" + lat + "&longitude=" + lng;
                    Bundle bundle=new Bundle();
                    bundle.putString("eventSearchUrl",searchUrl);
                    bundle.putBoolean("noLocResults",noLocationResults);
                    bundle.putString("keyword",keyword);
                    bundle.putString("distance",distance);
                    bundle.putInt("category",spinnerCategory.getSelectedItemPosition());
                    bundle.putString("location","");
                    bundle.putBoolean("autoDetect",chkAutodetect.isChecked());
                    bundle.putString("lat",lat);
                    bundle.putString("lng",lng);
                    Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_resultsFragment,bundle);
                }
                Log.d("form check","success form check");
//                generate url
                // intent obj
//                Intent intent = new Intent(MainActivity.this, SearchResult.class);
//                // pack data
//                intent.putExtra("SearchURL",searchUrl );
//                intent.putExtra("isFavorite", "false");
//                // start activity
//                startActivity(intent);


            }
        });

        clearButton = view.findViewById(R.id.clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtTxtKeyword.setText("");
                edtTxtDistance.setText("10");
                edtTxtLocation.setText("");
                chkAutodetect.setChecked(false);
                spinnerCategory.setSelection(0);
            }
        });
//        Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_blankFragment);

    }
    private void getIpInfo(final VolleyCallBack callBack) {

        RequestQueue ipinfoQueue;
        String IpInfoUrl = "https://ipinfo.io/json?token=651f72ea2c7b3e";
        System.out.println(IpInfoUrl);
        ipinfoQueue = Volley.newRequestQueue(getContext());
        ipinfoQueue.start();

        StringRequest ipinfoRequest = new StringRequest(Request.Method.GET, IpInfoUrl,
                response -> {
                    try{
                        Log.d("Ipinfo json", response);
                        JSONObject myJsonObject = new JSONObject(response);
                        String loc = myJsonObject.getString("loc");
                        String[] location = loc.split(",");
                        lat = location[0];
                        lng = location[1];
                        callBack.onSuccess();
                        System.out.println("LatLng extracted---------------->" + lat + lng);

                    } catch (JSONException e) {
                        System.out.println("Some Exception");
                        e.printStackTrace();
                    }
                },
                volleyError -> System.out.println(volleyError.getMessage())
//                            Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show()
        );

        ipinfoQueue.add(ipinfoRequest);
    }

    private void getLatLng(String location,final VolleyCallBack callBack) {
        String ApiKey="";
        try {
            ApplicationInfo app = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;
            ApiKey=bundle.getString("com.google.android.geo.API_KEY");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        RequestQueue geoCodeQueue;
        String geocodeUrl = "https://maps.googleapis.com/maps/api/geocode/json?address="+ location + "&key=" + ApiKey;
        System.out.println(geocodeUrl);
        geoCodeQueue = Volley.newRequestQueue(getContext());
        geoCodeQueue.start();

        JsonObjectRequest geocodeRequest = new JsonObjectRequest(geocodeUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    if (results.length() > 0) {
                        lat = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
                        lng = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");
                        callBack.onSuccess();
                    } else {
                        noLocationResults = true;
                        return;
                    }

                    System.out.println("LatLng extracted---------------->" + lat + lng);

                } catch (JSONException e) {
                    System.out.println("Some Exception");
                    e.printStackTrace();
                }
                //JSONArray array = new JSONArray(response.body().string());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
        geoCodeQueue.add(geocodeRequest);
    }
}