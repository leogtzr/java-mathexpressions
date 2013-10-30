package mathparser;

import java.util.HashMap;
import java.util.Locale;

import mathparser.Lexer.TokenType;
import static mathparser.Lexer.TokenType.*;

/**
 * Evalua una cadena y devuelve su valor numérico.
 *
 * @author Leo Gutiérrez Ramírez. leogutierrezramirez@gmail.com
 * @author <a href="mailto:leogutierrezramirez@gmail.com"><b>Leo Gutiérrez
 * R.</b></a>
 */
public final class Parser {
    
    private HashMap<String, CustomFunction> customFunctions = null;
    private HashMap<String, SimpleFunction> simpleFunctions = null;
    private final Lexer lex = new Lexer();
    private double respuestaNumerica = 0.0;
    private final Variables userVar;
    private boolean evaluation = false;

    public Parser() {
        userVar = new Variables();
        customFunctions = new HashMap<>();
        simpleFunctions = new HashMap<>();
    }
    
    public void addCustomFunction(String name, CustomFunction cf) {
        if(isFunction(name.toUpperCase(java.util.Locale.getDefault()))) {
            throw new IllegalArgumentException("function '" + name + "' is reserved");
        }
        this.customFunctions.put(name.toUpperCase(java.util.Locale.getDefault()), cf);
    }
    
    public void addSimpleFunction(String name, SimpleFunction sf) {
        if(isFunction(name.toUpperCase(java.util.Locale.getDefault()))) {
            throw new IllegalArgumentException("function '" + name + "' is reserved");
        }
        this.simpleFunctions.put(name.toUpperCase(java.util.Locale.getDefault()), sf);
    }
    
    public void addVariable(String name, double value) {
        if(isFunction(name) || userVar.isConstant(name) || name.equals("e") || 
                name.equals("pi") || name.equals("g") || name.equals("random")) {
            throw new IllegalArgumentException("'" + name + "' is a reserved word");
        }
        userVar.addVar(name, value);
    }
    
    public double getNumericAnswer() {
        return respuestaNumerica;
    }

    public Variables getUserVars() {
        return userVar;
    }

    public boolean evaluate(String expressionStr) throws ParsingException {
        this.evaluation = true;
        try {
            if (expressionStr.isEmpty()) {
                throw new ParsingException("Empty expression", 0, ErrorType.EXPRESION_VACIA);
            }

            lex.init(expressionStr);
            lex.nextToken();
            
            if(lex.getToken().isEmpty()) {
                throw new ParsingException("Empty expression", 0, ErrorType.EXPRESION_VACIA);
            }
            
            respuestaNumerica = parseLevel1();
            
            // Lexer.TokenType.DELIMITADOR
            
            if(lex.getCurrentType() != DELIMITADOR) {
                throw new ParsingException("unexpected expression '" + lex.getToken() + "'", lex.getPos(), ErrorType.PARTE_NO_ESPERADA);
            }
            
            evaluation = false;
            return true;
            
        } catch (ParsingException ex) {
            evaluation = false;
            throw ex;
        }
    }

    public boolean isExpressionOk(String expressionStr) {
        try {
            boolean result = evaluate(expressionStr);
            return result;
        } catch (ParsingException ex) {
            this.evaluation = false;
            return false;
        }
    }

    public void parse(final String expressionStr) throws ParsingException {

        if (expressionStr.isEmpty()) {
            throw new ParsingException("Empty expression", 0, ErrorType.EXPRESION_VACIA);
        }

        lex.init(expressionStr);
        lex.nextToken();
        
        if(lex.getToken().isEmpty()) {
            throw new ParsingException("Empty expression [e1]", 0, ErrorType.EXPRESION_VACIA);
        }

        respuestaNumerica = parseLevel1();

        if(lex.getCurrentType() != DELIMITADOR) {
            throw new ParsingException("unexpected expression '" + lex.getToken() + "'", lex.getPos(), ErrorType.PARTE_NO_ESPERADA);
        }

        userVar.addVar("ans", respuestaNumerica);
    }

