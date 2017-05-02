import java.util.ArrayList;
import java.io.Serializable; 

/**
 * Bracket Class
 * Created by Matt and Dan on 5/1/2017.
 */
//Hillary Ssemakula 5/1
public class Bracket implements Serializable //Hillary: This bracket class is to implement the serializable interface inorder to be serialized
{
    //Attributes
    private ArrayList<String> bracket;
    private String playerName;
    private String password;
    static final int EAST_BRACKET = 3;
    static final int WEST_BRACKET = 4;
    static final int NORTH_BRACKET = 5;
    static final int SOUTH_BRACKET = 6;

    //Constructor
    /**
     *Cosntructor using an ArrayList of strings to start
     */
    public Bracket(ArrayList<String> starting){
        bracket = new ArrayList<String>(starting);
        while(bracket.size()<127){
            bracket.add(0,"");
        }
    }

    /**
     * Constructor using another Bracket to start
     */
    public Bracket(Bracket starting){
        /*bracket = new ArrayList<String>();
        for(int i=0; i<127; i++){
            bracket.add(i,starting.getBracket().get(i));
        }*/
        //code above removed and replaced by matt 5/1
        bracket = new ArrayList<String>(starting.getBracket());
    }

    //Methods
    /**
     * Returns an ArrayList of the bracket
     */
    public ArrayList<String> getBracket(){
        return bracket;
    }

    /**
     * Moves a team up the bracket
     * @param position, the starting position of the team to be moved
     */

    public void moveTeamUp(int position){
        int newPos = (int)((position-1)/2);
        if (bracket.get(newPos).equals(""))
            bracket.set(newPos, bracket.get(position));
        else {
            removeAbove(newPos);
            bracket.set(newPos, bracket.get(position));
        }
    }

    /**
     * added by matt 5/1
     * resets all children of root location except for initail teams at final children
     * special behavior if root = 0; just resets the final 4
     * @param root, everything below and including this is reset
     */
    public void resetSubtree(int root){
        if (root ==0){//special behavior to reset final 4
            for (int i = 0; i < 7; i++) {
                bracket.set(i,"");
            }
        }
        else {
            int child1 = 2 * root + 1;
            int child2 = 2 * root + 2;

            if (child1 < 64) {//child is above round 1
                resetSubtree(child1);
            }
            if (child2 < 64) {
                resetSubtree(child2);
            }
            bracket.set(root, "");
        }
    }

    /**
     * removes all future wins of a team, including spot that this is called from
     * @param child, index of the first place that the team gets deselected
     */
    //public void resetSubtree(int root){
    public void removeAbove(int child){//renamed by matt 5/1
        if (child==0)
            bracket.set(child,"");
        else {
            int parent = (int) ((child - 1) / 2);
            if (bracket.get(parent).equals(bracket.get(child))) {
                removeAbove(parent);
            }
            bracket.set(child, "");
        }
    }

    /**
     * add a value to the bracket arrayList
     * used for creating new brackets
     * @param position, index to add new value
     * @param s, string added to bracket
     */
    private void add(int position, String s){
        bracket.add(position, s);
    }

    /******Hillary Ssemakula 5/1********/
     /* @param String password, the player's password is set to the given parameter */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /*returns the player's name */
    public String getPlayerName()
    {
        return playerName;
    }

    /* Hillary: returns the player's password */
    public String getPassword()
    {
        return password;
    }
    
    /* Hillary: checks whether there are no empty strings in the bracket, i.e. it's completely filled in */
    public boolean isComplete()
    {
        for(String team: bracket){
            if(team.equals("")){ return false; }
        }
        return true;
    }
    
    /* Hillary: checks whether subtree starting at root is complete  INCOMPLETE-MIGHT NEED IMPROVEMENT */
    public boolean isSubtreeComplete(int root)
    {
        int rightChild = 2 * root + 2;
        int leftChild = 2 * root + 1;
        if(bracket.get(root).equals("")) return false;
        else if(rightChild < bracket.size()) return isSubTreeComplete(rightChild); 
        else if(leftChild < bracket.size()) return isSubTreeComplete(rightChild); 
        
        return true;
    }
}

