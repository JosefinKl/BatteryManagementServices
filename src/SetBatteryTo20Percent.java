import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SetBatteryTo20Percent {
    public static void main(String[] args) {
        setBatteryTo20Percent();
        Info info = new Info();
        double batteryCapacity = info.performGetRequest();
//        double currentLoad = information.baseCurrentLoad;

//        System.out.println("BatteryCapacity" + batteryCapacity);

    }

    public static void setBatteryTo20Percent(){
        try {
            URL url = new URL("http://127.0.0.1:5000/discharge");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setDoOutput(true);
            String command = "on";

            String jsonInputString = "{\"discharging\": \"" + command + "\"}";


            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = con.getResponseCode();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

//                System.out.println(content.toString());
            }

        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
