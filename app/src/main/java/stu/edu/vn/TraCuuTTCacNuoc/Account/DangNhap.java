package stu.edu.vn.TraCuuTTCacNuoc.Account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import stu.edu.vn.TraCuuTTCacNuoc.MainActivity;
import stu.edu.vn.TraCuuTTCacNuoc.R;

public class DangNhap extends AppCompatActivity {

    EditText txtEmail, txtpass;
    Button btndangnhap,btndangky;
    FirebaseAuth mAuth;

    public void addControls(){
        txtEmail =findViewById(R.id.txtEmail);
        txtpass =findViewById(R.id.txtPassword);
        btndangnhap =findViewById(R.id.btnDangNhap);
        btndangky =findViewById(R.id.btnDangKy);
        mAuth =  FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_nhap);

        addControls();
        btndangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dang_ky();
            }
        });

        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }
    private void dang_ky() {
        Intent intent = new Intent(DangNhap.this, DangKy.class);
        startActivity(intent);
    }
    private void login() {

        String email = txtEmail.getText().toString();
        String pass =txtpass.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Vui lòng nhập password", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String userUID = mAuth.getCurrentUser().getUid();
                    Toast.makeText(DangNhap.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DangNhap.this, MainActivity.class);
                    intent.putExtra("userUID", userUID);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(DangNhap.this, "Đăng Nhập không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}