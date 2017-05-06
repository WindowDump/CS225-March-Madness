import java.io.FileInputStream;
import java.io.ObjectInputStream;

/*Hillary Ssemakula: This class tests deserialization of a file named hillary.ser */
public class DeserializeTest
{
  public static void main(String[] args)
  {
    Bracket hillary = null;
    FileInputStream inStream = null;
    ObjectInputStream in = null;
    try 
    {
      inStream = new FileInputStream("hillary.ser");
      in = new ObjectInputStream(inStream);
      hillary = (Bracket) in.readObject();
      in.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    for(String team: hillary.getBracket()) { System.out.println(team); };
  }
}
