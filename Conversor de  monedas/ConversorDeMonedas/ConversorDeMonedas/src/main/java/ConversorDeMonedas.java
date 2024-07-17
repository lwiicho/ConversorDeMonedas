package com.conversordemonedas;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ConversorDeMonedas {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/686ddb693136e2918387504e/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese la moneda origen (USD, EUR, ARS, COP, CAD, CNY, JPY, MXN): ");
        String monedaOrigen = scanner.next().toUpperCase();

        System.out.print("Ingrese la moneda destino (USD, EUR, ARS, COP, CAD, CNY, JPY, MXN): ");
        String monedaDestino = scanner.next().toUpperCase();

        System.out.print("Ingrese la cantidad a convertir: ");
        double cantidad = scanner.nextDouble();

        try {
            JsonObject data = obtenerDatosDeAPI(API_URL + monedaOrigen);

            if (data != null) {
                convertirMonedas(cantidad, data, monedaDestino);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JsonObject obtenerDatosDeAPI(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Leer la respuesta JSON
        InputStreamReader reader = new InputStreamReader(conn.getInputStream());
        JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
        reader.close();

        return data;
    }

    private static void convertirMonedas(double cantidad, JsonObject data, String monedaDestino) {
        JsonObject rates = data.getAsJsonObject("conversion_rates");

        if (rates.has(monedaDestino)) {
            double tasaMonedaDestino = rates.get(monedaDestino).getAsDouble();
            double cantidadMonedaDestino = cantidad * tasaMonedaDestino;
            String monedaOrigen = data.get("base_code").getAsString();
            System.out.printf("%.2f %s equivale a %.2f %s%n", cantidad, monedaOrigen, cantidadMonedaDestino, monedaDestino);
        } else {
            System.out.println("Moneda destino no encontrada en las tasas de conversión.");
        }
    }
}
