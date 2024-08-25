package com.function;

public class UpdateRequest {
    private long publisherId;
    private String oldConsumerQueueName;
    private String oldEventType;
    private String newConsumerQueueName;
    private String newEventType;

    // Getters and setters
    public long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(long publisherId) {
        this.publisherId = publisherId;
    }

    public String getOldConsumerQueueName() {
        return oldConsumerQueueName;
    }

    public void setOldConsumerQueueName(String oldConsumerQueueName) {
        this.oldConsumerQueueName = oldConsumerQueueName;
    }

    public String getOldEventType() {
        return oldEventType;
    }

    public void setOldEventType(String oldEventType) {
        this.oldEventType = oldEventType;
    }

    public String getNewConsumerQueueName() {
        return newConsumerQueueName;
    }

    public void setNewConsumerQueueName(String newConsumerQueueName) {
        this.newConsumerQueueName = newConsumerQueueName;
    }

    public String getNewEventType() {
        return newEventType;
    }

    public void setNewEventType(String newEventType) {
        this.newEventType = newEventType;
    }

    // Validation method
    public boolean isValid() {
        return oldConsumerQueueName != null && !oldConsumerQueueName.isEmpty() && 
               oldEventType != null && !oldEventType.isEmpty() &&
               newConsumerQueueName != null && !newConsumerQueueName.isEmpty() &&
               newEventType != null && !newEventType.isEmpty() &&
               publisherId > 0;
    }
}
