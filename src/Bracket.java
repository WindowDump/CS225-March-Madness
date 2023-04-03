import java.util.ArrayList;
import java.io.Serializable; 

/**
 * Bracket Class
 * @author Matt, Dan, Hillary
 * @since 5/1/2017
 */
public class Bracket implements Serializable
{
    private ArrayList<String> bracket;
    private transient int[] teamScores = new int[127];
    private String playerName;
    private String password;
    public static final long serialVersionUID = 5609181678399742983L;

    /**
     * Constructor using an ArrayList of strings to start
     * @param starting and arraylist containing the 64 teams participating in the tournament
     */
    public Bracket(ArrayList<String> starting){
        bracket = new ArrayList<>(starting);
        while (bracket.size() < 127) { // TODO: 4/2/2023 Replace magic number with constant
            bracket.add(0,""); // FIXME: 4/2/2023 Adding empty strings may unnecessary
        }
    }

    /**
     * Constructor using another Bracket to start
     * @param starting master bracket pre-simulation
     */
    public Bracket(Bracket starting) {
        bracket = new ArrayList<String>(starting.getBracket());
    }

    /**
     * added by matt 5/2
     * Constructor that creates a new bracket with a users name
     * @param starting master bracket pre-simulation
     * @param user name of the new bracket owner
     */
    public Bracket(Bracket starting, String user) {
        bracket = new ArrayList<>(starting.getBracket());
        playerName = user;
    }

    /**
     * Returns an ArrayList of the bracket
     */
    public ArrayList<String> getBracket(){
        return bracket;
    }

    /**
     * Moves a team up the bracket
     * updated by matt 5/7, now removesAbove anytime the above position is not equal to the clicked one
     * @param position, the starting position of the team to be moved
     */
    public void moveTeamUp(int position) {
        int newPos = ((position - 1) / 2);
        if (!bracket.get(position).equals(bracket.get(newPos))) {
            bracket.set(newPos, bracket.get(position));
        }
    }

    /**
     * added by matt 5/1
     * resets all children of root location except for initail teams at final children
     * special behavior if root = 0; just resets the final 4
     * @param root everything below and including this is reset
     */
    public void resetSubtree(int root) {
        if (root == 0) {//special behavior to reset final 4
            for (int i = 0; i < 7; i++) {
                bracket.set(i, "");
            }
        } else {
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
     * @param child index of the first place that the team gets deselected
     */
    //public void resetSubtree(int root){
    public void removeAbove(int child) {//renamed by matt 5/1
        if (child == 0)
            bracket.set(child, "");
        else {
            int parent = ((child - 1) / 2);
            if (bracket.get(parent).equals(bracket.get(child))) {
                removeAbove(parent);
            }
            bracket.set(child, "");
        }
    }

    /**
     * add a value to the bracket arrayList
     * used for creating new brackets
     * @param position index to add new value
     * @param s string added to bracket
     */
    private void add(int position, String s){ // TODO: 4/2/2023 Unused method. Maybe remove it.
        bracket.add(position, s);
    }

    /** 
     * Hillary Ssemakula:
     * set player's password to string parameter 
     * @param password a String
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

      /** 
        * Hillary: 
        * returns the name of the player
        * @return String
        */
    public String getPlayerName()
    {
        return playerName;
    }
    
      /** 
        * Hillary:
        * returns the player's password
        * @return String
        */
    public String getPassword()
    {
        return password;
    }
    
      /** 
        * Hillary:
        * returns true or false depending on whether there are any empty slots on the bracket.
        * If a position has an empty string then the advancing team has not been chosen for that spot and the whole bracket is not complete.
        * @return boolean.
        */
      public boolean isComplete() {
          for (String team : bracket) {
              if (team.equals("")) {
                  return false;
              }
          }
          return true;
      }

    // TODO: 4/2/2023 Return value in doc should tell more
    /** 
      * Hillary:
      * returns true or false depending on whether there are any empty slots in the bracket from a given point all the way to the starting 64 teams.
      * If the root itself is empty return false. otherwise the method is recursively applied to the left and right subtrees of the root.
      * @param root the int index of the root
      * @return boolean
d    *update by matt and hillary 5/2 */
    public boolean isSubtreeComplete(int root)
    {
        if(bracket.get(root).equals(""))
            return false;
        int rightChild = 2 * root + 2;
        int leftChild = 2 * root + 1;
        if(leftChild< bracket.size() && rightChild<bracket.size())
            return isSubtreeComplete(leftChild) && isSubtreeComplete(rightChild);
        return true;
    }

    /**
     * Matt 5/2
     * Scores the bracket by assigning points of each correct winner
     * number of points is based on round
     * @param master the master bracket of true winners to which all brackets are compared
     */
    public int scoreBracket(Bracket master) {
        int score = 0;
        if (bracket.get(0).equals(master.getBracket().get(0)))//finals
            score += 32;
        for (int i = 1; i < 3; i++) {
            if (bracket.get(i).equals(master.getBracket().get(i)))//semi
                score += 16;
        }
        for (int i = 3; i < 7; i++) {
            if (bracket.get(i).equals(master.getBracket().get(i)))//quarters
                score += 8;
        }
        for (int i = 7; i < 15; i++) {
            if (bracket.get(i).equals(master.getBracket().get(i)))//sweet 16
                score += 4;
        }
        for (int i = 15; i < 31; i++) {
            if (bracket.get(i).equals(master.getBracket().get(i)))//round of 32
                score += 2;
        }
        for (int i = 31; i < 63; i++) {
            if (bracket.get(i).equals(master.getBracket().get(i)))//round of 64
                score += 1;
        }
        return score;
    }

    /**
     * added by matt 5/2
     * sets the playerName for the bracket
     * @param user name of the player
     */
    public void setPlayerName(String user){
        playerName = user;
    }
    /**
     * added by dan and matt 5/3
     * Set teamScore for a game
     * @param game, index of the place that will be scored
     * @param score, the amount of points that the team scores
     */
    public void setTeamScore(int game, int score){
        teamScores[game] = score;
    }
}
