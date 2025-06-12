import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.*;


public class Info {
    public static void main(String[] args) {
        performGetRequest();
    }

    public static double performGetRequest() {
//        double baseCurrentLoad = 0;
        double batteryCapacity = 0;
        double batteryMaxCapacity = 46.3; //kWh

        try {
            URL url = new URL("http://127.0.0.1:5000/info");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");


            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                JSONObject json = new JSONObject(content.toString());

//                baseCurrentLoad = json.getDouble("base_current_load");
                batteryCapacity = json.getDouble("battery_capacity_kWh");

//                double loadLevel = baseCurrentLoad * 100 / batteryCapacity ;
//                System.out.println("baseCurrentLoad: " + baseCurrentLoad);
                System.out.println("battery capacity: " + batteryCapacity);

//                double chargeLevel = batteryCapacity * 100 / batteryMaxCapacity;

                System.out.println(content);
                //to get rid of decimals
//                String newValue = Integer.toString((int)chargeLevel);
//                System.out.println("Charge level is: " + newValue + "%");

                CalculateCurrentChargeLevel calculateCurrentChargeLevel = new CalculateCurrentChargeLevel();
                double ChargeLevel = calculateCurrentChargeLevel.ChargeLevel(batteryCapacity);



            }

        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        return batteryCapacity;
    }

}
