package twitterdriver;

//Observers are the users
public interface ObserverPattern {
    
    public void update(SubjectPattern subject, String message);
}