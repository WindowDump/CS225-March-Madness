import java.util.ArrayList;
import java.io.Serializable; 

/**
 * Bracket Class
 * @author Matt, Dan, Hillary
 * @since 5/1/2017
 */
public class Bracket implements Serializable
{
    private final ArrayList<String> bracket;
    private final transient int[] teamScores = new int[Byte.MAX_VALUE];
    private String playerName;
    private String password;
    public static final long serialVersionUID = 5609181678399742983L;

    /**
     * Constructor using an ArrayList of strings to start
     * @param starting and arraylist containing the 64 teams participating in the tournament
     */
    public Bracket(ArrayList<String> starting){
        bracket = new ArrayList<>(starting);
        while (bracket.size() < Byte.MAX_VALUE) {
            bracket.add(0,""); // FIXME: 4/2/2023 Adding empty strings may unnecessary
        }
    }

    /**
     * Constructor using another Bracket to start
     * @param starting master bracket pre-simulation
     */
    public Bracket(Bracket starting) {
        bracket = new ArrayList<>(starting.getBracket());
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
     * added by dan and matt 5/3
     * Set teamScore for a game
     * @param game, index of the place that will be scored
     * @param score, the amount of points that the team scores
     */
    /* TODO: 4/3/2023
    *   Figure out what to do with this method.
    *   Right now it's not being used for anything since nothing is getting
    *   contents from it. This doesn't mean that it won't be useful in the
    *   future because the game simulation may change, utilizing this method.
    *   I'll leave it here until things are more finalized. */
    public void setTeamScore(int game, int score){
        teamScores[game] = score;
    }
}
