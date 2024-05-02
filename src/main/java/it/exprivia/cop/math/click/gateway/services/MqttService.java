package it.exprivia.cop.math.click.gateway.services;

import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class MqttService {

    @Value("${clientIdMqtt}")
    private String clientId;
    @Value("${mqttSourceUrlConfiguration}")
    private String brokerUrl;

    @Value("${usernameMqttClient}")
    private String usernameMqttClient;

    @Value("${passwordMqttClient}")
    private String passwordMqttClient;

    private MqttClient client;

    private Gson gson = new Gson();


    @Value("${qosLevel}")
    private int qos;



    @Async
    public void sendTelemetryInTopicMqtt(String nameTopic, Map<String, Object> data) {
        try {


            byte[] payload = gson.toJson(data).getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(payload);
            message.setQos(qos);
            this.client.publish(nameTopic, message);
        }catch (Exception e) {

        }
    }

    @PostConstruct
    private void connectEmqx()  {
        try {
            this.client = new MqttClient(brokerUrl, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(usernameMqttClient);
            connOpts.setPassword(passwordMqttClient.toCharArray());
            connOpts.setCleanSession(true);
            this.client.connect(connOpts);
        }catch (Exception e) {

        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            this.client.disconnect();
            this.client.close();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

}