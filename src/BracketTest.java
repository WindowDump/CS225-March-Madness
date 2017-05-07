import java.util.ArrayList;

/**
 * Created by Matthew on 5/1/2017.
 */
public class BracketTest {
    public static void main(String[]args){
        ArrayList<String> array = new ArrayList<String>();
        array.add("Test");
        Bracket bracket1 = new Bracket(array);
        Bracket bracket2 = new Bracket(bracket1);
        bracket1.moveTeamUp(126);

        System.out.println(bracket1.getBracket().get(126));
        System.out.println(bracket1.getBracket().get(62));

        System.out.println(bracket2.getBracket().get(126));
        System.out.println(bracket2.getBracket().get(62));

        System.out.println(-1/2);
    }
}