    private double parseLevel1() throws ParsingException {

        if (lex.getCurrentType() == VARIABLE) {
            
            TokenType tokenTemp = lex.getCurrentType();
            String tokenNow = lex.getToken();

            int temp_index = lex.getPos();

            lex.nextToken();
            if (lex.getToken().equals("=")) {
                if (isFunction(tokenNow)) {
                    throw new ParsingException("reserved word used as identifier '" + lex.getToken() + "'", lex.getPos(), ErrorType.IDENTIFICADOR_COMO_PALABRA_RESERVADA);
                } else if (userVar.isConstant(tokenNow)) {
                    throw new ParsingException("cannot assign a value to const variable '" + tokenNow + "'", lex.getPos(), ErrorType.ASIGNACION_DE_CONSTANTE);
                } else if (tokenNow.equals("e")) {
                    throw new ParsingException("cannot assign a value to const variable '" + tokenNow + "'", lex.getPos(), ErrorType.ASIGNACION_DE_CONSTANTE);
                } else if (tokenNow.equals("pi")) {
                    throw new ParsingException("cannot assign a value to const variable '" + tokenNow + "'", lex.getPos(), ErrorType.ASIGNACION_DE_CONSTANTE);
                } else if (tokenNow.equals("g")) {
                    throw new ParsingException("cannot assign a value to const variable '" + tokenNow + "'", lex.getPos(), ErrorType.ASIGNACION_DE_CONSTANTE);
                } else if (tokenNow.equals("random")) {
                    throw new ParsingException("cannot assign a value to const variable '" + tokenNow + "'", lex.getPos(), ErrorType.ASIGNACION_DE_CONSTANTE);
                }
                lex.nextToken();

                double r_temp = parseLevel2();
                if (userVar.addVar(tokenNow, r_temp) == false) {
                    throw new ParsingException("Defining variable failed", lex.getPos(), ErrorType.DEFINICION_DE_VARIABLE_FALLIDA);
                } else {
                    return r_temp;
                }
            } else {
                // No era una asignación, hay que recuperar el índex:
                lex.setIndex(temp_index);
                lex.setToken(tokenNow);
                lex.setType(tokenTemp);
            }
        }
        return parseLevel2();
    }

    private double parseLevel2() throws ParsingException {

        double answer = parseLevel3();

        OperadorID op_id = getOperatorId(lex.getToken());

        while ((op_id == OperadorID.AND) || (op_id == OperadorID.OR)
                || (op_id == OperadorID.BITSHIFTLEFT) || (op_id == OperadorID.BITSHIFTRIGHT)) {
            lex.nextToken();
            answer = eval_operator(op_id, answer, parseLevel3());
            op_id = getOperatorId(lex.getToken());
        }
        return answer;
    }

    private double parseLevel3() throws ParsingException {

        double answer = parseLevel4();
        OperadorID op_id = getOperatorId(lex.getToken());

        while ((op_id == OperadorID.EQUAL) || (op_id == OperadorID.UNEQUAL)
                || (op_id == OperadorID.SMALLER)
                || (op_id == OperadorID.LARGER) || (op_id == OperadorID.SMALLEREQ) || (op_id == OperadorID.LARGEREQ)) {
            lex.nextToken();
            answer = eval_operator(op_id, answer, parseLevel4());
            op_id = getOperatorId(lex.getToken());
        }
        return answer;
    }

    private double parseLevel4() throws ParsingException {
        double answer = parseLevel5();
        OperadorID op_id = getOperatorId(lex.getToken());

        while (op_id == OperadorID.PLUS || op_id == OperadorID.MINUS) {
            //getToken();
            lex.nextToken();

            // XXX Eliminar si hay problemas!
				/*
             El siguiente trozo de código, evita que haya expresiones del tipo:
             1+-2
             1--2
             */
            if (lex.getToken().isEmpty()) {
                throw new ParsingException("syntax error", lex.getPos(), ErrorType.ERROR_SINTAXIS);
            }

            if (lex.getToken().charAt(0) == '-') {
                throw new ParsingException("unexpected expression '" + lex.getToken() + "'", lex.getPos(), ErrorType.PARTE_NO_ESPERADA);
            }

            answer = eval_operator(op_id, answer, parseLevel5());
            op_id = getOperatorId(lex.getToken());
        }
        return answer;
    }

    private double parseLevel5() throws ParsingException {
        double answer = parseLevel6();
        OperadorID op_id = getOperatorId(lex.getToken());

        while ((op_id == OperadorID.MULTIPLY) || (op_id == OperadorID.DIVIDE) || (op_id == OperadorID.MODULUS) || (op_id == OperadorID.XOR)) {
            lex.nextToken();
            answer = eval_operator(op_id, answer, parseLevel6());
            op_id = getOperatorId(lex.getToken());
        }

        return answer;
    }

    private double parseLevel6() throws ParsingException {
        double answer = parseLevel8();
        OperadorID op_id = getOperatorId(lex.getToken());

        while (op_id == OperadorID.POW) {
            lex.nextToken();
            answer = eval_operator(op_id, answer, parseLevel8());
            op_id = getOperatorId(lex.getToken());
        }

        return answer;
    }

