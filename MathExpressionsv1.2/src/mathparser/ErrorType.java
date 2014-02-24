package mathparser;

/**
 * Enumeración conteniendo los tipos de errores que se pueeden presentar al 
 * traducir una expresión.
 * @author Leo Gutiérrez Ramírez <leogutierrezramirez@gmail.com>
 */
public enum ErrorType {
    ERROR_SINTAXIS_PARTE,
    ERROR_SINTAXIS,
    PARENTESIS_FALTANTE,
    EXPRESION_VACIA,
    PARTE_NO_ESPERADA,
    FIN_INESPERADO_EXPRESION,
    VALOR_ESPERADO,
    OPERADOR_DESCONOCIDO,
    FUNCION_DESCONOCIDA,
    VARIABLE_DESCONOCIDA,
    EXPRESION_DEMASIADA_LARGA,
    DEFINICION_DE_VARIABLE_FALLIDA,
    VALOR_ENTERO_ESPERADO_EN_FUNCION,
    COMA_FALTANTE,
    IDENTIFICADOR_COMO_PALABRA_RESERVADA,
    ASIGNACION_DE_CONSTANTE,
    FUERA_DOMINIO,
    ERROR_LEYENDO_VAR_FILE,
    DIVISION_POR_CERO;
}
