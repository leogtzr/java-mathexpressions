package mathparser;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Evalua una cadena y devuelve su valor numérico.
 * @author Leo Gutiérrez Ramírez. leogutierrezramirez@gmail.com
 * @author <a href="mailto:leogutierrezramirez@gmail.com"><b>Leo Gutiérrez R.</b></a>
 */
public final class Parser {

    /**
     * Representa la expresión a interpretar para un manejo más fácil.
     */
    private char[] expression = null;

    /**
     * El tipo de token actual obtenido con la función getToken().
     * @see getToken().
     */
    private TOKEN_TYPE tipoTokenActual = TOKEN_TYPE.NADA;
    /**
     * Contiene el caracter actual leído.
     */
    private int i = 0;
    /**
     * El token actual obtenido de la expresión.
     */
    private String token = "";
    /**
    *  Almacena el valor numérico de la expresión.
    */
    private double respuestaNumerica = 0.0;
    /**
     * Almacena la lista de variables definidas por el usuario.
     */
    private Variables userVar;

    /**
     * Constructor por defecto, inicializa el parser a sus valores por defecto.
     */
    public Parser() {
        tipoTokenActual = TOKEN_TYPE.NADA;
        userVar = new Variables();
        savedExpressions = new ArrayList<>();
    }
    
    private boolean evaluation = false;
    
    private ArrayList<String> savedExpressions = null;

    /**
     * Devuelve la lista de expresiones guardadas para ser evaluadas.
     * @return La lista de expresiones guardadas.
     */
    public ArrayList<String> getSavedExpressions() {
        return savedExpressions;
    }

    /**
     * Devuelve la respuesta numérica de la evaluación de la expresión.
     * @return La respuesta numérica de la evaluación de la expresión.
     */
    public double getNumericAnswer() {
        return respuestaNumerica;
    }

    /**
     * Devuelve la lista de variables definidas por el usuario.
     * @return Devuelve un ArrayList con las variables definidas por el usuario.
     */
    public Variables getUserVars() {
        return userVar;
    }

    /**
     * Inicializa el parser a sus valores iniciales.
     * @param expr La expresión a interpretar.
     */
    private void init(final String expr) {
        this.expression = (expr.replaceAll("\\s+", "") + '\u0000').
                toCharArray();
        i = 0;
        respuestaNumerica = 0.0;
    }
    
    public boolean evaluate(String expressionStr) throws ParsingException {
        this.evaluation = true;
        try {
            if(expressionStr.length() < 1) {
                throw new ParsingException("Expressión vacía", 0);
            }
            init(expressionStr);
            getToken();

            if(expression.length < 1) {
                throw new ParsingException("Expresión vacía", 0);
            }
            respuestaNumerica = parseLevel1();
            if(tipoTokenActual != TOKEN_TYPE.DELIMITADOR) {
                throw new ParsingException("Parte no esperada: " + token, i);
            }
            evaluation = false;
            return true;
        } catch(ParsingException ex) {
            evaluation = false;
            throw ex;
        }
    }
    
    /**
     * Almacena una expresión en la lista de expresiones.
     * @param expressionStr
     * @return un true si se agregó la expresión, false si la expresión no es
     * válida y no se puede agregrar.
     */
    public boolean saveExpression(String expressionStr) {
        try {
            boolean result = evaluate(expressionStr);
            if(result) {
                savedExpressions.add(expressionStr);
                return true;
            }
        } catch(ParsingException ex) {
            return false;
        }
        return false;
    }
    
    /**
     * Comprueba si una expresión es válida o no.
     * @param expressionStr
     * @return true si la expresión es válida, false si la expresión no es válida.
     */
    public boolean isExpressionOk(String expressionStr) {
        try {
            boolean result = evaluate(expressionStr);
            return result;
        } catch(ParsingException ex) {
            return false;
        }
    }
    
    

/**
 * Inicia la interpretación de la expresión, almacenando en
 * respuestaNumerica el valor de la evaluación.
 * @param expressionStr La expresión a evaluar.
 * @throws ParsingException Si no se siguen las reglas de sintáxis.
 */
    public void parse(final String expressionStr) throws ParsingException {

        if(expressionStr.length() < 1) {
            throw new ParsingException("Expressión vacía", 0);
        }

        init(expressionStr);
        getToken();

        if(expression.length < 1) {
            throw new ParsingException("Expresión vacía", 0);
        }

        respuestaNumerica = parseLevel1();

        if(tipoTokenActual != TOKEN_TYPE.DELIMITADOR) {
            throw new ParsingException("Parte no esperada: " + token, i);
        }

        userVar.addVar("ans", respuestaNumerica);
    }

