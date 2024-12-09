package stu.edu.vn.TraCuuTTCacNuoc.GioiThieu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import stu.edu.vn.TraCuuTTCacNuoc.MainActivity;
import stu.edu.vn.TraCuuTTCacNuoc.R;

public class GioiThieu extends AppCompatActivity {

    private Button btnout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gioi_thieu);

        btnout =findViewById(R.id.btn_outgt);
        btnout.setOnClickListener(view -> {
           finish();
        });
    }
}