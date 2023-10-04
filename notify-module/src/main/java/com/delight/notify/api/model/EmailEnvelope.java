package com.delight.notify.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class EmailEnvelope {
    private String[] to;
    private String[] bcc;
    private String[] cc;
    private String subject;
    private String body;
    private String from;
    private String fromName;
    private boolean html = true;
}