    private double parseLevel1() throws ParsingException {

        if(tipoTokenActual == TOKEN_TYPE.VARIABLE) {
            TOKEN_TYPE tokenTemp = tipoTokenActual;
            String tokenNow = token;/*new String(token);*/

            int temp_index = i;

            getToken();
            if(token.equals("=")) {
                if(isFunction(tokenNow)) {
                    throw new ParsingException("Identificador como palabra reservada (" + tokenNow + ")", i);
                } else if(userVar.isConstant(tokenNow)) {
                    throw new ParsingException("Asignación de constante: " + tokenNow, i);
                } else if(tokenNow.equals("e")) {
                    throw new ParsingException("Identificador como palabra reservada (" + tokenNow + ")", i);
                } else if(tokenNow.equals("pi")) {
                    throw new ParsingException("Identificador como palabra reservada (" + tokenNow + ")", i);
                } else if(tokenNow.equals("g")) {
                    throw new ParsingException("Identificador como palabra reservada (" + tokenNow + ")", i);
                } else if(tokenNow.equals("random")) {
                    throw new ParsingException("Identificador como palabra reservada (" + tokenNow + ")", i);
                }
                getToken();

                double r_temp = parseLevel2();
                if(userVar.addVar(tokenNow, r_temp) == false) {
                    throw new ParsingException("Definición de variable fallida", i);
                } else {
                    return r_temp;
                }
            } else {
                // No era una asignación, hay que recuperar el índex:
                i = temp_index;
                token = tokenNow;
                tipoTokenActual = tokenTemp;
            }
        }
        return parseLevel2();
    }

    private double parseLevel2() throws ParsingException {

        double answer = parseLevel3();

        OPERADOR_ID op_id = getOperatorId(token);

    while((op_id == OPERADOR_ID.AND) || (op_id == OPERADOR_ID.OR) ||
                (op_id == OPERADOR_ID.BITSHIFTLEFT) || (op_id == OPERADOR_ID.BITSHIFTRIGHT)) {
                getToken();
                answer = eval_operator(op_id, answer, parseLevel3());
                op_id = getOperatorId(token);
            }
        return answer;
    }
    
    private double parseLevel3() throws ParsingException {

        double answer = parseLevel4();
        OPERADOR_ID op_id = getOperatorId(token);
        
        while((op_id == OPERADOR_ID.EQUAL) || (op_id == OPERADOR_ID.UNEQUAL) || 
                (op_id == OPERADOR_ID.SMALLER) ||
                  (op_id == OPERADOR_ID.LARGER) || (op_id == OPERADOR_ID.SMALLEREQ) || (op_id == OPERADOR_ID.LARGEREQ)) {
                getToken();
                answer = eval_operator(op_id, answer, parseLevel4());
                op_id = getOperatorId(token);
            }
        return answer;
    }

    private double parseLevel4() throws ParsingException {
        double answer = parseLevel5();
        OPERADOR_ID op_id = getOperatorId(token);

        while(op_id == OPERADOR_ID.PLUS || op_id == OPERADOR_ID.MINUS) {
				getToken();

				// XXX Eliminar si hay problemas!
				/*
					El siguiente trozo de código, evita que haya expresiones del tipo:
					1+-2
					1--2
				*/

                if(token.isEmpty()) {
                    throw new ParsingException("Error de sintáxis: " + i);
                }

                if(token.charAt(0) == '-') {
                    throw new ParsingException("Parte no esperadas: " + 
                            token, i);
                }

                answer = eval_operator(op_id, answer, parseLevel5());
                op_id = getOperatorId(token);
            }
        return answer;
    }

