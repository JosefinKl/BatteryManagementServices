import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SumbitPostCharge {
    public static void main(String[] args) {
        Info info = new Info();

        LoadInfo information = info.performGetRequest();
//        System.out.println(info);
        double currentLoad = information.baseCurrentLoad;
        double batteryCapacity = information.batteryCapacity;


        startStopCharging("on");
        //time to load the battery
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("charging for 5s");
        startStopCharging("off");

        //charing capacity 7.4kW
        double chargingStationCapacity = 7.4;

        double chargingAmount = chargingStationCapacity * 5/60;
//        System.out.println(chargingAmount);

        currentLoad = currentLoad + chargingAmount;
//        System.out.println(currentLoad);

        double loadLevel = currentLoad * 100 / batteryCapacity;
        //to get rid of decimals
        String newValue = Integer.toString((int)loadLevel);
        System.out.println("loadlevel is: " + newValue + "%");



    }

    public static void startStopCharging(String command) {



        try {
            URL url = new URL("http://127.0.0.1:5000/charge");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setDoOutput(true);

            String jsonInputString = "{\"charging\": \"" + command + "\"}";

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
