package pt.picaponto.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pt.picaponto.app.Api.ColaboradorInterface;
import pt.picaponto.app.Api.PicaPonto;
import pt.picaponto.app.Database.DatabaseHelper;
import pt.picaponto.app.Models.Colaborador;
import pt.picaponto.app.Models.Dia;
import pt.picaponto.app.Models.Ferias;
import pt.picaponto.app.Models.Periodo;
import pt.picaponto.app.Models.Registo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class EmpresaActivity extends AppCompatActivity  {

    public static final String MY_PREFS_NAME = "PicaPontoPrefs";
    SharedPreferences sharedPreferences;

    public static String dataRegisto, empresaNome, data, hora, tipoMovimento, nome, periodo_inicio, periodo_fim, observacoes, estado;
    public static Integer id, colID, empresaID, codigo, senha, colaboradorID, id2, picagemManual, feriasID;

    private DatabaseHelper databaseHelper = null;
    ArrayList<Registo> list = new ArrayList<Registo>();
    ArrayList<Ferias> listFerias = new ArrayList<Ferias>();

    EditText codigoEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        if(sharedPreferences.contains("codigo-empresa")) {

            if (isNetworkAvailable()) {
                syncRecords();
                updateData();
            }

            if(sharedPreferences.contains("user")){
                Intent go = new Intent(this, MainActivity.class);
                startActivity(go);
            } else {
                Intent go = new Intent(this, ColaboradorPinActivity.class);
                startActivity(go);
            }

        }

        codigoEdit = findViewById(R.id.codigoEmpresa);

    }

    public void updateData(){

        DatabaseHelper dh = new DatabaseHelper(PicaPonto.getContext());

        dh.updateData();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ColaboradorInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ColaboradorInterface api = retrofit.create(ColaboradorInterface.class);

        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Call<String> call = api.getData(sharedPreferences.getString("codigo-empresa", ""));

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();

                        try {

                            JSONObject jsonObject1 = new JSONObject(jsonresponse);

                            String check = jsonObject1.optString("status");

                            if(check == "false"){
                            } else {
                                Log.d("Updated", "data");
                                parseLoginData(jsonresponse);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


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

    public void syncRecords(){
        JSONObject objMainList = new JSONObject();


        databaseHelper = new DatabaseHelper(PicaPonto.getContext());
        list = databaseHelper.syncRecords();

        SharedPreferences sharedPreferences = PicaPonto.getContext().getSharedPreferences("PicaPontoPrefs", MODE_PRIVATE);

        JSONArray arrPicagem = new JSONArray();

        if (list.size() > 0) {

            for (int i = 0; i < list.size(); i++) {
                arrPicagem.put(list.get(i).toJSON());
            }

            try {
                objMainList.put("token", sharedPreferences.getString("codigo-empresa", ""));
                objMainList.put("picagens", arrPicagem);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            sendDataPicagem(objMainList.toString());

        }
    }

    public void syncVacations(){

        JSONObject objMainList = new JSONObject();

        databaseHelper = new DatabaseHelper(PicaPonto.getContext());
        listFerias = databaseHelper.syncVacations();

        SharedPreferences sharedPreferences = PicaPonto.getContext().getSharedPreferences("PicaPontoPrefs", MODE_PRIVATE);

        JSONArray arrFerias = new JSONArray();

        Log.i("Numero Ferias", String.valueOf(listFerias.size()));

        if(listFerias.size() > 0){

            for (int i = 0; i < listFerias.size(); i++) {
                arrFerias.put(listFerias.get(i).toJSON());
            }

            try {
                objMainList.put("token", sharedPreferences.getString("codigo-empresa", ""));
                objMainList.put("ferias", arrFerias);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            sendDataVacation(objMainList.toString());

        }
    }

    public void sendVacation(Ferias ferias){

        JSONObject objMainList = new JSONObject();

        SharedPreferences sharedPreferences = PicaPonto.getContext().getSharedPreferences("PicaPontoPrefs", MODE_PRIVATE);
        JSONArray arrFerias = new JSONArray();

        ArrayList<Ferias> feriasList = new ArrayList<>();

        feriasList.add(ferias);

        if (feriasList.size() > 0) {

            for (int i = 0; i < feriasList.size(); i++) {
                arrFerias.put(feriasList.get(i).toJSON());
            }

            try {
                objMainList.put("token", sharedPreferences.getString("codigo-empresa", ""));
                objMainList.put("ferias", arrFerias);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            sendData(objMainList.toString());

        }
    }




    public void submitCode(View view){

        if(isNetworkAvailable()){
            loginUser();
        } else {
            Toast.makeText(this, "Ligue a internet para podermos fazer download dos dados da empresa", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void sendPicagem(Registo registo){

            JSONObject objMainList = new JSONObject();

            databaseHelper = new DatabaseHelper(this);

            String MY_PREFS_NAME = "PicaPontoPrefs";

            SharedPreferences sharedPreferences;
            sharedPreferences = PicaPonto.getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

            JSONArray arrPicagem = new JSONArray();

            list = new ArrayList<Registo>();
            list.add(registo);

            if (list.size() > 0) {

                for (int i = 0; i < list.size(); i++) {
                    arrPicagem.put(list.get(i).toJSON());
                }

                try {
                    objMainList.put("token", sharedPreferences.getString("codigo-empresa", ""));
                    objMainList.put("picagens", arrPicagem);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                sendData(objMainList.toString());

            }

    }

    private void sendData(String json){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ColaboradorInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ColaboradorInterface api = retrofit.create(ColaboradorInterface.class);
        Log.d("Json", json);

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


    private void sendDataPicagem(String json){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ColaboradorInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ColaboradorInterface api = retrofit.create(ColaboradorInterface.class);
        Log.d("Json", json);

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


                        DatabaseHelper databaseHelper = null;
                        databaseHelper = new DatabaseHelper(PicaPonto.getContext());

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

    private void sendDataVacation(String json){

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


                        DatabaseHelper databaseHelper = null;
                        databaseHelper = new DatabaseHelper(PicaPonto.getContext());

                        databaseHelper.updateSyncVacations();


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


    public void getHorario(){

        SharedPreferences sharedPreferences = PicaPonto.getContext().getSharedPreferences("PicaPontoPrefs", MODE_PRIVATE);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl((ColaboradorInterface.JSONURL))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ColaboradorInterface api = retrofit.create(ColaboradorInterface.class);

        Call<String> call = api.getHorario(sharedPreferences.getString("codigo-empresa", ""), sharedPreferences.getString("user", ""));

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();

                        try {

                            JSONObject jsonObject1 = new JSONObject(jsonresponse);
                            JSONArray horarioArray = jsonObject1.getJSONArray("horario");

                            for (int i = 0; i < horarioArray.length(); i++) {

                                JSONObject dataobj = horarioArray.getJSONObject(i);
                                JSONArray periodosArray = dataobj.getJSONArray("periodos");

                                databaseHelper = new DatabaseHelper(PicaPonto.getContext());

                                // Colaboradores
                                Integer id = dataobj.getInt("periodo_id");
                                String dia = dataobj.getString("dia");
                                String tipo = dataobj.getString("periodo_nome");

                                ArrayList<Periodo> periodos = new ArrayList<Periodo>();

                                for (int j = 0; j < periodosArray.length(); j++) {

                                    JSONObject dataobj2 = periodosArray.getJSONObject(j);

                                    databaseHelper = new DatabaseHelper(PicaPonto.getContext());

                                    String hora_inicio = dataobj2.getString("trabalho_hora_inicio");
                                    String hora_fim = dataobj2.getString("trabalho_hora_fim");

                                    periodos.add(new Periodo(hora_inicio, hora_fim));

                                }

                                Dia newDia = new Dia(id, tipo, dia, periodos);
                                databaseHelper.addDia(newDia);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


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

        Call<String> call = api.getData(codigoEdit.getText().toString());


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();

                        try {

                            JSONObject jsonObject1 = new JSONObject(jsonresponse);

                            String check = jsonObject1.optString("status");

                            if(check == "false"){
                                Toast.makeText(getApplicationContext(), "Erro no codigo", Toast.LENGTH_LONG).show();
                            } else {
                                sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("codigo-empresa", codigoEdit.getText().toString());
                                editor.apply();
                                parseLoginData(jsonresponse);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


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

            JSONArray picagensArray = jsonObject1.getJSONArray("picagens");
            JSONArray dataArray = jsonObject1.getJSONArray("admin_colaboradores");
            JSONArray colabsArray = jsonObject1.getJSONArray("colaboradores");
            JSONArray feriasArray = jsonObject1.getJSONArray("ferias");


            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject dataobj = dataArray.getJSONObject(i);

                databaseHelper = new DatabaseHelper(PicaPonto.getContext());

                // Colaboradores
                id = dataobj.getInt("id");
                colaboradorID = dataobj.getInt("colaborador_id");
                empresaID = dataobj.getInt("empresa_id");
                codigo = dataobj.getInt("codigo");
                senha = dataobj.getInt("senha");
                dataRegisto = dataobj.getString("data_registo");

                for (int j = 0; j < colabsArray.length(); j++) {

                    JSONObject dataobj2 = colabsArray.getJSONObject(j);

                    databaseHelper = new DatabaseHelper(PicaPonto.getContext());
                    id2 = dataobj2.getInt("id");

                    // Colaboradores
                    if(id2 == colaboradorID){
                        nome = dataobj2.getString("nome");
                        picagemManual = dataobj2.getInt("picagem_manual");
                    }


                }

                Colaborador newCol = new Colaborador(colaboradorID, empresaID, colaboradorID, codigo, "", senha, dataRegisto);
                newCol.setNome(nome);
                newCol.setPicagemManual(picagemManual);
                databaseHelper.addUser(newCol);

            }

            for (int i = picagensArray.length() -1; i > -1; i--) {

                JSONObject picagensObj = picagensArray.getJSONObject(i);

                databaseHelper = new DatabaseHelper(PicaPonto.getContext());

                //Picagens
                colID = picagensObj.getInt("colaborador_id");
                tipoMovimento = picagensObj.getString("tipo_movimento");
                data = picagensObj.getString("data");
                hora = picagensObj.getString("hora");

                Registo newReg = new Registo(colID, tipoMovimento, data, hora);
                newReg.setCodigo(colID.toString());
                databaseHelper.addRegisto(newReg);

            }


            for(int i = feriasArray.length() - 1; i >= 0; i--){

                JSONObject feriasObj = feriasArray.getJSONObject(i);

                databaseHelper = new DatabaseHelper(PicaPonto.getContext());

                //Ferias
                feriasID = feriasObj.getInt("colaborador_id");
                periodo_inicio = feriasObj.getString("periodo_inicio");
                periodo_fim = feriasObj.getString("periodo_fim");
                observacoes = feriasObj.getString("observacoes");
                estado = feriasObj.getString("estado");

                Ferias newFer = new Ferias(feriasID, periodo_inicio, periodo_fim, observacoes, estado, 1, "0");
                databaseHelper.addFerias(newFer);

            }

            SharedPreferences sharedPreferences = PicaPonto.getContext().getSharedPreferences("PicaPontoPrefs", MODE_PRIVATE);

            if(sharedPreferences.contains("user")){
                Intent goMain = new Intent(PicaPonto.getContext(), MainActivity.class);
                startActivity(goMain);
            } else {
                Intent goPinCode = new Intent(PicaPonto.getContext(), ColaboradorPinActivity.class);
                startActivity(goPinCode);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