    private double parseLevel5() throws ParsingException {
        double answer = parseLevel6();
        OPERADOR_ID op_id = getOperatorId(token);

        while((op_id == OPERADOR_ID.MULTIPLY) || (op_id == OPERADOR_ID.DIVIDE) || (op_id == OPERADOR_ID.MODULUS) || (op_id == OPERADOR_ID.XOR)) {
                getToken();
                answer = eval_operator(op_id, answer, parseLevel6());
                op_id = getOperatorId(token);
            }

        return answer;
    }

    private double parseLevel6() throws ParsingException {
        double answer = parseLevel8();
        OPERADOR_ID op_id = getOperatorId(token);

        while(op_id == OPERADOR_ID.POW) {
                getToken();
                answer = eval_operator(op_id, answer, parseLevel8());
                op_id = getOperatorId(token);
            }

        return answer;
    }

    // private double parseLevel7() {return 0.0;}

    private double parseLevel8() throws ParsingException {
        double answer;

        OPERADOR_ID op_id = getOperatorId(token);
        if(op_id == OPERADOR_ID.MINUS) {
				getToken();
				answer = parse_not();
                answer = -answer;
			} /*else if(op_id == NOT) {
				getToken();
				answer = parse_level2();
				answer = !answer;
			}*/ else {
                    answer = parse_not();
				}

        return answer;
    }

    private double parse_not() throws ParsingException {
            double answer;
            OPERADOR_ID op_id = getOperatorId(token);

			if(op_id == OPERADOR_ID.NOT) {

                getToken();
                answer = parseLevel9();
                //answer = !(answer);
                // @todo Aguas!:
                answer = (answer != 0.0) ? 0.0 : 1.0;
            } else {
                answer = parseLevel9();
            }
            return answer;
        }

    private double parseLevel9() throws ParsingException {
        double answer = 0.0;
        if(tipoTokenActual == TOKEN_TYPE.FUNCION) {
            // Copiamos el nombre de la función:
            String fn_name = token.toUpperCase(java.util.Locale.getDefault());
            if(isFunction(fn_name) == false) {
                throw new ParsingException("Función desconocida: " +
                        fn_name, i);
            }
            if(isFunctionDouble(fn_name)) {
                // Avanzar al siguiente token, avanzar el delimitador '(':
                    getToken();
                    // Avanzamos a la expresion:
                    getToken();

                    double expresion_1 = parseLevel2();
                    getToken();
                    double expresion_2 = parseLevel2();

                    answer = eval_function_double(fn_name, expresion_1,
                            expresion_2);

                    if(!token.equals(")")) {
                        throw new ParsingException("Se esperaba paréntesis de cierre", i);
                    }
                    
            } else {
                getToken();
                double expresionFunction = parseLevel10();
                answer = eval_function(fn_name, expresionFunction);
            }
        } else if(tipoTokenActual == TOKEN_TYPE.VARIABLE) {
            if(isFunction(token)) {
                int e_now = i;
                TOKEN_TYPE token_type_now = tipoTokenActual;
                String token_now = token;
                
                getToken();
                
                if(token.compareTo("(") != 0) {
                    throw new ParsingException("Se esperaba paréntesis de cierre", i);
                } else {
                    // Sino es una asignación entonces recuperamos el token anterior:
                    // Volver todo a la normalidad.
					i = e_now;
					tipoTokenActual = token_type_now;
                    token = token_now;
                }
            }
            answer = parseLevel10();
        } else {
            answer = parseLevel10();
        }
        return answer;
    }

    private double parseLevel10() throws ParsingException {
        if(tipoTokenActual == TOKEN_TYPE.DELIMITADOR) {

            if(token.isEmpty()) {
                throw new ParsingException("Final inesperado de expresión");
            }

            if(token.charAt(0) == '(') {
                getToken();

                double answer = parseLevel2();
                if(token.isEmpty()) {
                    throw new ParsingException("Paréntesis faltante", i);
                }

                if(tipoTokenActual != TOKEN_TYPE.DELIMITADOR) {
                    throw new ParsingException("Paréntesis faltante", i);
                }
                getToken();
                return answer;
            }
        }
        return parseNumber();
    }

    private double parseNumber() throws ParsingException {
        double answer = 0.0;
        switch(tipoTokenActual) {
            case NUMERO:
                answer = Double.parseDouble(token);
                getToken();
                break;

            case VARIABLE:
                answer = eval_variable(token);
                getToken();
                break;

            default:
                if(token.charAt(0) == '\u0000' || token.length() < 1) {
                    throw new ParsingException("Fin inesperado de expresión", i);
                } else {
                    throw new ParsingException("Valor esperado", i);
                }
        }
        return answer;
    }

