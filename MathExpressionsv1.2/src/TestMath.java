/**
 * Main Class
 *
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
            "1+2+3", "x^2 + 1 - sin(pi * 0.3) * 2.123", "1+", "1+cos(1+2)-acos", "0", "1+sin^2"
        };

        Parser parser = new Parser();

        for (String s : expresiones) {
            try {
                parser.evaluate(s);
                
                System.out.println("BIEN   [" + s + "]");
                
            } catch (ParsingException ex) {
                System.out.println(ex.getMessage() + "[" + s + "]");
            }
        }

    }

}