    // private double parseLevel7() {return 0.0;}
    private double parseLevel8() throws ParsingException {
        double answer;

        OperadorID op_id = getOperatorId(lex.getToken());
        if (op_id == OperadorID.MINUS) {
            lex.nextToken();
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

    // Funcionando, versión anterior
    private double parseLevel9() throws ParsingException {
        double answer = 0.0;
        
        if(lex.getCurrentType() == FUNCION) {
            String fn_name = lex.getToken().toUpperCase(java.util.Locale.getDefault());
            if (isFunction(fn_name) == false && existsCustomFunction(fn_name) == false && existsSimpleFunction(fn_name) == false) {
                throw new ParsingException("unknown function " + fn_name, lex.getPos(), ErrorType.FUNCION_DESCONOCIDA);
            }
            if (isFunctionDouble(fn_name)) {
                lex.nextToken();
                lex.nextToken();
                double lhs = parseLevel2();
                
                /*if(!lex.getToken().equals(",")) {
                    throw new ParsingException(", expected", lex.getPos(), ErrorType.ERROR_SINTAXIS);
                }*/
                
                lex.nextToken();
                double rhs = parseLevel2();
                
                lex.nextToken();
                
                answer = eval_function_double(fn_name, lhs, rhs);
                
                /*if (!lex.getToken().equals(")")) {
                    throw new ParsingException("')' expected 276", lex.getPos(), ErrorType.PARENTESIS_FALTANTE);
                }*/
            } else if(existsCustomFunction(fn_name)) {
                lex.nextToken();
                lex.nextToken();
                
                double lhs = parseLevel2();
                
                lex.nextToken();
                
                double rhs = parseLevel2();
                
                lex.nextToken();
                
                answer = this.customFunctions.get(fn_name).functionCode(lhs, rhs);
            }
            else {
                if(existsSimpleFunction(fn_name)) {
                    lex.nextToken();
                    double exp = parseLevel10();
                    answer = this.simpleFunctions.get(fn_name).functionCode(exp);
                } else {
                    lex.nextToken();
                    double exp = parseLevel10();
                    answer = eval_function(fn_name, exp);
                }
            }
        } else {
            answer = parseLevel10();
        }
        
        return answer;
    }
    
    private double parse_not() throws ParsingException {
        double answer;
        OperadorID op_id = getOperatorId(lex.getToken());

        if (op_id == OperadorID.NOT) {

            lex.nextToken();
            answer = parseLevel9();
            //answer = !(answer);
            // @todo Aguas!:
            answer = (answer != 0.0) ? 0.0 : 1.0;
        } else {
            answer = parseLevel9();
        }
        return answer;
    }

    // Functions ...
    private double parseLevel10() throws ParsingException {
        
        if (lex.getCurrentType() == DELIMITADOR) {
            
            if(lex.getToken().isEmpty()) {
                throw new ParsingException("syntax error", lex.getPos(), ErrorType.ERROR_SINTAXIS);
            }
            
            if (lex.getToken().charAt(0) == '(') {
                lex.nextToken();
                double answer = parseLevel2();
                
                lex.nextToken();
                /*if(!lex.getToken().isEmpty() && lex.getToken().equals(")")) {
                    throw new ParsingException("')' expected [...]", lex.getPos(), ErrorType.PARENTESIS_FALTANTE);
                }*/
                return answer;
            }
        }
        return parseNumber();
    }

    private double parseNumber() throws ParsingException {
        double answer = 0.0;
        
        switch (lex.getCurrentType()) {
            case NUMERO:
                answer = Double.parseDouble(lex.getToken());
                lex.nextToken();
                break;

            case VARIABLE:
                answer = eval_variable(lex.getToken());
                lex.nextToken();
                break;

            default:
                if (lex.getToken().charAt(0) == '\u0000' || lex.getToken().isEmpty()) {
                throw new ParsingException("expected expression", -1, ErrorType.FIN_INESPERADO_EXPRESION);
            } else {
                throw new ParsingException("value expected", lex.getPos(), ErrorType.VALOR_ESPERADO);
            }
        }
        return answer;
    }
    
    private OperadorID getOperatorId(final String op) {
        switch (op) {
            case "&&":
                return OperadorID.AND;
            case "|":
                return OperadorID.OR;
            case "<<":
                return OperadorID.BITSHIFTLEFT;
            case ">>":
                return OperadorID.BITSHIFTRIGHT;
            case "==":
                return OperadorID.EQUAL;
            case "!=":
                return OperadorID.UNEQUAL;
            case "<":
                return OperadorID.SMALLER;
            case ">":
                return OperadorID.LARGER;
            case "<=":
                return OperadorID.SMALLEREQ;
            case ">=":
                return OperadorID.LARGEREQ;
            case "+":
                return OperadorID.PLUS;
            case "-":
                return OperadorID.MINUS;
            case "*":
                return OperadorID.MULTIPLY;
            case "/":
                return OperadorID.DIVIDE;
            case "%":
                return OperadorID.MODULUS;
            case "||":
                return OperadorID.MODULUS;
            case "^":
                return OperadorID.POW;
            case "!":
                return OperadorID.NOT;
        }
        return OperadorID.UNKNOWN;
    }

    private double eval_operator(OperadorID op_id, double lhs, double rhs)
            throws ParsingException {

        if (evaluation) {
            return 1.0;
        }

        switch (op_id) {
            case AND:
                return boolToDouble(toBool((int) lhs) && toBool((int) rhs));
            case OR:
                return boolToDouble(toBool((int) lhs) || toBool((int) rhs));
            case BITSHIFTLEFT:
                return (int) lhs << (int) rhs;
            case BITSHIFTRIGHT:
                return (int) lhs >> (int) rhs;
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
                return (int) lhs ^ (int) rhs;
            case POW:
                return Math.pow(lhs, rhs);
        }
        throw new ParsingException("wrong operator '" + lex.getToken() + "'", lex.getPos(), ErrorType.OPERADOR_DESCONOCIDO);
    }

    private double eval_function_double(final String fn_name,
            double param_left, double param_right) throws ParsingException {
        if (evaluation) {
            return 1.0;
        }
        switch (fn_name) {
            case "POWER":
                return Math.pow(param_left, param_right);
            case "MAX":
                return Math.max(param_left, param_right);
            case "MIN":
                return Math.min(param_left, param_right);
            case "MOD":
                return param_left % param_right;
            case "RAND":
                return MathFunctions.rand_int_between((int) param_left,
                        (int) param_right);
        }
        throw new ParsingException("unknown function '" + fn_name, lex.getPos(), ErrorType.FUNCION_DESCONOCIDA);
    }

    private double eval_function(final String fn_name, final double value)
            throws ParsingException {

        if (evaluation) {
            return 1.0;
        }

        switch (fn_name.toUpperCase(java.util.Locale.getDefault())) {

            case "ABS":
                return Math.abs(value);

            case "EXP":
                return Math.exp(value);

            case "SIGN":
                return Math.signum(value);

            case "SQRT":
            case "RAIZ":
                double __temp = Math.sqrt(value);
                if (Double.isNaN(__temp)) {
                    throw new ParsingException("Intentando evaluar raíz para número negativo: " + value, lex.getPos());
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
        throw new ParsingException("unknown function '" + fn_name, lex.getPos(), ErrorType.FUNCION_DESCONOCIDA);
    }

    private double eval_variable(final String var_name)
            throws ParsingException {

        if (evaluation) {
            return 1.0;
        }

        switch (var_name.toUpperCase(java.util.Locale.getDefault())) {
            case "E":
                return Math.E;
            case "PI":
                return Math.PI;
            case "G":
                return 9.80665;
            case "RANDOM":
                return Math.random();
        }
        
        double ans;

        if (userVar.existVar(var_name)) {
            ans = userVar.getValueVar(var_name);
            return ans;
        }
        throw new ParsingException("unknown variable '" + var_name + "'", lex.getPos(), ErrorType.VARIABLE_DESCONOCIDA);
    }

    private double boolToDouble(boolean value) {
        return value ? 1.0 : 0.0;
    }

    private static boolean toBool(int val) {
        return val != 0;
    }

    private boolean isFunctionDouble(final String functionName) {
        switch (functionName.toUpperCase(Locale.getDefault())) {
            case "POWER":
            case "SUMA":
            case "MIN":
            case "MAX":
            case "MOD":
            case "RAND":
                return true;
        }
        return false;
    }
    
    private boolean existsCustomFunction(String functionName) {
        return customFunctions.containsKey(functionName);
    }
    
    private boolean existsSimpleFunction(String functionName) {
        return simpleFunctions.containsKey(functionName);
    }

    private boolean isFunction(final String functionName) {
        switch (functionName.toUpperCase(Locale.getDefault())) {
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
                return true;
            default:
                return false;
        }
    }
}
