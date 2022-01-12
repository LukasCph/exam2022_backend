package callables;

import utils.HttpUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

public class Parallel implements Callable<String> {

    private String host;

    public Parallel(String host) {
        this.host = host;
    }

    @Override
    public String call() throws Exception {
        String response = null;


        URLConnection urlConnection = new URL(host).openConnection();
        urlConnection.setRequestProperty("Accept","application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader (urlConnection.getInputStream()));
        String i;
        while ((i = br.readLine()) != null)
        {
            response = i;
        }
        return response;
    }
}
