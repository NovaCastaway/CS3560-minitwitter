package twitterdriver;

import java.util.ArrayList;
import java.util.List;

import twitterdriver.LeafPattern;

//Accepting visitor from visitor pattern
public class ContainerGroup implements CompositePattern{
    
    private List<CompositePattern> groupUsers= new ArrayList<>();
    private String groupID;
    private Long createdGroup;
    
    //verify if ID was used already
    public ContainerGroup(String newID) {
        this.groupID = newID;
        this.createdGroup=System.currentTimeMillis();
    }
    
    @Override
    public String getID() {
        return this.groupID;
    }
    public List<CompositePattern> getGroupUsers(){
        return groupUsers;
    }

    @Override
    public String toString() {
        return groupID;
    }
    //get/set
    public long getCreationTime(){
        return createdGroup;
    }
    public void setCreationTime(){
        createdGroup=System.currentTimeMillis();
    }
    
    @Override
    public void accept(UserGroup visitor) {
        visitor.visitGroup(this);
        for(CompositePattern members : groupUsers) {
            if (members instanceof UserLeaf) {
                members.accept(visitor);
            } else if (members instanceof ContainerGroup) {
                members.accept(visitor);
            }
        }
    }
    //Display group
    public void addGroupUsers(CompositePattern newGroup){
        this.groupUsers.add(newGroup);
    }
    //Verify user ID
    public Boolean containsUser(String UserID){
        for (CompositePattern members : groupUsers) {
            if (members instanceof LeafPattern) {
                if (members.getID().equals(UserID)) {
                    return true;
                }
            }
            else if (members instanceof ContainerGroup) {
                if (((ContainerGroup) members).containsUser(UserID)) {
                    return true;
                }
            }
        }
        return false;
    }
    //Does the group already have the ID/name
    public Boolean containsGroup(String memberID){
        for (CompositePattern members : groupUsers) {
            if (members instanceof LeafPattern) {
                continue;
            }
            //check group members
            else if (members instanceof ContainerGroup) {
                if (members.getID().equals(memberID)){
                    return true;
                }
                //check group ID's
                else {
                    if(((ContainerGroup) members).containsGroup(memberID)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //group x3
    public LeafPattern getUser(String userID){
        for (CompositePattern members : groupUsers) {
            if (members instanceof LeafPattern) {
                if (members.getID().equals(userID)){
                    return (UserLeaf) members;
                }
            }
            else if (members instanceof ContainerGroup) {
                // Check if user exist through iteration
                if (((ContainerGroup) members).containsUser(userID)) {
                    return ((ContainerGroup) members).getUser(userID);
                }
            }
        }
        return null;
    } 
}