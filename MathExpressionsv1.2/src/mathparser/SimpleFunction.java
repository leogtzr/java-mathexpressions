package mathparser;

/**
 * SimpleFunction.
 * @author Leo Gutiérrez Ramírez | leogutierrezramirez@gmail.com
 */
public abstract class SimpleFunction {
    
    final String name;

    public SimpleFunction(String name) {
        this.name = name;
    }
    
    public abstract double functionCode(double a);

    @Override
    public String toString() {
        return "SimpleFunction{" + "name=" + name + '}';
    }
    
}
