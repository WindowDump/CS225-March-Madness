import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * Created by Artem on 5/2/2017.
 */
public class TournamentInfo {//renamed from teamInfo by matt 5/4
    HashMap<String, Team> teams;

    public TournamentInfo() throws IOException {
        teams = new HashMap<>();
        loadFromFile();
    }

    /**
     * This private method will load all the team information from the teamInfo.txt file via a BufferedReader and load each team into
     * the teams HashMap using their name as the key and the actual Team object as the data.
     *
     * @authors Artem, Rodrigo
     */
    private void loadFromFile() throws IOException {

        FileInputStream inFS = null; //added KF
        Scanner scnr;
        String name;
        String nickname;
        String info;
        int ranking;
        double offensivePPG;
        double defensivePPG;
        Team newTeam;

        try{
            //InputStream u = getClass().getResourceAsStream("teamInfo.txt");
            inFS = new FileInputStream("teamInfo.txt"); // added by KF
            //BufferedReader br = new BufferedReader(new InputStreamReader(u));
            scnr = new Scanner(inFS);
            //while((name = br.readLine()) != null){
            while (scnr.hasNextLine()) {

                name = scnr.nextLine();
                nickname = scnr.nextLine();
                info = scnr.nextLine();
                ranking = Integer.parseInt(scnr.nextLine());
                offensivePPG = Double.parseDouble(scnr.nextLine());
                defensivePPG = Double.parseDouble(scnr.nextLine());

                newTeam = new Team(name, nickname, info, ranking, offensivePPG, defensivePPG); //creates team with info

                teams.put(newTeam.getName(), newTeam);   //map team name with respective team object


                scnr.nextLine();   //gets rid of empty line between team infos

            }
        } catch (NoSuchElementException ignored) {
        } finally {
            if (inFS != null) {
                inFS.close();
            }
        }
    }

    /**
     * This method will take a parameter of a team name and return the Team object corresponding to it.
     * If it is unsuccessful, meaning the team does not exist, it will throw an exception.
     *
     * @param teamName -- the name of the team to be found
     * @return the Team object for that team
     * @throws Exception in case it's not in there
     * @authors Artem
     */
    public Team getTeam(String teamName) {
        return teams.get(teamName);
    }

    /**
     * This will be the method that actually does the work of determining the outcome of the games.
     * It will use the seed/ranking from each team on the bracket and put it into an algorithm to somewhat randomly generate a winner
     *
     * @param startingBracket -- the bracket to be simulated upon. The master bracket
     * @authors Artem, Dan, Matt
     */
    public void simulate(Bracket startingBracket) {
        for (int i = 62; i >= 0; i--) {
            /* The equation for score that I settled on is this:
             * (Random int 75-135) * (1 - 0.02 * seed ranking)
             * This way, the multiplier would be between 0.68 and 0.98. Multiply that by 75-135, and you get a reasonable score with room for chance to prevail for lower teams. */

            int index1 = 2 * i + 1;
            int index2 = 2 * i + 2;

            Team team1 = teams.get(startingBracket.getBracket().get(index1));
            Team team2 = teams.get(startingBracket.getBracket().get(index2));

            int score1 = 0;
            int score2 = 0;
            while (score1 == score2) {
                score1 = (int) (((Math.random() * 136) + 75) * (1 - (team1.getRanking() * 0.02)));
                score2 = (int) (((Math.random() * 136) + 75) * (1 - (team2.getRanking() * 0.02)));
            }

            startingBracket.setTeamScore(index1, score1);
            startingBracket.setTeamScore(index2, score2);

            if (score1 > score2)
                startingBracket.moveTeamUp(index1);
            else
                startingBracket.moveTeamUp(index2);
        }

    }



    /**
     * reads Strings from initialMatches.txt into an ArrayList in order to construct the starting bracket
     *
     * @return ArrayList of Strings
     * @authors Matt, Artem
     */
    public ArrayList<String> loadStartingBracket() throws IOException {
        String name;
        ArrayList<String> starting = new ArrayList<String>();

        InputStream u = getClass().getResourceAsStream("initialMatches.txt");
        if (u == null) {
            throw new IOException("initialMatches.txt not found");
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(u));

        while ((name = br.readLine()) != null) {
            starting.add(name);
        }

        br.close();
        return starting;

    }
}
