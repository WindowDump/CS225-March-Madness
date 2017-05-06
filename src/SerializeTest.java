import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/* Hillary Ssemakula: This class tests serialization of a bracket */
public class SerializeTest
{
  public static void main(String[] args)
  {
    ArrayList<String> array = new ArrayList<String>();
    array.add("UCLA");
    array.add("Kentucky");
    array.add("North Carolina");
    array.add("Duke");
    array.add("Indiana");
    array.add("Connecticut");
    array.add("Louisville");
    array.add("Cincinnati");
    array.add("Florida");
    array.add("Michigan State");
    array.add("North Carolina State");
    array.add("Oklahoma State");
    array.add("Oklahoma A&M");
    array.add("Villanova");
    array.add("Arizona");
    array.add("Arkansas");
    array.add("California");
    array.add("CCNY");
    array.add("Georgetown");
    array.add("Holy Cross");
    array.add("La Salle");
    array.add("Loyola Chicago");
    array.add("Marquette");
    array.add("Maryland");
    array.add("Michigan");
    array.add("UNLV");
    array.add("Ohio State");
    array.add("Oregon");
    array.add("Stanford");
    array.add("Syracuse");
    array.add("UTEP");
    array.add("Texas Western");
    array.add("Utah");
    array.add("Wisconsin");
    array.add("Wyoming");
    
    Bracket hillary = new Bracket(array);
    
    FileOutputStream outStream = null;
    ObjectOutputStream out = null;
    try 
    {
      outStream = new FileOutputStream("hillary.ser");
      out = new ObjectOutputStream(outStream);
      out.writeObject(hillary);
      out.close();
    } 
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
}
