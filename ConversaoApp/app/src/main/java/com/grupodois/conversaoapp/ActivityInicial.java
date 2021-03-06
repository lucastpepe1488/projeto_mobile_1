package com.grupodois.conversaoapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.grupodois.conversaoapp.R.layout.activity_inicial;

public class ActivityInicial extends AppCompatActivity {

    private Button btnOK;
    private RadioButton radioButton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_inicial);

        Button btnOK = (Button) findViewById(R.id.button);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupMoedas);
        requestQueue = Volley.newRequestQueue(this);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                final JSONObject object  = new  JSONObject();

                if (radioButton != null) {
                    String myUrl = getText(R.string.url_request) + radioButton.getTag().toString();
                    requisicaoHttp(myUrl);
                } else {
                    Toast.makeText(ActivityInicial.this, getText(R.string.escolherMoeda) , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void requisicaoHttp(final String url){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getText(R.string.carregando));
        pDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>( ) {


            @Override
            public void onResponse(JSONObject response) {

                JSONObject array = null;

                String base;

                try {
                    array = response.getJSONObject("rates");
                    base = response.getString("base");
                    setRates(array, base);
                    pDialog.hide();
                } catch (JSONException e) {
                    e.printStackTrace( );
                    pDialog.hide();
                }

            }
        }, new Response.ErrorListener( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    public void setRates(JSONObject jsonObject, String base) throws JSONException {
            try {
                Double brl = (base.equals("BRL")) ? 1.00 : Double.valueOf(jsonObject.getString("BRL"));
                Double usd = (base.equals("USD")) ? 1.00 : Double.valueOf(jsonObject.getString("USD"));
                Double eur = (base.equals("EUR")) ? 1.00 : Double.valueOf(jsonObject.getString("EUR"));
                Double gbp = (base.equals("GBP")) ? 1.00 : Double.valueOf(jsonObject.getString("GBP"));
                Double jpy = (base.equals("JPY")) ? 1.00 : Double.valueOf(jsonObject.getString("JPY"));


                ((CurrencyRates) this.getApplication()).setBase(base);
                ((CurrencyRates) this.getApplication()).setBRL(brl);
                ((CurrencyRates) this.getApplication()).setUSD(usd);
                ((CurrencyRates) this.getApplication()).setEUR(eur);
                ((CurrencyRates) this.getApplication()).setGBP(gbp);
                ((CurrencyRates) this.getApplication()).setJPY(jpy);

                startActivity(new Intent(ActivityInicial.this, TelaDois.class));
            }catch (JSONException e){
                e.printStackTrace();
            }

    }

}
