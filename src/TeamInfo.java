import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by Artem on 5/2/2017.
 */
public class TeamInfo{
    HashMap<String, Team> teams;

    public TeamInfo() throws IOException{
        teams = new HashMap<>();
        loadFromFile();
    }

    /**
     * This private method will load all the team information from the teamInfo.txt file via a BufferedReader and load each team into
     * the teams HashMap using their name as the key and the actual Team object as the data.
     * -- au: Artem, Rodrigo
     */
    private void loadFromFile(){
     
     String name;
     String info;
     int ranking;
     
     try{
      BufferedReader br = new BufferedReader(new FileReader("teaminfo.txt"));

      while((name = br.readLine()) != null){
       info = br.readLine();
       ranking = Integer.parseInt(br.readLine());
       Team newTeam = new Team(name, info, ranking); //creates team with info
                
       br.readLine();   //gets rid of empty line between team infos
       
       teams.put(newTeam.getName(), newTeam);   //map team name with respective team object
      }
     }
     catch(IOException ioe){
      System.out.println("File Not Found");
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
    public Team getTeam(String teamName){
        return teams.get(teamName);
    }

    /**
     * updated by Dan and Matt 5/3 using Artem's method of score generation
     *
     * This will be the method that actually does the work of determining the outcome of the games.
     * It will use the seed/ranking from each team on the bracket and put it into an algorithm to somewhat randomly generate a winner
     * -- au: Artem
     * @param startingBracket -- the bracket to be simulated upon. The master bracket
     */
    public void simulate(Bracket startingBracket){
        for (int i = 62; i > 0; i--) {
        /* The equation for score that I settled on is this:
         * (Random int 75-135) * (1 - 0.02 * seed ranking)
         * This way, the multiplier would be between 0.68 and 0.98. Multiply that by 75-135, and you get a reasonable score with room for chance to prevail for lower teams. */
          
            int index1 = 2*i+1;
            int index2 = 2*i+2;

            Team team1 = teams.get(startingBracket.getBracket().get(index1));
            Team team2 = teams.get(startingBracket.getBracket().get(index2));

            int score1 = 0;
            int score2 = 0;
            while(score1==score2) {
                score1 = (int) (((Math.random() * 136) + 75) * (1 - (team1.getRanking() * 0.02)));
                score2 = (int) (((Math.random() * 136) + 75) * (1 - (team2.getRanking() * 0.02)));
            }

            startingBracket.setTeamScore(index1, score1);
            startingBracket.setTeamScore(index2, score2);

            if(score1>score2)
                startingBracket.moveTeamUp(index1);
            else
                startingBracket.moveTeamUp(index2);
        }

    }
}

