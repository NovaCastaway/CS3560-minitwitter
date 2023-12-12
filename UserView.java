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
    
    private UserView(LeafPattern user, ContainerGroup rootGroup){
        //Button that follows user
        TextField userIDText=new TextField();
        userIDText.setPromptText("Enter User ID");
        Button follow = new Button();
        follow.setText("Follow User");
        
        Alert informationAlert=new Alert(Alert.AlertType.INFORMATION);
        
        //Follow request
        follow.setOnAction((ActionEvent event)-> {
            String UserNeedFollow=userIDText.getText();
            LeafPattern NeedFollow=rootGroup.getUser(UserNeedFollow);
            if(NeedFollow == user){
                informationAlert.setContentText("Unable to follow yourself.");
                informationAlert.showAndWait();
               
            }
            else if(NeedFollow == null){
                informationAlert.setContentText("Unable to find user");
                informationAlert.showAndWait();
            }
            //User can only follow as long as the user is not being followed
            else{ 
                NeedFollow.attach(user);
                user.addFollowingList(NeedFollow);
            }
            userIDText.clear();
        });
        //Display list
        ListView followlist = new ListView(user.getFollowingList());
        followlist.setPrefHeight(50);
        
        //Posting button
        TextArea writeTweet=new TextArea();
        writeTweet.setWrapText(true);
        writeTweet.setPrefHeight(70);
        writeTweet.setPromptText("What's happening?");
        Button postTweet = new Button();
        postTweet.setText("Post Tweet");  
        
        //Post/Display Tweet 
        postTweet.setOnAction((ActionEvent event)-> {
            String tweet=writeTweet.getText();
            user.tweetMessage(tweet);
            writeTweet.clear();
        });
        //NewsFeed 
        ListView newsFeedList = new ListView(user.getNewsFeedList());
        newsFeedList.setPrefHeight(100);
                
        Label newsFeed=new Label("News Feed");
        Label currentfollow=new Label("Currently Following");
        
        //Last update of users
        Label creation=new Label("User's Creation Time: " + user.getCreationTime());
        
        //Layout
        HBox followUserOption=new HBox(10,userIDText, follow);
        HBox tweetOption=new HBox(10, writeTweet, postTweet);
        tweetOption.setAlignment(Pos.BOTTOM_CENTER);
        
        //Newest User Creation Time
        menuBox=new VBox(10, creation, followUserOption, currentfollow, 
                followlist, tweetOption, newsFeed, newsFeedList);
        
    }
    public VBox getMenuBox(){
        return menuBox;
    }
    public static void openUserUI(UserLeaf user, GroupContainer rootGroup){
        //menu
        UserViewUI userViewUI=new UserViewUI(user, rootGroup);
        VBox menuBox=userViewUI.getMenuBox();
        
        Scene userScene = new Scene(menuBox, 570, 400);

        Stage userStage=new Stage();
        userStage.setTitle(user.getID()+ "'s Account");
        userStage.setScene(userScene);
        userStage.show();
    }
}