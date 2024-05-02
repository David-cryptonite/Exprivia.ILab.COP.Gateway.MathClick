package it.exprivia.cop.math.click.gateway.models.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfigPojo {

    private String idLinkedAssetInstanceTracking;

    private String idLinkedAssetInstanceDevice;
}
