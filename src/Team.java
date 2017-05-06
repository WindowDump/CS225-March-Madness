public class Team{
  
  private String name;
  private String info;
  private int ranking;
  /*
   * can be implemented if we have time.
   * public double defense;
   * public double offense;
   */ 
  
  
  public Team(String name, String info, int ranking){
    this.name = name;
    this.info = info;
    this.ranking = ranking;
  }
    
  
  public String getName(){
    return name;
  }
  public String getInfo(){
    return info;
  }
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
  public void setInfo(String info){
    this.info = info;
  }
  public void setRanking(int ranking){
    this.ranking = ranking;
  }
}
