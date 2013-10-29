/**
 * Main Class
 * @author Leo Gutiérrez Ramírez <leogutierrezramirez@gmail.com>
 */
import java.util.Scanner;
import mathparser.CustomFunction;
import mathparser.Parser;
import mathparser.ParsingException;
public class TestMath {

    /**
     * @param args the command line arguments
     */
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
