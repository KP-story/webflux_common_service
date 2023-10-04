package com.delight.notify.service.provider.notification;

import com.delight.notify.service.provider.RemoteResult;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.TopicManagementResponse;

import java.util.List;
import java.util.stream.Collectors;

public class ResultMapper {
    public static RemoteResult map(TopicManagementResponse topicManagementResponse) {
        RemoteResult notifyResult = new RemoteResult();
        if (topicManagementResponse.getFailureCount() > 0) {
            notifyResult.setSuccess(false);
            notifyResult.setReason(topicManagementResponse.getErrors().get(0).getReason());
        } else {
            notifyResult.setSuccess(true);
        }
        return notifyResult;
    }

    public static RemoteResult map(Exception e) {
        RemoteResult notifyResult = new RemoteResult();
        notifyResult.setSuccess(false);
        notifyResult.setException(e);
        notifyResult.setReason(e.getMessage());
        return notifyResult;
    }

    public static List<RemoteResult> map(BatchResponse batchResponse) {
        return batchResponse.getResponses().stream().map(sendResponse -> {
            RemoteResult notifyResult = new RemoteResult();
            notifyResult.setException(sendResponse.getException());
            notifyResult.setSuccess(sendResponse.isSuccessful());
            return notifyResult;
        }).collect(Collectors.toList());

    }

}
