package mathparser;

/**
 * Lexer for the Parser.
 * @author Leo Gutiérrez Ramírez | leogutierrezramirez@gmail.com
 */
public class Lexer {

    public enum TokenType {
        NADA, DELIMITADOR, NUMERO, VARIABLE, FUNCION, DESCONOCIDO, OPERADOR
    }
    
    /**
     * Representa la expresión a interpretar para un manejo más fácil.
     */
    private char[] expression = null;
    
    private TokenType tipoTokenActual = TokenType.NADA;

    public TokenType getCurrentType() {
        return tipoTokenActual;
    }
    
    /**
     * Contiene el caracter actual leído.
     */
    private int i = 0;
    
    public int getPos() {
        return i;
    }
    
    /**
     * El token actual obtenido de la expresión.
     */
    private String token = "";

    public String getToken() {
        return token;
    }
    
    public Lexer() {
        this.i = 0;
        tipoTokenActual = TokenType.NADA;
    }
    
    public Lexer(String expr) {
        tipoTokenActual = TokenType.NADA;
        this.expression = (expr.replaceAll("\\s+", "") + '\u0000').toCharArray();
    }
    
    public boolean validateParentheses() {
        
        int openBraces = 0;
        for(char c : this.expression) {
            openBraces += c == '(' ? 1 : (c == ')') ? -1 : 0;
        }
        return openBraces == 0;
    }
    
    public void init(final String expr) throws ParsingException {
        
        this.expression = (expr.replaceAll("\\s+", "") + '\u0000').toCharArray();
        
        if(!validateParentheses()) {
            throw new ParsingException("unbalanced parentheses");
        }
        
        i = 0;
    }
    
    private boolean isDelimeter(final char c) {
        return "&|<>=+/*%^!,".indexOf(c) != -1;
    }

    private boolean isDigitDot(final char c) {
        return "0123456789.".indexOf(c) != -1;
    }

    private boolean isDigit(final char c) {
        return Character.isDigit(c);
    }

    private boolean isAlpha(final char c) {
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZ_".indexOf(Character.toUpperCase(c)) != -1;
    }
    
    public void nextToken() throws ParsingException {

        tipoTokenActual = TokenType.NADA;
        token = "";

        if(expression[i] == ' ' || expression[i] == '\t' || 
                expression[i] == '\n') {
            i++;
        }

        if(expression[i] == '\u0000') {
            tipoTokenActual = TokenType.DELIMITADOR;
            return;
        }

        if(expression[i] == '-') {
            tipoTokenActual = TokenType.DELIMITADOR;
            token += '-';
            i++;
            return;
        }

        // Paréntesis:
        if(expression[i] == ')' || expression[i] == '(') {
            tipoTokenActual = TokenType.DELIMITADOR;
            token += expression[i];
            i++;
            return;
        }
        
        if(isDelimeter(expression[i])) {
            tipoTokenActual = TokenType.DELIMITADOR;
            switch(expression[i]) {
                case ',':
                    token = expression[i] + "";
                    i++;
                    return;
                case '+':
                    token = expression[i] + "";
                    i++;
                    return;
                case '-':
                    token = expression[i] + "";
                    i++;
                    return;
                case '*':
                    token = expression[i] + "";
                    i++;
                    return;
                case '/':
                    token = expression[i] + "";
                    i++;
                    return;
                case '<':
                    token = expression[i] + "";
                    i++;
                    if(expression[i] == '=') {
                        token += expression[i] + "";
                        i++;
                    } 
                    return;
                case '>':
                    token = expression[i] + "";
                    i++;
                    if(expression[i] == '=') {
                        token += expression[i] + "";
                        i++;
                    } 
                    return;
                case '=':
                    token = expression[i] + "";
                    i++;
                    if(expression[i] == '=') {
                        token += expression[i] + "";
                        i++;
                    } 
                    return;
                case '!':
                    token = expression[i] + "";
                    i++;
                    if(expression[i] == '=') {
                        token += expression[i] + "";
                        i++;
                    } 
                    return;
                    
                case '&':
                    token = expression[i] + "";
                    i++;
                    if(expression[i] == '&') {
                        token += expression[i] + "";
                        i++;
                    } else {
                        throw new ParsingException("OPERADOR INCORRECTO " + token, i);
                    }
                    return;
                    
                case '|':
                    token = expression[i] + "";
                    i++;
                    if(expression[i] == '|') {
                        token += expression[i] + "";
                        i++;
                    } else {
                        throw new ParsingException("OPERADOR INCORRECTO " + token, i);
                    }
                    return;
                    
                case '^':
                    token = expression[i] + "";
                    i++;
                    return;
                    
            }
        }
        
        if (isDigitDot(expression[i])) {
            tipoTokenActual = TokenType.NUMERO;
            while (isDigit(expression[i])) {
                token += expression[i];
                i++;
            }
            if (expression[i] == '.') {
                token += '.';
                i++;
            }
            while (isDigit(expression[i])) {
                token += expression[i];
                i++;
            }
            if (Character.toUpperCase(expression[i]) == 'E') {
                token += 'E';
                i++;

                if (expression[i] == '+' || expression[i] == '-') {
                    token += expression[i];
                    i++;
                }

                while (isDigit(expression[i])) {
                    token += expression[i];
                    i++;
                }

            }
            return;
        }
        
        if (isAlpha(expression[i])) {
            while (isAlpha(expression[i]) || isDigit(expression[i])) {
                token += expression[i];
                i++;
            }
            // Verificar si es función o variable:
            if (expression[i] == '(') {
                tipoTokenActual = TokenType.FUNCION;
            } else {
                //System.out.println("Var: " + token);
                tipoTokenActual = TokenType.VARIABLE;
            }
            return;
        }
        
    }
    
    public void setType(TokenType type) {
        this.tipoTokenActual = type;
    }
    
    public void setIndex(int index) {
        this.i = index;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
}
