import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

public class SumbitPostCharge {
    public static void main(String[] args) {
        Info info = new Info();

        LoadInfo information = info.performGetRequest();

        double currentLoad = information.baseCurrentLoad;
        double batteryCapacity = information.batteryCapacity;
        double durationSeconds = 0;


        boolean b = true;

        while(b) {
            System.out.println("Start charging by writing 'on'");
            Scanner scanner = new Scanner(System.in);
            String s1 = scanner.nextLine().trim().toLowerCase();

            if (s1.equals("on")) {
                long startTime = System.currentTimeMillis();
                startStopCharging("on");

                System.out.println("Charging started. Type 'off' to stop charging:");
                while (true) {
                    String s2 = scanner.nextLine().trim().toLowerCase();

                    if (s2.equals("off")) {
                        long endTime = System.currentTimeMillis();
                        startStopCharging("off");

                        long durationMillis = endTime - startTime;
                        durationSeconds = durationMillis / 1000.0;

                        System.out.println("Charging stopped.");
                        System.out.println("charging for: " + durationSeconds + " seconds");
                        b = false;
                        break;

                    } else if (s2.equals("on")) {
                        System.out.println("Charging is already on. Type 'off' to stop.");
                    } else {
                        System.out.println("Invalid input. Type 'off' to stop charging.");
                    }
                }

            } else {
                System.out.println("Charging not started. Please type 'on' to begin.");

            }
        }


        //charing capacity 7.4kW
        double chargingStationCapacity = 7.4;

        double chargingAmount = chargingStationCapacity * durationSeconds / 60;
//        System.out.println(chargingAmount);

        currentLoad = currentLoad + chargingAmount;
//        System.out.println(currentLoad);

        double loadLevel = currentLoad * 100 / batteryCapacity;
        //to get rid of decimals
        String newValue = Integer.toString((int) loadLevel);
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
