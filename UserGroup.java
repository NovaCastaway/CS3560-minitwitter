package twitterdriver;

public interface UserGroup {
    public void visitUser(LeafPattern user);
    
    public void visitGroup(ContainerGroup group);
    
}