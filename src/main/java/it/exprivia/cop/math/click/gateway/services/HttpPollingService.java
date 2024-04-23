package it.exprivia.cop.math.click.gateway.services;

import it.exprivia.cop.math.click.gateway.models.DataObject;
import jakarta.annotation.PostConstruct;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;
@Service
public class HttpPollingService {
    private final OkHttpClient okHttpClient = new OkHttpClient();
    @Value("${pollingTime}")
    private long pollingTime;
    @Value("${urlEndpointMathClick}")
    private String urlEndpointMathClick;

    @PostConstruct
    public void startPolling() {

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                    System.out.println(getDataObject());

            }

        }, 0, pollingTime);

    }


    public static DataObject createDataObject(String dataString) {
        String[] parts = dataString.split(",\\s+");
        long time = Long.parseLong(parts[1]);
        double longitude = Double.parseDouble(parts[3]);
        double latitude = Double.parseDouble(parts[5]);
        double depth = Double.parseDouble(parts[7]);
        double temp = Double.parseDouble(parts[9]);
        return new DataObject(time, longitude, latitude, depth, temp);
    }


    public DataObject getDataObject() {

        try {
            Request request = new Request.Builder()
                    .url(urlEndpointMathClick)
                    .build();



            Call call = okHttpClient.newCall(request);
            Response response = call.execute();

            String resBody = response.body().string();

            if(resBody != null && !resBody.isEmpty()) {
                    return createDataObject(resBody);
            }

            return null;


        } catch (Exception e) {

            return null;
        }


    }


}
