package twitterdriver;

import java.util.ArrayList;
import java.util.List;

public class SubjectPattern {
    private List<ObserverPattern> followers = new ArrayList<>();

    public void attach(ObserverPattern observer) {
        followers.add(observer);
    }
    //Update all followers of user who posted
    public void updateFollowers(String message) {
        for(ObserverPattern observer : this.followers) {
            observer.update(this, message);
        }
    }

}