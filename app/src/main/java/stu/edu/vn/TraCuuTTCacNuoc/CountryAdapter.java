package stu.edu.vn.TraCuuTTCacNuoc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

import stu.edu.vn.TraCuuTTCacNuoc.R;
import stu.edu.vn.TraCuuTTCacNuoc.models.CountryInfo;

public class CountryAdapter extends ArrayAdapter<CountryInfo> {
    private Context context;
    private List<CountryInfo> countries;

    public CountryAdapter(@NonNull Context context, @NonNull List<CountryInfo> countries) {
        super(context, 0, countries); // 0: Không sử dụng layout mặc định
        this.context = context;
        this.countries = countries;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Sử dụng LayoutInflater để tạo View nếu convertView null
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_country, parent, false);
        }
        // Liên kết các thành phần trong layout item_country.xml
        TextView txtName = convertView.findViewById(R.id.txt_countryname);
        ImageView imgFlag = convertView.findViewById(R.id.img_country_flag);
        // Lấy dữ liệu quốc gia tại vị trí hiện tại
        CountryInfo country = countries.get(position);
        // Gán tên quốc gia
        txtName.setText(country.getName());
        // Tải hình ảnh từ URL bằng Glide
        Glide.with(context)
                .load(country.getFlagUrl())
                .placeholder(R.drawable.placeholder) // Hình ảnh chờ
                .error(R.drawable.error)            // Hình ảnh lỗi
                .into(imgFlag);
        return convertView; // Trả về View đã hoàn chỉnh
    }
}