    private void getToken() throws ParsingException {

        tipoTokenActual = TOKEN_TYPE.NADA;
        token = "";

        if(expression[i] == ' ' || expression[i] == '\t' || 
                expression[i] == '\n') {
            i++;
        }

        if(expression[i] == '\u0000') {
            tipoTokenActual = TOKEN_TYPE.DELIMITADOR;
            return;
        }

        if(expression[i] == '-') {
            tipoTokenActual = TOKEN_TYPE.DELIMITADOR;
            token += '-';
            i++;
            return;
        }

        // Paréntesis:
        if(expression[i] == ')' || expression[i] == '(') {
            tipoTokenActual = TOKEN_TYPE.DELIMITADOR;
            token += expression[i];
            i++;
            return;
        }

        if(isDelimeter(expression[i])) {
            tipoTokenActual = TOKEN_TYPE.DELIMITADOR;
            while(isDelimeter(expression[i])) {
                token += expression[i];
                i++;
            }

            // == !1
            // x ==!0
            if(token.length() > 2) {
                token = token.substring(0, 2);
                i--;
            }
            //token = token + '\u0000';
            return;
            // Cuidado con el NOT!
        }

        if(isDigitDot(expression[i])) {
            tipoTokenActual = TOKEN_TYPE.NUMERO;
            while(isDigit(expression[i])) {
                token += expression[i];
                i++;
            }
            if(expression[i] == '.') {
                token += '.';
                i++;
            }
            while(isDigit(expression[i])) {
                token += expression[i];
                i++;
            }
            if(Character.toUpperCase(expression[i]) == 'E') {
                token += 'E';
                i++;
                
                if(expression[i] == '+' || expression[i] == '-') {
                    token += expression[i];
                    i++;
                }
                
                while(isDigit(expression[i])) {
                    token += expression[i];
                    i++;
                }
                
            }
            return;
        }
        if(isAlpha(expression[i])) {
            while(isAlpha(expression[i]) || isDigit(expression[i])) {
                token += expression[i];
                i++;
            }
            // Verificar si es función o variable:
            if(expression[i] == '(') {
                tipoTokenActual = TOKEN_TYPE.FUNCION;
            } else {
                //System.out.println("Var: " + token);
                tipoTokenActual = TOKEN_TYPE.VARIABLE;
            }
            return;
        }

        tipoTokenActual = TOKEN_TYPE.NADA;

        int colError = i;

        // ERROR .... Ver qué hacer:
        while(expression[i] != '\u0000') {
            token += expression[i];
            i++;
        }
        throw new ParsingException("Error en parte: [" + token + ']', colError);
    }

    private OPERADOR_ID getOperatorId(final String op) {
        switch(op) {
            case "&&":
                return OPERADOR_ID.AND;
            case "|":
                return OPERADOR_ID.OR;
            case "<<":
                return OPERADOR_ID.BITSHIFTLEFT;
            case ">>":
                return OPERADOR_ID.BITSHIFTRIGHT;
            case "==":
                return OPERADOR_ID.EQUAL;

            case "!=":
                return OPERADOR_ID.UNEQUAL;
            case "<":
                return OPERADOR_ID.SMALLER;
            case ">":
                return OPERADOR_ID.LARGER;
            case "<=":
                return OPERADOR_ID.SMALLEREQ;
            case ">=":
                return OPERADOR_ID.LARGEREQ;
            case "+":
                return OPERADOR_ID.PLUS;
            case "-":
                return OPERADOR_ID.MINUS;
            case "*":
                return OPERADOR_ID.MULTIPLY;
            case "/":
                return OPERADOR_ID.DIVIDE;
            case "%":
                return OPERADOR_ID.MODULUS;
            case "||":
                return OPERADOR_ID.MODULUS;
            case "^":
                return OPERADOR_ID.POW;
            case "!":
                return OPERADOR_ID.NOT;
        }
        return OPERADOR_ID.UNKNOWN;
    }

