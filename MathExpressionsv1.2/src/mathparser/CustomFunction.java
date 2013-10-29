package mathparser;

/**
 * @author Leo Gutiérrez Ramírez | leogutierrezramirez@gmail.com
 */
public abstract class CustomFunction {
    
    final String name;

    public CustomFunction(String name) {
        this.name = name;
    }
    
    public abstract double functionCode(double a, double b);

    @Override
    public String toString() {
        return "CustomFunction{" + "name=" + name + '}';
    }
    
}
