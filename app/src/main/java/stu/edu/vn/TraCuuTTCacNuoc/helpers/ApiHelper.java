package stu.edu.vn.TraCuuTTCacNuoc.helpers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiHelper {

    public static String fetchFromApi(String urlString) throws Exception {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(13000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder data = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
                return data.toString();
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new Exception("Lỗi: Không tìm thấy tài nguyên (404 Not Found). URL: " + urlString);
            } else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                throw new Exception("Lỗi: Máy chủ nội bộ gặp vấn đề (500 Internal Server Error).");
            } else {
                throw new Exception("Lỗi HTTP không xác định, mã lỗi: " + responseCode);
            }
        } finally {
            if (reader != null) reader.close();
            if (connection != null) connection.disconnect();
        }
    }
}
