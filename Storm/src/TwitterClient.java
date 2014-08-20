package com.gmail.lifeofreilly.lotus;

import org.apache.log4j.Logger;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.TwitterFactory;

/**
 * Utilizes the Twitter Streaming API to collect messages.
 */
class TwitterClient extends AbstractClient {

    private final static Logger log = Logger.getLogger(TwitterClient.class);

    /**
     * Constructs a Twitter Client using the supplied MessageData object and tracked term.
     *
     * @param messageData the data structure for the Twitter data.
     * @param trackedTerm the term to track on Twitter.
     */
    public TwitterClient(final MessageData messageData, final String trackedTerm) {
        this.setMessageData(messageData);
        this.setTrackedTerm(trackedTerm);

        try {
            TwitterFactory twitter = new TwitterFactory();
            this.setScreenName(twitter.getInstance().getScreenName());
            this.setId(twitter.getInstance().getId());
        } catch (TwitterException ex) {
            log.fatal("Exiting. An Exception occurred while establishing twitter client: ", ex);
            System.exit(1);
        }
    }

    @Override
    public void run() {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new TwitterListener());
        twitterStream.filter(getFilterQuery());
        log.info("Start listening to the Twitter stream.");
    }

    private FilterQuery getFilterQuery() {
        FilterQuery filterQuery = new FilterQuery();
        String keywords[] = {this.getTrackedTerm()};
        filterQuery.track(keywords);
        return filterQuery;
    }

    private class TwitterListener implements StatusListener {

        @Override
        public void onStatus(final Status status) {
            log.debug("Received onStatus: " + status.getText());
            synchronized (TwitterClient.this.getMessageData()) {
                TwitterClient.this.getMessageData().addMessage(status.getText());
                TwitterClient.this.getMessageData().notifyAll();
            }
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            log.info("Received a status deletion notice id:" + statusDeletionNotice.getStatusId());
        }

        @Override
        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            log.info("Received track limitation notice:" + numberOfLimitedStatuses);
        }

        @Override
        public void onScrubGeo(long userId, long upToStatusId) {
            log.info("Received scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
        }

        @Override
        public void onStallWarning(StallWarning warning) {
            log.info("Received stall warning:" + warning);
        }

        @Override
        public void onException(Exception ex) {
            log.fatal("Received exceptions. Exiting for twitter api safety. onException: ", ex);
            System.exit(1);
        }
    }

}
