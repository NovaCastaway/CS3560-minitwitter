package twitterdriver;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserView {
    
    private VBox menuBox;
    
    private UserView(UserLeaf user, GroupContainer rootGroup){
        //Create button and textfield to follow user
        TextField userIDText=new TextField();
        userIDText.setPromptText("Enter User ID");
        Button follow = new Button();
        follow.setText("Follow User");
        
        Alert informationAlert=new Alert(Alert.AlertType.INFORMATION);
        
        //Set conditions for if one request to follow a user
        follow.setOnAction((ActionEvent event)-> {
            String UserNeedFollow=userIDText.getText();
            UserLeaf NeedFollow=rootGroup.getUser(UserNeedFollow);
            if(NeedFollow == user){
                informationAlert.setContentText("Unable to follow yourself.");
                informationAlert.showAndWait();
               
            }
            else if(NeedFollow == null){
                informationAlert.setContentText("Unable to find user");
                informationAlert.showAndWait();
            }
            //Only follow user and add to List if not following before and user exist
            else{ 
                NeedFollow.attach(user);
                user.addFollowingList(NeedFollow);
            }
            userIDText.clear();
        });
        //Display following list in a listview
        ListView followlist = new ListView(user.getFollowingList());
        followlist.setPrefHeight(50);
        
        //TextArea and button for posting tweet
        TextArea writeTweet=new TextArea();
        writeTweet.setWrapText(true);
        writeTweet.setPrefHeight(70);
        writeTweet.setPromptText("What's happening?");
        Button postTweet = new Button();
        postTweet.setText("Post Tweet");  
        
        //Post tweet when button is click and display 
        postTweet.setOnAction((ActionEvent event)-> {
            String tweet=writeTweet.getText();
            user.tweetMessage(tweet);
            writeTweet.clear();
        });
        //ListView for newsFeed
        ListView newsFeedList = new ListView(user.getNewsFeedList());
        newsFeedList.setPrefHeight(100);
                
        Label newsFeed=new Label("News Feed");
        Label currentfollow=new Label("Currently Following");
        
        //label for user creation time and last updated of all users
        Label creation=new Label("User's Creation Time: " + user.getCreationTime());
        
        //Place buttons, Listview, label, textfield/area into proper layout
        HBox followUserOption=new HBox(10,userIDText, follow);
        HBox tweetOption=new HBox(10, writeTweet, postTweet);
        tweetOption.setAlignment(Pos.BOTTOM_CENTER);
        
        //Add new User's creation time label
        menuBox=new VBox(10, creation, followUserOption, currentfollow, 
                followlist, tweetOption, newsFeed, newsFeedList);
        
    }
    public VBox getMenuBox(){
        return menuBox;
    }
    public static void openUserUI(UserLeaf user, GroupContainer rootGroup){
        //Open menuBox when called 
        UserViewUI userViewUI=new UserViewUI(user, rootGroup);
        VBox menuBox=userViewUI.getMenuBox();
        
        Scene userScene = new Scene(menuBox, 570, 400);
        
        //Set title for the Window display
        Stage userStage=new Stage();
        userStage.setTitle(user.getID()+ "'s Account");
        userStage.setScene(userScene);
        userStage.show();
    }
}