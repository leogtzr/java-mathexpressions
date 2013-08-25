package mathparser;

/**
 *  Arrojada para indicar que se cometió un error de sintáxis en la
 * expresión a evaluar.
 * @author Leo Gutiérrez Ramírez. leogutierrezramirez@gmail.com
 * @author <a href="mailto:leogutierrezramirez@gmail.com"><b>Leo Gutiérrez R.</b></a>
 */
public class ParsingException extends Exception {

    /**
     * La columna o posición donde se dió el error de sintáxis o de
     * evaluación.
     */
    private int column;

    /**
     * Construye un ParsingException con la cadena mensaje dada como
     * parámetro.
     * @param message Descripción del error que se detectó.
     */
    public ParsingException(final String message) {
        super(message);
    }

    /**
     * Construye un ParsingException con la cadena mensaje dada como
     * parámetro y la posición donde se detectó el error.
     * @param message Descripción del error que se detectó.
     * @param column La posición donde se detectó el error.
     */
    public ParsingException(final String message, final int column) {
        super(message);
        this.column = column;
    }

    /**
     * Devuelve la posición del error donde se detectó.
     * @return La posición del error donde se detectó.
     */
    public final int getColumn() {
        return column;
    }
}