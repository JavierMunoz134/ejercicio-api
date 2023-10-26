import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        try {
            String apiUrl = "https://www.amiiboapi.com/api/amiibo/?character=zelda";

            String apiResponse = fetchDataFromAmiiboAPI(apiUrl);

            parseAmiiboData(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String fetchDataFromAmiiboAPI(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();
            return response.toString();
        } else {
            throw new Exception("Error al hacer la solicitud a la API de Amiibo. Código de respuesta: " + responseCode);
        }
    }

    public static void parseAmiiboData(String jsonData) {

        String[] lines = jsonData.split("\n");

        for (String line : lines) {
            if (line.contains("character")) {

                String character = extractValue(line, "character");
                String amiiboSeries = extractValue(line, "amiiboSeries");
                String gameSeries = extractValue(line, "gameSeries");
                String type = extractValue(line, "type");
                String image = extractValue(line, "image");

                System.out.println("Personaje: " + character);
                System.out.println("Serie Amiibo: " + amiiboSeries);
                System.out.println("Serie de Juego: " + gameSeries);
                System.out.println("Tipo: " + type);
                System.out.println("Imagen: " + image);
                System.out.println("------------------------------");
            }
        }
    }


    public static String extractValue(String line, String key) {

        int keyIndex = line.indexOf("\"" + key + "\"");
        int startIndex = line.indexOf(":", keyIndex);
        int endIndex = line.indexOf(",", startIndex);

        if (endIndex == -1) {
            endIndex = line.indexOf("}", startIndex);
        }

        return line.substring(startIndex + 1, endIndex).trim().replace("\"", "");
    }
}
