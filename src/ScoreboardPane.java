import java.util.HashMap;
import java.util.Map;

public class ScoreboardPane {
	
	private static Map<String, Integer> scores = new HashMap<String, Integer>();
	private static final int MAX_PLAYER_NUMBER = 16;
	
	public ScoreboardPane() {
		
	}
	
	public void addPlayer(String name, int score) {
		try {
			if (scores == null) {
				scores = new HashMap<String, Integer>();
			}
			//only allow to update the existing player score or add new player if there is under 16 players
			if (scores.get(name) != null || scores.size() < MAX_PLAYER_NUMBER) {
				scores.put(name, score);
			} 
	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	public void clearPlayers() {
		scores = new HashMap<String, Integer>();
	}
}
