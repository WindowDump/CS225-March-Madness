import java.util.ArrayList;

/**
 * Bracket Class
 * Created by Matt and Dan on 5/1/2017.
 */
public class Bracket {
    //Attributes
    ArrayList<String> bracket;
    String name;
    static final int EAST_BRACKET = 63;
    static final int WEST_BRACKET = 79;
    static final int NORTH_BRACKET = 95;
    static final int SOUTH_BRACKET = 111;

    //Constructor
    /**
     *Cosntructor using an ArrayList of strings to start
     */
    public Bracket(ArrayList<String> starting){
        bracket = starting;
        while(bracket.size()<127){
            bracket.add(0,"");
        }
    }

    /**
     * Constructor using another Bracket to start
     */
    public Bracket(Bracket starting){
        bracket = new ArrayList<String>();
        for(int i=0; i<127; i++){
            bracket.add(i,starting.getBracket().get(i));
        }
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
        bracket.set(newPos, bracket.get(position));
    }

    /**
     * removes all future wins of a team, including spot that this is called from
     * @param root, index of the first place that the team gets deselected
     */
    public void resetSubtree(int root){
        //String temp = bracket.get(root);
        if (root==0)
            bracket.set(root,"");
        else {
            int parent = (int) ((root - 1) / 2);
            if (bracket.get(parent).equals(bracket.get(root))) {
                resetSubtree(parent);
            }
            bracket.set(root, "");
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


}