    /*
            Evaluar un operador con determinados valores:
        */
    private double eval_operator(OPERADOR_ID op_id, double lhs, double rhs) 
            throws ParsingException {

        if(evaluation) {
            return 1.0;
        }
        
        switch(op_id) {
            case AND:
                return boolToDouble(toBool((int)lhs) && toBool((int)rhs));
            case OR:
                return boolToDouble(toBool((int)lhs) || toBool((int)rhs));
            case BITSHIFTLEFT:
                return (int)lhs << (int)rhs;
            case BITSHIFTRIGHT:
                return (int)lhs >> (int)rhs;
            case EQUAL:
                return (lhs == rhs ? 1.0 : 0.0);
            case UNEQUAL:
                return (lhs != rhs ? 1.0 : 0.0);
            case SMALLER:
                return (lhs < rhs ? 1.0 : 0.0);
            case LARGER:
                return (lhs > rhs ? 1.0 : 0.0);
            case SMALLEREQ:
                return (lhs <= rhs ? 1.0 : 0.0);
            case LARGEREQ:
                return (lhs >= rhs ? 1.0 : 0.0);
            case PLUS:
                return lhs + rhs;
            case MINUS:
                return lhs - rhs;
            case MULTIPLY:
                return lhs * rhs;
            case DIVIDE:
                return lhs / rhs;
            case MODULUS:
                return lhs % rhs;
            case XOR:
                return (int)lhs ^ (int)rhs;
            case POW:
                return Math.pow(lhs, rhs);
        }

        throw new ParsingException("Operador: " + op_id + " desconocido", i);
    }

    private double eval_function_double(final String fn_name, 
            double param_left, double param_right) throws ParsingException {

        if(evaluation) {
            return 1.0;
        }
        
        switch(fn_name) {
            case "POWER":
                return Math.pow(param_left, param_right);
            case "MAX":
                return Math.max(param_left, param_right);
            case "MIN":
                return Math.min(param_left, param_right);
            case "MOD":
                return param_left % param_right;
            case "RAND":
                return MathFunctions.rand_int_between((int)param_left, 
                        (int)param_right);
        }
        throw new ParsingException("Función desconocida: " + fn_name, i);
    }

    private double eval_function(final String fn_name, final double value) 
            throws ParsingException {
        
        if(evaluation) {
            return 1.0;
        }
        
        switch(fn_name.toUpperCase(java.util.Locale.getDefault())) {

            case "ABS":
                return Math.abs(value);

            case "EXP":
                return Math.exp(value);

            case "SIGN":
                return Math.signum(value);

            case "SQRT":
            case "RAIZ":
                double __temp = Math.sqrt(value);
                if(Double.isNaN(__temp)) {
                    throw new ParsingException("Intentando evaluar raíz para número negativo: " + value, i);
                }
                return __temp;

            case "SIN":
                return MathFunctions.math_sin(value);

            case "COS":
                return Math.cos(value);

            case "FACTORIAL":
                return MathFunctions.factorial(value);

            case "LOG":
            case "LN":
                return MathFunctions.math_log(value);

            case "LOG10":
                return MathFunctions.math_log10(value);

            case "LOG2":
                return MathFunctions.math_log2(value);

            case "TAN":
                return MathFunctions.math_tan(value);

            case "ASIN":
                return MathFunctions.math_asin(value);

            case "ACOS":
                return MathFunctions.math_acos(value);

            case "ASEC":
                return MathFunctions.math_asec(value);

            case "COT":
                return MathFunctions.math_cot(value);

            case "SEC":
                return MathFunctions.math_sec(value);

            case "CSC":
                return MathFunctions.math_csc(value);

            case "ATAN":
                return MathFunctions.math_atan(value);

            case "SINH":
                return Math.sinh(value);

            case "ASINH":
                return MathFunctions.math_asinh(value);

            case "ACOSH":
                return MathFunctions.math_acosh(value);

            case "ASECH":
                return MathFunctions.math_asech(value);

            case "COSH":
                return Math.cosh(value);

            case "TANH":
                return Math.tanh(value);

            case "COTH":
               return MathFunctions.math_coth(value);

            case "SECH":
                return MathFunctions.math_sech(value);

            case "CSCH":
                return MathFunctions.math_csch(value);

            case "ACSCH":
                return MathFunctions.math_acsch(value);

            case "ATANH":
                return MathFunctions.math_atanh(value);

            case "ACOT":
                return MathFunctions.math_acot(value);

            case "ACOTH":
                return MathFunctions.math_acoth(value);

            case "ACSC":
                return MathFunctions.math_acsc(value);

            case "RAND":
                return MathFunctions.rand_0_to_1();
        }
        throw new ParsingException("Función desconocida: " + fn_name, i);
    }

