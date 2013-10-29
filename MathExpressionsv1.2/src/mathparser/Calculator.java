package mathparser;

import java.util.HashMap;

/**
 * @author Leo Gutiérrez Ramírez | leogutierrezramirez@gmail.com
 */
public class Calculator {
    
    private final String expression;
    private HashMap<String, CustomFunction> customFunctions = null;
    
    public static class CalcBuilder {
        // Required parámeters:
        private final String expression;
        
        // Optional parameters:
        private HashMap<String, CustomFunction> customFunctions = null;
        
        // Constructor:
        public CalcBuilder(String expression) {
            this.expression = expression;
            this.customFunctions = new HashMap<>();
        }
        
        public CalcBuilder addCustomFunction(String name, CustomFunction customFunction) {
            this.customFunctions.put(name, customFunction);
            return this;
        }
        
        public Calculator build() {
            return null;
        }
        
    }
    
    private Calculator(CalcBuilder builder) {
        this.expression = builder.expression;
        this.customFunctions = builder.customFunctions;
    }
    
}
