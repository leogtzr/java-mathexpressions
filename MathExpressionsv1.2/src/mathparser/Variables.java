package mathparser;

import java.util.ArrayList;

/**
 * Contiene la lista de variables definidas por el usuario.
 * @author Leo Gutiérrez Ramírez. leogutierrezramirez@gmail.com
 * <br/>
 * @author <a href="mailto:leogutierrezramirez@gmail.com"><b>Leo Gutiérrez R.</b></a>
 */
public final class Variables {

    /**
     * Contiene la lisa de variables definidas por el usuario.
     */
    private ArrayList < Variable > varList;

    /** Construye un <b>Variables</b> inicializando la lista de variables
     * definidas por el usuario.
     */
    public Variables() {
        varList = new ArrayList<>();
    }

    /**
     * Devuelve la lista de variables definidas por el usuario.
     * @return la lista de variables definidas por el usuario.
     */
    public ArrayList<Variable> getVarList() {
        return varList;
    }

    /**
     * Devuelve el índice en la lista del nombre de la variable dada 
     *  como parámetro.
     * @param name El nombre de la variable a encontrar.
     * @return índice en la lista de variables.
     * @link popis
     */
    public int getIdVar(String name) {
        for (int i = 0; i < varList.size(); i++) {
            if (name.equalsIgnoreCase(varList.get(i).getVarName())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Determina si el String especificado forma parte de la lista de
     * variables.
     * @param varName el String especificado a verificar.
     * @return un booleano, true si existe, false sino existe.
     */
    public boolean existVar(final String varName) {
        return (getIdVar(varName) != -1);
    }

    /**
     * Devuelve el valor numérico de una varible.
     * @param name el nombre de la variable a obtener su valor.
     * @return el valor numérico de la variable.
     */
    public double getValueVar(String name) {
        int id = getIdVar(name);
        if (id != -1) {
            return varList.get(id).getValue();
        }
        return 0.0;
    }

    /**
     * Elimina una variable de la lista.
     * @param varName el String de la variable a eliminar.
     * @return un booleano true si se eliminó, false sino se eliminó.
     */
    public boolean delVar(final String varName) {
        int id = getIdVar(varName);
        if (id != -1) {
            varList.remove(id);
            return true;
        }
        return false;
    }

    /**
     * Agrega una variable a lista.
     * @param varName el nombre de la variable a agregar.
     * @param value el valor numérico de la variable a agregar.
     * @return un booleano, true si se agregó, false sino se agregó.
     */
    public boolean addVar(final String varName, double value) {
        Variable newVar = new Variable(varName, value);

        int index = getIdVar(varName);
        if (index == -1) {
            varList.add(newVar);
        } else {
            varList.set(index, newVar);
        }
        return true;
    }

    /**
     * Especifica si una variable será constante.
     * @param varName El nombre de la variable a modificar.
     * @param value true para constante, false (por defecto) para variables.
     * @return Un booleano, trye si se pudo modificar, false sino se pudo.
     */
    public boolean setConstant(final String varName, boolean value) {
        int index = getIdVar(varName);
        if (index != -1) {
            varList.get(index).setIsConstant(value);
            return true;
        }
        return false;
    }

    /**
     * Determina si una variable es constante.
     * @param varName el nombre de la variable a comprobar.
     * @return un booleano, true si se pudo comprobar, false sino.
     */
    public boolean isConstant(final String varName) {
        int index = getIdVar(varName);
        if (index != -1) {
            return varList.get(index).isConstant();
        }
        return false;
    }

    /**
     * Especifica una descripción para una variable.
     * @param varName el nombre de la variable a modificar.
     * @param descripcion la descripción a especificar en la variable.
     * @return true si se pudo modificar, false sino.
     */
    public boolean setDescripcion(final String varName, final String descripcion) {
        int index = getIdVar(varName);
        if (index != -1) {
            varList.get(index).setDescripcion(descripcion);
            return true;
        }
        return false;
    }

    /**
     * Contiene la estructura de una variable, identificador
     * si es constante o no, descripción y su valor numérico.
     * @author Leo Gutiérrez Ramírez | leogutierrezramirez@gmail.com
     */
    private static final class Variable {

        private final String var_name;
        private boolean isConstant;
        private String descripcion;
        private double value;

        /**
         * Construye una nueva variable.
         * @param var_name el nombre de la variable.
         * @param isConstant si es una constante (true) o no (false).
         * @param descripcion la descripción de la variable.
         * @param value el valor numérico de la variable.
         */
        public Variable(String var_name, boolean isConstant, String descripcion, double value) {
            this.var_name = var_name;
            this.isConstant = isConstant;
            this.descripcion = descripcion;
            this.value = value;
        }

        /**
         * Construye una variable con lo más básico, su identificador
         * y su valor.
         * @param var_name el nombre de la variable.
         * @param value el valor numérico de la variable.
         */
        public Variable(String var_name, double value) {
            this.var_name = var_name;
            this.value = value;
        }

        /**
         * Devuelve el nombre de la variable.
         * @return String con el nombre de la variable.
         */
        public String getVarName() {
            return var_name;
        }

        /**
         * Determina si la variable es constabte.
         * @return true si es constante, false sino.
         */
        public boolean isConstant() {
            return isConstant;
        }

        public void setIsConstant(boolean isConstant) {
            this.isConstant = isConstant;
        }

        /**
         * Obtiene la descripción de la variable.
         * @return un String con la descripción de la variable.
         */
        public String getDescripcion() {
            return descripcion;
        }

        /**
         * Especifica una descripción para la variable.
         * @param descripcion un String con la descripción de la variable.
         */
        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        /**
         * Devuelve el valor de la variable.
         * @return un double del valor de la variable.
         */
        public double getValue() {
            return value;
        }

        /**
         * Especifica el valor numérico para la variable.
         * @param value el valor double a especificar.
         */
        public void setValue(final double value) {
            this.value = value;
        }


        /**
         * Devuelve un String representando el valor de la variable.
         * @return String representando a la variable.
         */
        @Override
        public String toString() {
            return "Variable{" + "var_name=" + var_name + ", value=" + value + 
                    '}';
        }
    }
}
