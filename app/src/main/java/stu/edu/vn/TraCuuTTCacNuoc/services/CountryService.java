package stu.edu.vn.TraCuuTTCacNuoc.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import stu.edu.vn.TraCuuTTCacNuoc.helpers.ApiHelper;
import stu.edu.vn.TraCuuTTCacNuoc.models.CountryInfo;

public class CountryService {
    private static final String REST_COUNTRIES_SEARCH_URL = "https://restcountries.com/v3.1/name/";
    private static final String GEO_NAME_URL_TEMPLATE = "http://api.geonames.org/countryInfoJSON?formatted=true&lang=it&country=%s&username=minhduc";
    private static final String NEIGHBOURS_URL_TEMPLATE = "http://api.geonames.org/neighboursJSON?country=%s&username=minhduc";

    public CountryInfo fetchCountryInfo(String countryName) throws Exception {
        String restCountriesData = ApiHelper.fetchFromApi(REST_COUNTRIES_SEARCH_URL + countryName);
        JSONArray jsonArray = new JSONArray(restCountriesData);

        if (jsonArray.length() > 0) {
            JSONObject countryData = jsonArray.getJSONObject(0);

            CountryInfo countryInfo = new CountryInfo();
            countryInfo.setName(countryData.getJSONObject("name").getString("common"));
            countryInfo.setIsoCode(countryData.optString("cca2", ""));
            countryInfo.setLatitude(countryData.getJSONArray("latlng").optDouble(0, 0));
            countryInfo.setLongitude(countryData.getJSONArray("latlng").optDouble(1, 0));
            countryInfo.setFlagUrl(countryData.getJSONObject("flags").getString("png"));

            String geoNameUrl = String.format(GEO_NAME_URL_TEMPLATE, countryInfo.getIsoCode());
            String geoNamesData = ApiHelper.fetchFromApi(geoNameUrl);

            JSONObject geoObject = new JSONObject(geoNamesData);
            JSONArray geonames = geoObject.getJSONArray("geonames");
            if (geonames.length() > 0) {
                JSONObject geoCountry = geonames.getJSONObject(0);
                countryInfo.setCapital(geoCountry.optString("capital", "N/A"));
                countryInfo.setPopulation(geoCountry.optInt("population", -1));
                countryInfo.setContinent(geoCountry.optString("continentName", "N/A"));
                countryInfo.setCurrencyCode(geoCountry.optString("currencyCode", "N/A"));
                countryInfo.setArea(geoCountry.optDouble("areaInSqKm", 0.0));

                // hàng xóm
                String neighboursUrl = String.format(NEIGHBOURS_URL_TEMPLATE, countryInfo.getIsoCode());
                String neighboursData = ApiHelper.fetchFromApi(neighboursUrl);
                List<String> neighbours = new ArrayList<>();
                if (neighboursData != null) {
                    JSONArray neighboursArray = new JSONObject(neighboursData).optJSONArray("geonames");
                    for (int i = 0; i < neighboursArray.length(); i++) {
                        neighbours.add(neighboursArray.getJSONObject(i).optString("countryName", "Unknown"));
                    }
                }
                countryInfo.setNeighbours(neighbours);
            }

            return countryInfo;
        } else {
            throw new Exception("Country not found");
        }
    }
}
