import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by Artem on 5/2/2017.
 */
//Rodrigo, Hillary 5/3
public class TeamInfo {

    HashMap<String, Team> teams;

    public TeamInfo(){
        teams = new HashMap<>();
        loadFromFile();
    }

   /** Hillary and Rodrigo 5/3
     * This private method will load all the team information from the teamInfo.txt file via a BufferedReader and load each team into
     * the teams HashMap using their name as the key and the actual Team object as the data.
     * -- au: Artem
     */
    private void loadFromFile()
    {
        try
        {
            String line; //To store every lin read so that team can be extracted.
            BufferedReader br = new BufferedReader(new FileReader("teamInfo.txt"));
            /* Rodrich read lines here, turn them into teams and put them in the HashMap( I dont know how tyler is organizing the textfile though) */
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method will take a parameter of a team name and return the Team object corresponding to it.
     * If it is unsuccessful, meaning the team does not exist, it will throw an exception.
     * -- au: Artem
     * @param teamName -- the name of the team to be found
     * @return the Team object for that team
     * @throws Exception in case it's not in there
     */
    public Team getTeam(String teamName) throws Exception{
        try{
            return teams.get(teamName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    /**
     * This will be the method that actually does the work of determining the outcome of the games.
     * It will use the seed/ranking from each team on the bracket and put it into an algorithm to somewhat randomly generate a winner
     * -- au: Artem
     * @param startingBracket -- the bracket to be simulated upon. The master bracket.
     * @return the bracket after it has been populated with the results of the game.
     */
    public Bracket simulate(Bracket startingBracket){
    }




}
