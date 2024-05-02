package it.exprivia.cop.math.click.gateway.services;


import it.exprivia.cop.math.click.gateway.models.DataObject;
import it.exprivia.cop.math.click.gateway.models.config.ConfigPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class TelemetryService {

    @Autowired
    private MqttService mqttService;

    public void sendData(DataObject dataObject, ConfigPojo configPojo) {



        if(dataObject != null) {

            if (dataObject.getLatitude() != null && dataObject.getLongitude() != null && dataObject.getTime() != null) {

                Map<String, Object> telemetry = new HashMap<>();

                Map<String, Object> positionData = new HashMap<>();


                positionData.put("timestamp", dataObject.getTime());
                positionData.put("latitude", dataObject.getLatitude());
                positionData.put("longitude", dataObject.getLongitude());
                telemetry.put("payload", positionData);

                mqttService.sendTelemetryInTopicMqtt("device_events/" + configPojo.getIdLinkedAssetInstanceTracking(), telemetry);
            }


            if (dataObject.getTemp() != null && dataObject.getDepth() != null && dataObject.getTime() != null) {

                Map<String, Object> telemetry = new HashMap<>();

                Map<String, Object> sensorData = new HashMap<>();


                sensorData.put("timestamp", dataObject.getTime());
                sensorData.put("temperatura", dataObject.getTemp());
                sensorData.put("profondità", dataObject.getDepth());
                telemetry.put("payload", sensorData);

                mqttService.sendTelemetryInTopicMqtt("device_events/" + configPojo.getIdLinkedAssetInstanceDevice(), telemetry);
            }

        }



    }






}
