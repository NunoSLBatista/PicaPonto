package pt.picaponto.app.Api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import pt.picaponto.app.ColaboradorPinActivity;
import pt.picaponto.app.Database.DatabaseHelper;
import pt.picaponto.app.Models.Colaborador;
import pt.picaponto.app.Models.Registo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.picaponto.app.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GetData extends AppCompatActivity {

    public static String dataRegisto, empresaNome, data, hora, tipoMovimento;
    public static Integer id, colID, empresaID, codigo, senha;

    private DatabaseHelper databaseHelper = null;
    ArrayList<Registo> list = new ArrayList<Registo>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colaborador_pin);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String version = prefs.getString("syncShared", null);

        JSONObject objMainList = new JSONObject();

        if(version != null){

            databaseHelper = new DatabaseHelper(this);
            list = databaseHelper.syncRecords();

            JSONArray arrPicagem = new JSONArray();

            for(int i = 0; i < list.size(); i++){
                arrPicagem.put(list.get(i).toJSON());
            }

            try {
                objMainList.put("token", "picapontoxpto");
                objMainList.put("picagens", arrPicagem);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("JSON", objMainList.toString());

            sendData(objMainList.toString());

            Intent goPinCode = new Intent(this, ColaboradorPinActivity.class);
            startActivity(goPinCode);
        } else {
            Toast.makeText(this, "Synching", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putString("syncShared", "1.0");
            editor.apply();
            loginUser();
        }



    }

    private void sendData(String json){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ColaboradorInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ColaboradorInterface api = retrofit.create(ColaboradorInterface.class);

        Call<String> call = api.sendData(json);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        databaseHelper.updateSync();


                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }

    private void loginUser() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ColaboradorInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ColaboradorInterface api = retrofit.create(ColaboradorInterface.class);

        Call<String> call = api.getData("picapontoxpto");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        parseLoginData(jsonresponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    public void parseLoginData(String response) {

        try {
            JSONObject jsonObject1 = new JSONObject(response);
            JSONObject jsonObject2 = new JSONObject(response);

            JSONArray picagensArray = jsonObject2.getJSONArray("picagens");
            JSONArray dataArray = jsonObject1.getJSONArray("admin_colaboradores");


            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject dataobj = dataArray.getJSONObject(i);

                databaseHelper = new DatabaseHelper(this);

                // Colaboradores
                id = dataobj.getInt("id");
                empresaID = dataobj.getInt("empresa_id");
                colID = dataobj.getInt("empresa_id");
                codigo = dataobj.getInt("codigo");
                senha = dataobj.getInt("senha");
                dataRegisto = dataobj.getString("data_registo");

                Colaborador newCol = new Colaborador(id, empresaID, colID, codigo, "", senha, dataRegisto);
                databaseHelper.addUser(newCol);

            }

            for (int i = 0; i < picagensArray.length(); i++) {

                JSONObject picagensObj = picagensArray.getJSONObject(i);

                databaseHelper = new DatabaseHelper(this);

                //Picagens
                colID = picagensObj.getInt("colaborador_id");
                tipoMovimento = picagensObj.getString("tipo_movimento");
                data = picagensObj.getString("data");
                hora = picagensObj.getString("hora");

                Registo newReg = new Registo(colID, tipoMovimento, data, hora);
                databaseHelper.addRegisto(newReg);

                Intent goPinCode = new Intent(this, ColaboradorPinActivity.class);
                startActivity(goPinCode);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
