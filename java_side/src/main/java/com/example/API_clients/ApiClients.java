package com.example.API_clients;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClients {

    private static final String BASE_URL = "http://localhost:3000"; // adjust if needed

    // === GET all ===
    public static JSONArray getAll(String resource) throws IOException {
        String endpoint = BASE_URL + "/" + resource;
        return getJsonArray(endpoint);
    }

    // === GET by ID ===
    public static JSONObject getById(String resource, int id) throws IOException {
        String endpoint = BASE_URL + "/" + resource + "/" + id;
        return getJsonObject(endpoint);
    }

    // === CREATE ===
    public static JSONObject create(String resource, JSONObject data) throws IOException {
        String endpoint = BASE_URL + "/" + resource;
        return sendRequest(endpoint, "POST", data);
    }

    // === UPDATE ===
    public static JSONObject update(String resource, int id, JSONObject data) throws IOException {
        String endpoint = BASE_URL + "/" + resource + "/" + id;
        return sendRequest(endpoint, "PUT", data);
    }

    // === DELETE ===
    public static boolean delete(String resource, int id) throws IOException {
        String endpoint = BASE_URL + "/" + resource + "/" + id;
        HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
        conn.setRequestMethod("DELETE");
        conn.connect();
        int responseCode = conn.getResponseCode();
        conn.disconnect();
        return responseCode == HttpURLConnection.HTTP_NO_CONTENT || responseCode == HttpURLConnection.HTTP_OK;
    }

    // === Internal: GET JSON Array ===
    private static JSONArray getJsonArray(String endpoint) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        InputStream inputStream = conn.getInputStream();
        String response = new BufferedReader(new InputStreamReader(inputStream))
                .lines().reduce("", (a, b) -> a + b);
        conn.disconnect();
        return new JSONArray(response);
    }

    // === Internal: GET JSON Object ===
    private static JSONObject getJsonObject(String endpoint) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        InputStream inputStream = conn.getInputStream();
        String response = new BufferedReader(new InputStreamReader(inputStream))
                .lines().reduce("", (a, b) -> a + b);
        conn.disconnect();
        return new JSONObject(response);
    }

    // === Internal: POST or PUT ===
    private static JSONObject sendRequest(String endpoint, String method, JSONObject data) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = data.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        InputStream inputStream = conn.getInputStream();
        String response = new BufferedReader(new InputStreamReader(inputStream))
                .lines().reduce("", (a, b) -> a + b);
        conn.disconnect();
        return new JSONObject(response);
    }
}
