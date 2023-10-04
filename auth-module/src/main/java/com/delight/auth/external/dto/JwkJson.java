
package com.delight.auth.external.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
public class JwkJson {
    private String alg;
    private String kid;
    private String kty;
    private String use;
    private String n;
    private String e;

}
