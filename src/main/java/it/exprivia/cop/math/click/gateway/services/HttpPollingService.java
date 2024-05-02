package it.exprivia.cop.math.click.gateway.services;

import com.google.gson.Gson;
import it.exprivia.cop.math.click.gateway.models.DataObject;
import it.exprivia.cop.math.click.gateway.models.config.ConfigPojo;
import jakarta.annotation.PostConstruct;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class HttpPollingService {
    private final OkHttpClient okHttpClient = new OkHttpClient();
    @Value("${pollingTime}")
    private long pollingTime;
    @Value("${urlEndpointMathClick}")
    private String urlEndpointMathClick;
    @Value("${configUrl}")
    private String configUrl;

    @Autowired
    private TelemetryService telemetryService;

    private Gson gson = new Gson();


    @PostConstruct
    public void startPolling() throws FileNotFoundException {


        BufferedReader bufferedReader = new BufferedReader(new FileReader(this.configUrl));
        ConfigPojo configPojo = gson.fromJson(bufferedReader, ConfigPojo.class);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {





                    telemetryService.sendData(getDataObject(),configPojo);

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
        return new DataObject(time, longitude, latitude, -depth, temp);
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
