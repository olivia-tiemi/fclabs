package com.pods.fclabs.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pods.fclabs.exception.CepInvalidoException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
@Service
public class CepService {
    public Object validaCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setRequestProperty("User-Agent", "Java 8");
            connection.connect();
            JsonElement jsonElement = new JsonParser().parse(new InputStreamReader(connection.getInputStream()));
            return jsonElement.getAsJsonObject();
        } catch (IOException ex) {
            throw new CepInvalidoException("CEP inválido.");
        }
    }
}
