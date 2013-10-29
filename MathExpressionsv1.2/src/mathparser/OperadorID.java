package mathparser;

/**
 * @author Leo Gutiérrez Ramírez <leogutierrezramirez@gmail.com>
 */
public enum OperadorID {

    AND, // nivel2
    OR, // nivel2
    BITSHIFTLEFT, // nivel2               , sin utilidad.
    BITSHIFTRIGHT, // nivel2               , sin utilidad.
    EQUAL, // nivel3
    UNEQUAL, // nivel3
    SMALLER, // nivel3
    LARGER, // nivel3
    SMALLEREQ, // nivel3
    LARGEREQ, // nivel3
    PLUS, // nivel4
    MINUS, // nivel4
    MULTIPLY, // nivel5
    DIVIDE, // nivel5
    MODULUS, // nivel5
    XOR, // nivel5
    POW, // nivel6
    FACTORIAL, // nivel7
    NOT // nivel not  XXX NOT
    , UNKNOWN
}
