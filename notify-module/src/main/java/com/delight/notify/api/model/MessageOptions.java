package com.delight.notify.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class MessageOptions {
    private boolean contentAvailable;
    private boolean mutableContent = true;
    private String threadId;
}
