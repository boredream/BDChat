package com.boredream.bdchat.entity.event;

public class GetContactsCompleteEvent {

    public GetContactsCompleteEvent(boolean success) {
        this.success = success;
    }

    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
