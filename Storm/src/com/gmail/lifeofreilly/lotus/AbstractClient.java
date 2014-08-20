package com.gmail.lifeofreilly.lotus;

/**
 * An Abstract client for retrieving messages that contain hashtags. Can be extended for target social network.
 */
public abstract class AbstractClient implements Runnable {
    private String trackedTerm;
    private String screenName;
    private Long id;
    private MessageData messageData;

    public MessageData getMessageData() {
        return messageData;
    }

    public void setMessageData(final MessageData messageData) {
        this.messageData = messageData;
    }

    public String getTrackedTerm() {
        return trackedTerm;
    }

    public void setTrackedTerm(final String trackedTerm) {
        this.trackedTerm = trackedTerm;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(final String screenName) {
        this.screenName = screenName;
    }

    public Long getID() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Client{" +
                "trackedTerm='" + trackedTerm + '\'' +
                ", screenName='" + screenName + '\'' +
                ", id=" + id +
                '}';
    }
}
