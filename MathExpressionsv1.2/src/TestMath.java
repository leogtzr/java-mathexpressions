/**
 * Main Class
 * @author Leo Gutiérrez Ramírez <leogutierrezramirez@gmail.com>
 */
import mathparser.Parser;
import mathparser.ParsingException;
public class TestMath {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String[] expresiones = {
            "1+!0", "1-2", "1+2-", "1+!(0+2^3)"
        };

        Parser parser = new Parser();

        for (String s : expresiones) {
            try {
                parser.parse(s);
                System.out.println("Value: " + parser.getNumericAnswer());
                
            } catch (ParsingException ex) {
                System.out.println(ex.getMessage() + "[" + s + "]");
            }
        }
    }
}
