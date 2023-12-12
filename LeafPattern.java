package twitterdriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import twitterdriver.ObserverPattern;
import twitterdriver.SubjectPattern;

public class LeafPattern extends SubjectPattern implements CompositePattern, ObserverPattern{

   private String userID;
   private List<LeafPattern> following = new ArrayList<>();
   private ObservableList<LeafPattern> followingList = FXCollections.observableList(following);
   private List<String> myTweets = new ArrayList<>();
   private List<String> newsFeed = new ArrayList<>(Arrays.asList());
   //Update users viewfeed
   private ObservableList<String> newsFeedList = FXCollections.observableList(newsFeed);
   private long createdUser;
   private long lastUpdatedTime=0;
    
   //Set ID of User and created User
    public LeafPattern(String newID) {
        this.userID = newID;
        this.createdUser=System.currentTimeMillis();
    }
    
    @Override
    public String getID() {
        return userID;
    }
    
    @Override
    public String toString() {
        return userID;
    }
    
    //setter and getter method for creationtime for user
    public long getCreationTime(){
        return createdUser;
    }
    public long getLastUpdatedTime(){
        return lastUpdatedTime;
    }

    @Override
    public void accept(UserGroup visitor) {
        visitor.visitUser(this);
    }
    //override observer
    @Override
    public void update(SubjectPattern subject, String tweet) {
        if (subject instanceof LeafPattern) {
            this.newsFeedList.add("-" + ((LeafPattern) subject).getID() + " : " + tweet);
            //Update time for other users
            lastUpdatedTime=System.currentTimeMillis();
            this.newsFeedList.add("Last Updated: " + lastUpdatedTime);
        }
    }
    public ObservableList<LeafPattern> getFollowingList() {
        return followingList;
    }
    //if follow user, add to following list
    public void addFollowingList(LeafPattern user) {
        followingList.add(user);
    }

    public List<String> getMyTweets() {
        return myTweets;
    }

    public ObservableList<String> getNewsFeedList() {
        return newsFeedList;
    }
    
    public void tweetMessage (String tweet){
        myTweets.add(tweet);
        //add tweet to self's newsfeed
        newsFeedList.add("-" + this.userID + " : " + tweet);
        //update last updated time to own's newfeed in real time
        lastUpdatedTime=System.currentTimeMillis();
        //set(index, tweet string)
        this.newsFeedList.add("Last Updated: " + lastUpdatedTime);
        //Also add tweet to other user's (followers) newsfeed
        updateFollowers(tweet);
    }
}