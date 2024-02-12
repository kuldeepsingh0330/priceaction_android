package com.ransankul.priceaction.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.priceaction.R;
import com.ransankul.priceaction.activity.MainActivity;
import com.ransankul.priceaction.activity.SearchInstrumentKeyActivity;
import com.ransankul.priceaction.adapter.SearchInstrumentKeyAdapter;
import com.ransankul.priceaction.databinding.FragmentApiBinding;
import com.ransankul.priceaction.databinding.FragmentSequenceGeneratorBinding;
import com.ransankul.priceaction.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SequenceGeneratorFragment extends Fragment {

    private FragmentSequenceGeneratorBinding binding;

    String product, validity, instrument_token, order_type, transaction_type;
    int quantity, disclosed_quantity;
    float price, trigger_price,target_price;
    boolean is_amo,issimpleTrade;

    private List<String> platformlist;
    private String platform;
    private long dropdownid = 0L;

    public SequenceGeneratorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentSequenceGeneratorBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.scrollview.setVisibility(View.GONE);
        binding.progressbar.setVisibility(View.VISIBLE);
        platformlist = new ArrayList<>();
        loadAllPlatform();

        binding.platformDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                platform = (String) adapterView.getSelectedItem();
                dropdownid = adapterView.getSelectedItemId();
                if(dropdownid != 0){
                    binding.llinstrumentkey.setVisibility(View.VISIBLE);
                    binding.etinstrumentkey.requestFocus();
                    InputMethodManager lmm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    lmm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        binding.tvsearchinstrumentkey.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchInstrumentKeyActivity.class);
            intent.putExtra("platform",platform);
            startActivity(intent);
        });

        binding.etinstrumentkey.setOnEditorActionListener((v, actionId, event) -> {
            if(binding.etinstrumentkey.getText().toString().equals("")){
                Toast.makeText(getContext(), "Enter Instrument Key", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO)  {
                binding.llquantity.setVisibility(View.VISIBLE);
                binding.etquantity.requestFocus();
                binding.etinstrumentkey.setEnabled(false);
                instrument_token = binding.etinstrumentkey.getText().toString();
                return true;
            }
            return false;
        });


        binding.etquantity.setOnEditorActionListener((v, actionId, event) -> {
            if(binding.etquantity.getText().toString().equals("")){
                Toast.makeText(getContext(), "Enter Quantity", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT)  {
                binding.llproduct.setVisibility(View.VISIBLE);
                quantity = Integer.parseInt(binding.etquantity.getText().toString());
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                binding.etquantity.setEnabled(false);
                return true;
            }
            return false;
        });

        binding.rgproduct.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton rb = view.findViewById(i);
            product = rb.getText().toString();
            binding.llvalidity.setVisibility(View.VISIBLE);
            binding.rgproduct.setEnabled(false);

            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                radioGroup.getChildAt(j).setEnabled(false);
            }
        });

        binding.rgvalidity.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton rb = view.findViewById(i);
            validity = rb.getText().toString();
            binding.llordertype.setVisibility(View.VISIBLE);
            binding.rgvalidity.setEnabled(false);

            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                radioGroup.getChildAt(j).setEnabled(false);
            }
        });

        binding.rgordertype.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton rb = view.findViewById(i);
            order_type = rb.getText().toString();


            if(order_type.equals("MARKET")){
                binding.lldisclosequantity.setVisibility(View.VISIBLE);
            } else if (order_type.equals("LIMIT")) {
                binding.llprice.setVisibility(View.VISIBLE);
            } else {
                binding.lltriggerprice.setVisibility(View.VISIBLE);
            }

            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                radioGroup.getChildAt(j).setEnabled(false);
            }
        });

        binding.ettriggerprice.setOnEditorActionListener((v, actionId, keyEvent) -> {
            if(binding.ettriggerprice.getText().toString().equals("")){
                Toast.makeText(getContext(), "Enter Quantity", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT)  {
                trigger_price = Float.parseFloat(binding.ettriggerprice.getText().toString());
                if(order_type.equals("SL-M")){
                    binding.lldisclosequantity.setVisibility(View.VISIBLE);
                    return true;
                }
                binding.llprice.setVisibility(View.VISIBLE);
                binding.etprice.requestFocus();
                binding.ettriggerprice.setEnabled(false);
                return true;
            }

            return false;
        });

        binding.etprice.setOnEditorActionListener((v, actionId, keyEvent) -> {
            if(binding.etprice.getText().toString().equals("")){
                Toast.makeText(getContext(), "Enter Quantity", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT)  {
                price = Float.parseFloat(binding.etprice.getText().toString());
                binding.lldisclosequantity.setVisibility(View.VISIBLE);
                binding.etdisclosequantity.setText("0");
                binding.etdisclosequantity.requestFocus();
                binding.etprice.setEnabled(false);
                return true;
            }

            return false;
        });

        binding.etdisclosequantity.setOnEditorActionListener((v, actionId, keyEvent) -> {
            if(binding.etdisclosequantity.getText().toString().equals("")){
                Toast.makeText(getContext(), "Enter Quantity", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT)  {
                disclosed_quantity = Integer.parseInt(binding.etdisclosequantity.getText().toString());
                binding.lltransactiontype.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                binding.etdisclosequantity.setEnabled(false);
                return true;
            }

            return false;
        });


        binding.rgtransactiontype.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton rb = view.findViewById(i);
            transaction_type = rb.getText().toString();
            binding.llisamo.setVisibility(View.VISIBLE);
            binding.rgtransactiontype.setEnabled(false);

            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                radioGroup.getChildAt(j).setEnabled(false);
            }
        });

        binding.rgisamo.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton rb = view.findViewById(i);
            is_amo = Boolean.parseBoolean(rb.getText().toString());
            binding.llissimpletrade.setVisibility(View.VISIBLE);
            binding.rgtransactiontype.setEnabled(false);

            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                radioGroup.getChildAt(j).setEnabled(false);
            }
        });

        binding.rgissimpletrade.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton rb = view.findViewById(i);
            String tradetype = rb.getText().toString();
            if(tradetype.equals("SIMPLE TRADE")){
                issimpleTrade = true;
                binding.lltraget.setVisibility(View.VISIBLE);}
            else{ issimpleTrade = false;binding.llgeneratebtn.setVisibility(View.VISIBLE);}
            binding.rgtransactiontype.setEnabled(false);


            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                radioGroup.getChildAt(j).setEnabled(false);
            }
        });

        binding.ettarget.setOnEditorActionListener((v, actionId, keyEvent) -> {
            if(binding.ettarget.getText().toString().equals("")){
                Toast.makeText(getContext(), "Enter Target Price", Toast.LENGTH_SHORT).show();
                return false;
            }
            target_price = Float.parseFloat(binding.ettarget.getText().toString());

            if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT)  {
            binding.llgeneratebtn.setVisibility(View.VISIBLE);
            binding.rgisamo.setEnabled(false);
                return true;
            }

            return false;
        });

        binding.generatebtn.setOnClickListener(v -> {
            String sequence = generatesequenceString();
            if(!sequence.equals("")) showSequencePopUp(sequence);
        });

        return view;
    }

    private void loadAllPlatform() {
        String tokenValue = Constants.getTokenValue(requireContext());
        String url = Constants.USERAPIMAPPING_LOADALLPLATFORM_URL;
        platformlist.clear();

        StringRequest request = new StringRequest(Request.Method.GET,url,
                response -> {
                    try {
                        JSONArray responseObj = new JSONArray(response);

                        for (int i = 0; i < responseObj.length(); i++) {
                            JSONObject jsonObject = (JSONObject) responseObj.get(i);
                            platformlist.add(jsonObject.getString("platform"));
                        }
                        setPlatformDropdown();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                },error -> {

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + tokenValue);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }

    private void setPlatformDropdown() {
        binding.scrollview.setVisibility(View.VISIBLE);
        binding.progressbar.setVisibility(View.GONE);
        platformlist.add(0,"Select platform");
        if(getContext() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, platformlist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.platformDropdown.setAdapter(adapter);
        }
    }

    private void showSequencePopUp(String sequence) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(sequence);

        builder.setPositiveButton("Copy", (dialog, which) -> {
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Sequence", sequence);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(getContext(), "Copied Successfully", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String generatesequenceString(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("platform",platform);
            jsonObject.put("quantity", quantity);
            jsonObject.put("product", product);
            jsonObject.put("validity", validity);
            jsonObject.put("price", price);
            jsonObject.put("instrument_token", instrument_token);
            jsonObject.put("order_type", order_type);
            jsonObject.put("transaction_type", transaction_type);
            jsonObject.put("disclosed_quantity", disclosed_quantity);
            jsonObject.put("trigger_price", trigger_price);
            jsonObject.put("is_amo", is_amo);
            jsonObject.put("target_price", target_price);
            jsonObject.put("simpletradeis", issimpleTrade);

            return jsonObject.toString();

        } catch (JSONException e) {
            Toast.makeText(getContext(), "Something went wrong try again", Toast.LENGTH_SHORT).show();
            return "";
        }

    }

}