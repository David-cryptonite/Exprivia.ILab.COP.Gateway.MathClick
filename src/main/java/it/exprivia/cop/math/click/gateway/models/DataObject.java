package it.exprivia.cop.math.click.gateway.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class DataObject {
    private Long time;
    private Double longitude;
    private Double latitude;
    private Double depth;
    private Double temp;
}
