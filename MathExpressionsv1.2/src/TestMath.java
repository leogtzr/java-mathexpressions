/**
 * Main Class Tester
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
        
        Parser parser = new Parser();
        parser.addCustomFunction("max2", new CustomFunction("max2") {
            @Override
            public double functionCode(double a, double b) {
                return (a + b);
            }
        });
        
        parser.addCustomFunction("sumatoria", new CustomFunction("sumatoria") {
            @Override
            public double functionCode(double begin, double end) {
                long sum = 0;
                for(int i = (int)begin; i < (int)end; i++) {
                    sum += i;
                }
                return (double)sum;
            }
        });
        
        parser.addSimpleFunction("fibonacci", new SimpleFunction("fibonacci") {
            @Override
            public double functionCode(double a) {
                double i = 1.0;
                double j = 0.0;
                for(int k = 1; k <= a; k++) {
                    j += i;
                    i = j - i;
                }
                return j;
            }
        });
               
        double ans;
        
        String exprStr;
        while((exprStr = scan.nextLine()) != null) {
            try {
                
                /*if(parser.isExpressionOk(exprStr)) {
                    System.out.println("Expressión correcta ... ");
                    parser.parse(exprStr);
                    ans = parser.getNumericAnswer();
                    System.out.println("Result[" + ans + "]");
                } else {
                    System.out.println("La expresión no es válida.");
                }*/
                
                parser.parse(exprStr);
                ans = parser.getNumericAnswer();
                System.out.println("Result[" + ans + "]");
                
            } catch(ParsingException ex) {
                System.out.println(ex);
            }
        }
    }
}
