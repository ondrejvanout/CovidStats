package com.development.covidstats;

import android.app.VoiceInteractor;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class CovidDataService {
    private Context context;
    private Date date;
    private int index;
    private int indexHospital;

    // URLs
    private final String CASES_URL = "https://onemocneni-aktualne.mzcr.cz/api/v2/covid-19/nakaza.json";
    private final String TESTS_URL = "https://onemocneni-aktualne.mzcr.cz/api/v2/covid-19/testy.json";
    private final String DEATH_URL = "https://onemocneni-aktualne.mzcr.cz/api/v2/covid-19/hospitalizace.json";

    public CovidDataService(Context context, Date selectedDate) {
        this.context = context;
        this.date = selectedDate;
        index = 0;
        indexHospital = 0;
    }

    public interface VolleyResponseListener {
        void onError(String message);
        void onResponse(String response);
    }

    /*
     * Get count of new cases by day
     */
    public void getCasesCount(String date, VolleyResponseListener responseListener) {
        JsonObjectRequest casesRequest = new JsonObjectRequest(Request.Method.GET, CASES_URL, null, response -> {
            try {
                JSONArray casesArray = response.getJSONArray("data");
                JSONObject selectedCases = new JSONObject();
                for (int i = 0; i < casesArray.length(); i++) {
                    selectedCases = casesArray.getJSONObject(i);
                    if (selectedCases.getString("datum").equals(date)) {
                        index = i;
                        break;
                    }
                }

                responseListener.onResponse(selectedCases.getString("prirustkovy_pocet_nakazenych"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            responseListener.onError("Nepodařilo se načíst data.");
        });
        RequestsSingleton.getInstance(context).addToRequestQueue(casesRequest);
    }

    /*
     * Get count of tests by date
     */
    public void getTestsCount(String date, VolleyResponseListener responseListener) {
        JsonObjectRequest testsRequest = new JsonObjectRequest(Request.Method.GET, TESTS_URL, null, response -> {
            try {
                JSONArray testsArray = response.getJSONArray("data");
                JSONObject selectedTests = testsArray.getJSONObject(index);

                responseListener.onResponse(selectedTests.getString("prirustkovy_pocet_testu"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            responseListener.onError("Nepodařilo se načíst data.");
        });
        RequestsSingleton.getInstance(context).addToRequestQueue(testsRequest);
    }

    /*
     * Get count of death people by date
     */
    public void getDeathCount(String date, VolleyResponseListener responseListener) {
        JsonObjectRequest deathRequest = new JsonObjectRequest(Request.Method.GET, DEATH_URL, null, response -> {
            try {
                JSONArray deathArray = response.getJSONArray("data");
                JSONObject selectedDeath = new JSONObject();
                for (int i = 0; i < deathArray.length(); i++) {
                    selectedDeath = deathArray.getJSONObject(i);
                    if (selectedDeath.getString("datum").equals(date)) {
                        indexHospital = i;
                        break;
                    }
                }

                responseListener.onResponse(selectedDeath.getString("umrti"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            responseListener.onError("Nepodařilo se načíst data.");
        });
        RequestsSingleton.getInstance(context).addToRequestQueue(deathRequest);
    }

    /*
     * Get count of hospitalization people by date
     */
    public void getHospitalizationCount(String date, VolleyResponseListener responseListener) {
        JsonObjectRequest hospitalizationRequest = new JsonObjectRequest(Request.Method.GET, DEATH_URL, null, response -> {
            try {
                JSONArray hospitalizationArray = response.getJSONArray("data");
                JSONObject selectedHospitalization = hospitalizationArray.getJSONObject(indexHospital);

                responseListener.onResponse(selectedHospitalization.getString("pocet_hosp"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            responseListener.onError("Nepodařilo se načíst data.");
        });
        RequestsSingleton.getInstance(context).addToRequestQueue(hospitalizationRequest);
    }
}
