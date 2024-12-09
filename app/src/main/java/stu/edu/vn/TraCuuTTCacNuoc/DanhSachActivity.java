    package stu.edu.vn.TraCuuTTCacNuoc;

    import android.content.DialogInterface;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.ListView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.activity.EdgeToEdge;
    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;

    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import java.util.ArrayList;

    import stu.edu.vn.TraCuuTTCacNuoc.models.CountryInfo;

    public class DanhSachActivity extends AppCompatActivity {

        ListView lv;
        TextView tvEmpty;
        Button btnve;
        private DatabaseReference database;
        private ArrayList<CountryInfo> countryList;
        private CountryAdapter adapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_danh_sach);
            lv = findViewById(R.id.lv);
            btnve = findViewById(R.id.btnve);
            tvEmpty = findViewById(R.id.tvEmpty);
            // Nhận UID từ Intent
            String userUID = getIntent().getStringExtra("userUID");
            // Tham chiếu đến nhánh "users/{userUID}/countries" trong Firebase
            database = FirebaseDatabase.getInstance().getReference("users").child(userUID).child("countries");
            // Danh sách quốc gia
            countryList = new ArrayList<>();
            adapter = new CountryAdapter(this, countryList);
            lv.setAdapter(adapter);

            btnve.setOnClickListener(view -> finish());
            // Tải dữ liệu từ Firebase
            loadCountriesFromFirebase();
            addEvents();
        }

        private void addEvents() {
            lv.setOnItemLongClickListener((adapterView, view, position, l) -> {
                if (position < 0 || position >= countryList.size()) {
                    Toast.makeText(DanhSachActivity.this, "Danh sách không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return true;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(DanhSachActivity.this);
                builder.setTitle("Xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa không?");
                builder.setPositiveButton("Có", (dialogInterface, i) -> {
                    CountryInfo countryToDelete = countryList.get(position);

                    // Xóa dữ liệu từ Firebase
                    database.child(countryToDelete.getId()).removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(DanhSachActivity.this, "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DanhSachActivity.this, "Lỗi khi xóa dữ liệu!", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
                builder.setNegativeButton("Không", (dialogInterface, i) -> dialogInterface.dismiss());
                builder.setCancelable(false);
                builder.show();

                return true;
            });
        }


        private void loadCountriesFromFirebase() {
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    countryList.clear();
                    for (DataSnapshot countrySnapshot : snapshot.getChildren()) {
                        CountryInfo country = countrySnapshot.getValue(CountryInfo.class);
                        if (country != null) {
                            country.setId(countrySnapshot.getKey());
                            countryList.add(country);
                        }
                    }
                    adapter.notifyDataSetChanged();

                    if (countryList.isEmpty()) {
                        lv.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        lv.setVisibility(View.VISIBLE);
                        tvEmpty.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(DanhSachActivity.this, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }