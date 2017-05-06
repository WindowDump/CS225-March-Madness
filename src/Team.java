/**
 * 
 * @author Shivanie
 * comments by Tyler
 * @description 
 *       Team class holds all the information for individual teams. Their name, info, and ranking.
 *       
 */

public class Team{
  
  private String name;
  private String info;
  private int ranking;
  /*
   * can be implemented if we have time.
   * public double defense;
   * public double offense;
   */ 
  
  /**
   * Constructor
   * @param name 
   *        The name of the team
   * @param info
   * 		A short description of the team
   * @param ranking
   * 		The ranking in the team region from 1 to 16
   */
  public Team(String name, String info, int ranking){
    this.name = name;
    this.info = info;
    this.ranking = ranking;
  }
    
  /**
   * 
   * @return name
   */
  public String getName(){
    return name;
  }
  
  /**
   * 
   * @return info
   */
  public String getInfo(){
    return info;
  }
  
  /**
   * 
   * @return ranking
   */
  public int getRanking(){
    return ranking;
  }
  /*public double getOffense(){
    return offense;
  }
  public double getDefense(){
    return defense;
  }
  */
  
  /**
   * 
   * @param info 
   * 		The short description of the team
   */
  public void setInfo(String info){
    this.info = info;
  }
  
  /**
   * 
   * @param ranking
   * 		The ranking from 1 to 16
   */
  public void setRanking(int ranking){
    this.ranking = ranking;
  }
}