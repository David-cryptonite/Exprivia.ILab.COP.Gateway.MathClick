package it.exprivia.cop.math.click.gateway.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class DataObject {
    private long time;
    private double longitude;
    private double latitude;
    private double depth;
    private double temp;
}
