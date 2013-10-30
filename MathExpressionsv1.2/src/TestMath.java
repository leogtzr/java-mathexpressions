
/**
 * Main Class Tester
 *
 * @author Leo Gutiérrez Ramírez <leogutierrezramirez@gmail.com>
 */
import java.util.Scanner;
import mathparser.CustomFunction;
import mathparser.Parser;
import mathparser.ParsingException;
import mathparser.SimpleFunction;

public class TestMath {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        scan.useDelimiter("\n");
        String exprStr;

        Parser parser = new Parser();

        SimpleFunction fibFunc = new SimpleFunction("fibonacci") {
            @Override
            public double functionCode(double a) {
                double i = 1.0;
                double j = 0.0;
                for (int k = 1; k <= a; k++) {
                    j += i;
                    i = j - i;
                }
                return j;
            }
        };

        parser.addSimpleFunction("fibonacci", fibFunc);
        parser.addVariable("x", 8);

        while((exprStr = scan.nextLine()) != null) {
            try {
                parser.parse(exprStr);
                double result = parser.getNumericAnswer();
                System.out.println(result);
            } catch(ParsingException ex) {
                System.out.println("Error[" + ex + "]");
            }
        }
        
    }
}