    private double eval_variable(final String var_name) 
            throws ParsingException {
        
        if(evaluation) {
            return 1.0;
        }
        
        
            switch(var_name.toUpperCase(java.util.Locale.getDefault())) {
                case "E":
                    return Math.E;
                case "PI":
                    return Math.PI;
                case "G":
                    return 9.80665;
                case "RANDOM":
                    return Math.random();
            }

            // Verificar por variables definidas por el usuario:
            double ans;

            if(userVar.existVar(var_name)) {
                ans = userVar.getValueVar(var_name);
                return ans;
            }

            throw new ParsingException("Variable desconocida: [" + var_name + 
                    "]", i);
    }

    private double boolToDouble(boolean value) {
        return value == true ? 1.0 : 0.0;
    }

    private static boolean toBool(int val) {
        return val != 0;
    }

    private enum TOKEN_TYPE {
        NADA, DELIMITADOR, NUMERO, VARIABLE, FUNCION, DESCONOCIDO, OPERADOR
    };

    // Tipos de operadores:
    private enum OPERADOR_ID {
        AND,				// nivel2
        OR,					// nivel2
        BITSHIFTLEFT,		// nivel2               , sin utilidad.
        BITSHIFTRIGHT,		// nivel2               , sin utilidad.
        EQUAL,				// nivel3
        UNEQUAL, 			// nivel3
        SMALLER, 			// nivel3
        LARGER, 			// nivel3
        SMALLEREQ, 			// nivel3
        LARGEREQ, 			// nivel3
        PLUS, 				// nivel4
        MINUS, 				// nivel4
        MULTIPLY, 			// nivel5
        DIVIDE, 			// nivel5
        MODULUS, 			// nivel5
        XOR, 				// nivel5
        POW, 				// nivel6
        FACTORIAL,			// nivel7
        NOT                 // nivel not  XXX NOT
        , UNKNOWN
    };

    private boolean isDelimeter(final char c) {
        return "&|<>=+/*%^!,".indexOf(c) != -1;
    }

    private boolean isDigitDot(final char c) {
        return "0123456789.".indexOf(c) != -1;
    }

    private boolean isDigit(final char c) {
        return "0123456789".indexOf(c) != -1;
    }

    private boolean isAlpha(final char c) {
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZ_".
                indexOf(Character.toUpperCase(c)) != -1;
    }
    
    private boolean isFunctionDouble(final String functionName) {
        
        boolean result = false;
        
        switch(functionName.toUpperCase(Locale.getDefault())) {
            case "POWER":
            case "SUMA":
            case "MIN":
            case "MAX":
            case "MOD":
            case "RAND":
                result = true;
                break;
            default:
                break;
        }
        return result;
    }
    
    private boolean isFunction(final String functionName) {
        
        boolean result = false;
        
        switch(functionName.toUpperCase(Locale.getDefault())) {
            case "ABS":
            case "EXP":
            case "SIGN":
            case "SQRT":
            case "RAIZ":
            case "LOG":
            case "LN":
            case "LOG10":
            case "SIN":
            case "COS":
            case "TAN":
            case "ASIN":
            case "ACOS":
            case "ATAN":
            case "FACTORIAL":
            case "COT":
            case "SEC":
            case "CSC":
            case "SINH":
            case "COSH":
            case "TANH":
            case "COTH":
            case "SECH":
            case "CSCH":
            case "ACSC":
            case "ASEC":
            case "ASINH":
            case "ACOSH":
            case "ACSCH":
            case "ATANH":
            case "POWER":
            case "MAX":
            case "MIN":
            case "MOD":
            case "ACOT":
            case "ACOTH":
            case "LOG2":
            case "RAND":
            case "ASECH":
                //return true;
                result = true;
                break;
            default:
                result = false;
        }
        return result;
    }
    
}
