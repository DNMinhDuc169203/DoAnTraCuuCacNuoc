package stu.edu.vn.TraCuuTTCacNuoc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import com.google.firebase.database.DatabaseReference;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;

import stu.edu.vn.TraCuuTTCacNuoc.models.CountryInfo;

public class data_mainActivity extends AppCompatActivity {

    private TextView txtCountryDetails;
    private ImageView imgFlag;
    private Button btnql;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_main);

        txtCountryDetails = findViewById(R.id.txt_country_details);
        imgFlag = findViewById(R.id.img_country_flag);
        btnql =findViewById(R.id.btn_quaylai);

        // Khởi tạo Firebase
        database = FirebaseDatabase.getInstance().getReference("countries");

        String countryDetails = getIntent().getStringExtra("countryDetails");
        String flagUrl = getIntent().getStringExtra("flagUrl");

        // Hiển thị dữ liệu
        txtCountryDetails.setText(countryDetails);
        Glide.with(this).load(flagUrl).into(imgFlag);

        btnql.setOnClickListener(view -> {
            saveToFirebase(countryDetails, flagUrl);
             finish();
        });
    }
    private void saveToFirebase(String countryDetails, String flagUrl) {
        String userUID = getIntent().getStringExtra("userUID");


        if (userUID == null || userUID.isEmpty()) {
            Toast.makeText(this, "Không xác định được UserUID!", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference userCountriesRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userUID)
                .child("countries");

        // Tạo đối tượng lưu trữ
        String id = userCountriesRef.push().getKey();
        CountryInfo info = new CountryInfo();
        info.setName(countryDetails);
        info.setFlagUrl(flagUrl);


        if (id != null) {
            userCountriesRef.child(id).setValue(info)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Lưu thành công!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi lưu!", Toast.LENGTH_SHORT).show());
        }
    }
}