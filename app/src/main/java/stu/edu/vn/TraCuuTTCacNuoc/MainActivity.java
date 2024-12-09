package stu.edu.vn.TraCuuTTCacNuoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;

import stu.edu.vn.TraCuuTTCacNuoc.Account.DangNhap;
import stu.edu.vn.TraCuuTTCacNuoc.GioiThieu.GioiThieu;
import stu.edu.vn.TraCuuTTCacNuoc.models.CountryInfo;
import stu.edu.vn.TraCuuTTCacNuoc.services.CountryService;
import stu.edu.vn.TraCuuTTCacNuoc.helpers.ApiHelper;

public class MainActivity extends AppCompatActivity {

    private static final String REST_COUNTRIES_URL = "https://countriesnow.space/api/v0.1/countries";
    private EditText edtCountry;
    private Button btnLoad,btnthoat,btngioithieu,btndanhsach;
    private TextView txtData,txtan;
    private ImageView imgFlag;
    private Spinner spinnerCountries;

    private CountryService countryService;
    private ArrayList<String> countryList = new ArrayList<>();
//    private ArrayList<String> countryCodeList = new ArrayList<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCountry = findViewById(R.id.edt_country);
        btnLoad = findViewById(R.id.btn_load);
        btngioithieu =findViewById(R.id.btnGioiThieu);
        btnthoat =findViewById(R.id.btnThoat);
        btndanhsach=findViewById(R.id.btnHienThiDanhSach);
        txtData = findViewById(R.id.txt_data);
        txtan =findViewById(R.id.txtFlagLabel);
        imgFlag = findViewById(R.id.img_flag);
        spinnerCountries = findViewById(R.id.spinner_countries);

        mAuth = FirebaseAuth.getInstance();
        String userUID = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;



        SharedPreferences sharedPreferences = getSharedPreferences("AppState", MODE_PRIVATE);
        // Khôi phục trạng thái
       // String savedCountryName = sharedPreferences.getString("countryName", "");
       // int selectedSpinnerIndex = sharedPreferences.getInt("spinnerIndex", 0);

        countryService = new CountryService();
        //txtan.setVisibility(View.GONE);
        loadCountries();

        btngioithieu.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GioiThieu.class);
            startActivity(intent);
        });
        btnthoat.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, DangNhap.class);
            startActivity(intent);

        });
        btndanhsach.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DanhSachActivity.class);
            intent.putExtra("userUID", userUID);
            startActivity(intent);
        });

        btnLoad.setOnClickListener(view -> {

            String countryName = edtCountry.getText().toString().trim();
            int spinnerIndex = spinnerCountries.getSelectedItemPosition();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("countryName", countryName);
            editor.putInt("spinnerIndex", spinnerIndex);
            editor.apply();

//            int selectedIndex = spinnerCountries.getSelectedItemPosition();
//            String countryName;
//
//            if (selectedIndex > 0) {
//                countryName = countryList.get(selectedIndex);
//            } else {
//                // Nếu không, lấy giá trị từ EditText
//                countryName = edtCountry.getText().toString().trim();
//            }
//
//            // Kiểm tra nếu vẫn không có tên quốc gia
//            if (countryName.isEmpty() || countryName.equals("Danh sách quốc gia")) {
//                txtData.setText("Vui lòng chọn quốc gia từ danh sách hoặc nhập.");
//                imgFlag.setImageDrawable(null);
//                txtan.setVisibility(View.GONE);
//                return;
//            }

             //Nếu không có tên quốc gia, lấy tên từ spinner
            if (countryName.isEmpty()) {
                int selectedIndex = spinnerCountries.getSelectedItemPosition();
                if (selectedIndex == 0 || selectedIndex == -1) {
                    txtData.setText("Vui lòng chọn quốc gia từ danh sách hoặc nhập.");
                    // Xóa hình ảnh
                    imgFlag.setImageDrawable(null);
                    txtan.setVisibility(View.GONE);
                    return;
                } else {
                    countryName = countryList.get(selectedIndex);
                }
            }
            if (countryName.equalsIgnoreCase("United States") || countryName.equalsIgnoreCase("USA")) {
                countryName = "United States of America";
            }

            // Đảm bảo biến countryName không thay đổi nữa, đánh dấu là final
            final String finalCountryName = countryName;

            new Thread(() -> {
                try {
                    // Lúc này, bạn có thể sử dụng finalCountryName trong Lambda Expression
                    CountryInfo info = countryService.fetchCountryInfo(finalCountryName);
                    runOnUiThread(() -> {

                        String result = "Tên đất nước: " + info.getName() + "\n"
                                + "ISO Code: " + info.getIsoCode() + "\n"
                                + "Thủ đô: " + info.getCapital() + "\n"
                                + "Dân số: " + info.getPopulation() + "\n"
                                + "Lục địa: " + info.getContinent() + "\n"
                                + "Mã tiền tệ: " + info.getCurrencyCode() + "\n"
                                + "latlng:[" + info.getLatitude() + "," + info.getLongitude()+ "]" +"\n"
                                + "Diện tích: " + info.getArea() + " km²\n"
                                + "Các quốc gia hàng xóm: " + info.getNeighbours() + "\n";

                        //txtData.setText(result);
//                        Glide.with(MainActivity.this).load(info.getFlagUrl()).into(imgFlag);
//                        txtan.setVisibility(View.VISIBLE);

                        txtData.setText("");
                        txtan.setVisibility(View.GONE);

                        Intent intent = new Intent(MainActivity.this, data_mainActivity.class);
                        intent.putExtra("userUID", userUID);
                        intent.putExtra("countryDetails", result);
                        intent.putExtra("flagUrl", info.getFlagUrl());
                        startActivity(intent);
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> txtData.setText("Lỗi: " + e.getMessage()));
                }
            }).start();
        });
    }

    // Hàm tải danh sách quốc gia
    private void loadCountries() {
        new Thread(() -> {
            try {
                String countriesData = ApiHelper.fetchFromApi(REST_COUNTRIES_URL);
                if (countriesData != null) {
                    JSONObject jsonResponse = new JSONObject(countriesData);
                    JSONArray countriesArray = jsonResponse.getJSONArray("data");

                    countryList.add("Danh sách quốc gia");
                    for (int i = 0; i < countriesArray.length(); i++) {
                        JSONObject country = countriesArray.getJSONObject(i);
                        String countryName = country.getString("country");
                        countryList.add(countryName);
                    }

                    runOnUiThread(() -> {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                MainActivity.this,
                                android.R.layout.simple_spinner_item,
                                countryList
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCountries.setAdapter(adapter);
                        spinnerCountries.setSelection(0);
                    });
                } else {
                    runOnUiThread(() -> txtData.setText("Không tải được danh sách quốc gia."));
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> txtData.setText("Lỗi tải danh sách quốc gia: " + e.getMessage()));
            }
        }).start();
    }
}
