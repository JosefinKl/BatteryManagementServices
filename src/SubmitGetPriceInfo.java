import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SubmitGetPriceInfo {
    public static void main(String[] args) {
        double[] values = performGetRequest();

        if (values != null) {
            System.out.println("Prices per hour:");
            for (double d : values) {
                System.out.println(d);
            }
        }
    }

    public static double[] performGetRequest() {
        try {
            URL url = new URL("http://127.0.0.1:5000/priceperhour");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");


            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
// Clean the string and parse to double[]
                String json = content.toString()
                        .replace("[", "")
                        .replace("]", "")
                        .trim();

                String[] parts = json.split(",");

                double[] result = new double[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    result[i] = Double.parseDouble(parts[i]);
                }

                return result;
            }

        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
