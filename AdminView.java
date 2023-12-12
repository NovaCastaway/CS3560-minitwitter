import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import twitterdriver.Visitor.UpdatedVisitor;
import twitterdriver.Visitor.PositiveVisitor;
import twitterdriver.Visitor.TotalGroupVisitor;
import twitterdriver.Visitor.TotalMessageVisitor;
import twitterdriver.Visitor.TotalUserVisitor;

public class AdminView {

    private static AdminView control;

    public static AdminView getInstance(){
        if (control == null){
            control = new AdminView(){
                return control;
            }
        }
    }
    private HBox menuBox;

    private AdminView(){
        GroupContainer rootGroup = new GroupContainer("Root");

        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);

        TreeItem<CompositeTree> root = new TreeItem<> (rootGroup, new ImageView(rootPic));
        root.setExpanded(true);
        TreeView<CompositeTree> treeView = new TreeView<>(root);

        //New User
        Button adduser = new Button();
        addUser.setText("Add User");
        TextField UserIDText = new TextField();
        UserIDText.setPromptText("Enter User ID");

        //Upon click of adding user
        adduser.setOnAction((ActionEvent event) -> {
            TreeItem<CompositeTree> selectedUser = treeView.getSelectionModel().getSelectedItem();
            String newUserInput= UserIDText.getText();
            //No 
            if(rootGroup.containsUser(newUserInput)){
                informationAlert.setContentText("User is in group");
                informationAlert.showAndWait();
            }
            else{
                //User ID creation value
                UserLeaf temp = new UserLeaf(newUserInput);
                //temp.setCreationTime();
                ((GroupContainer) selectedUser.getValue()).addGroupUsers(temp);
                selectedUser.getChildren().add(new TreeItem<>(temp));
            }
            UserIDText.clear();
        });
        //Group Button 
        Button addgroup = new Button();
        addgroup.setText("Add Group");
        TextField GroupIDText = new TextField();
        GroupIDText.setPromptText("Enter Group ID");
        
        //Group creating button function
        addgroup.setOnAction((ActionEvent event) -> {
            String newGroup= GroupIDText.getText();
            GroupContainer temp=new GroupContainer(newGroup);
            TreeItem<CompositeTree> selectedGroup = treeView.getSelectionModel().getSelectedItem();
            //No duplicate groups
            if(rootGroup.containsGroup(newGroup)) {
                informationAlert.setContentText("This group already exists");
                informationAlert.showAndWait();
            }
            else {
                //set value of when this group ID was created
                temp.setCreationTime();
                ((GroupContainer) selectedGroup.getValue()).addGroupUsers(temp);
                selectedGroup.getChildren().add(new TreeItem<>(temp, new ImageView(rootPic)));
            }
            GroupIDText.clear();
        });
        //UserID Validation
        Button validateIDs=new Button();
        validateIDs.setText("Validate IDs");
        
        //Invalidate ID's
        validateIDs.setOnAction((ActionEvent event) -> {
            String newGroup= GroupIDText.getText();
            
            String newUser= UserIDText.getText();
            
            //Invalid: ID already exist
            if(rootGroup.containsGroup(newGroup) || rootGroup.containsUser(newUser)){
                informationAlert.setContentText("Invalid: ID already exist");
                informationAlert.showAndWait();
            }
            //Invalid: No spaces allowed
            else if(newGroup.contains(" ") || newUser.contains(" ")){
                informationAlert.setContentText("Invalid: No spaces allowed");
                informationAlert.showAndWait();
            }
            //Valid: This is a new ID
            else if(!rootGroup.containsGroup(newGroup) || !rootGroup.containsUser(newUser)){
                informationAlert.setContentText("Valid: This is a new ID");
                informationAlert.showAndWait();
            }
        });
        //Last updated User
        Button LastUpdatedUser=new Button("Last Updated User's ID");
        LastUpdatedUser.setOnAction((ActionEvent event) -> {
            LastUpdatedIDVisitor updatedIDVisitor=new LastUpdatedIDVisitor();
            rootGroup.accept(updatedIDVisitor);
            informationAlert.setContentText("User's last updated: " 
                    + updatedIDVisitor.getLastUpdateUser()); 
            informationAlert.showAndWait();
        });

        //User View
        Button userView = new Button();
        userView.setText("User View");
        
        //Selected User's View
        userView.setOnAction((ActionEvent event) -> {
            TreeItem<CompositeTree> selectedUser = treeView.getSelectionModel().getSelectedItem();
            if (selectedUser.getValue() instanceof UserLeaf){
                UserLeaf userViewUser = (UserLeaf) selectedUser.getValue();
                UserViewUI.openUserUI(userViewUser, rootGroup);
            }
        });
        //Total user button
        Button usertotal = new Button();
        usertotal.setText("Show User Total");
        
        //Call usertotal function when button is clicked and a alert popup
        usertotal.setOnAction((ActionEvent event) -> {
            TotalUserVisitor userTotalVisitor=new TotalUserVisitor();
            rootGroup.accept(userTotalVisitor);
            informationAlert.setContentText("There are " + userTotalVisitor.getUserTotal()
                    +" users total");
            informationAlert.showAndWait();
        });
        //Button for group total
        Button grouptotal = new Button();
        grouptotal.setText("Show Group Total"); 
        
        grouptotal.setOnAction((ActionEvent event) -> {
            TotalGroupVisitor groupTotalVisitor=new TotalGroupVisitor();
            rootGroup.accept(groupTotalVisitor);
            informationAlert.setContentText("There are " + groupTotalVisitor.getGroupTotal()
                    + " groups total");
            informationAlert.showAndWait();
            
        });
        //Total tweets
        Button messagetotal = new Button();
        messagetotal.setText("Show Message Total");
        
        //Visitor class
        messagetotal.setOnAction((ActionEvent event) -> {
            TotalMessageVisitor messageTotalVisitor=new TotalMessageVisitor();
            rootGroup.accept(messageTotalVisitor);
            informationAlert.setContentText("There are " + messageTotalVisitor.getMessageTotal()
                    + " tweets total");
            informationAlert.showAndWait();
        });
        //Formatting/Display of AdminView
        VBox treeBox=new VBox(treeView);
        HBox userBox = new HBox(10, UserIDText, adduser);
        HBox groupBox = new HBox(10, GroupIDText, addgroup);
        HBox UserGroupBox = new HBox(10, usertotal, grouptotal);
        HBox MessagePositiveBox = new HBox(10, messagetotal, positive);
        
        VBox UserButtons=new VBox(10, validateIDs, LastUpdatedUser, userView);
        UserButtons.setAlignment(Pos.CENTER);
        
        VBox topButtons = new VBox(10, userBox, groupBox, UserButtons, UserGroupBox, 
                MessagePositiveBox);
        VBox bottomButtons=new VBox(10, UserGroupBox, MessagePositiveBox);
        VBox allButtons=new VBox(10, topButtons, bottomButtons);
        
        allButtons.setSpacing(170);
        menuBox = new HBox(10, treeBox, allButtons);
        menuBox.setPadding(new Insets(10));

    }
    //Display
    public HBox getAdminPanel() {
            return menuBox;
    }
}
