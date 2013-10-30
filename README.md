/***************************************
        MathExpressions v1.3
***************************************/

MathExpressions, a library to evaluate math expressions.



Evaluate an expression | Evaluar una expresión

Parser parser = new Parser(); 
try { 
	parser.parse("1.2 * sin(pi+2) - cos(factorial(5))"); 
	System.out.println(parser.getNumericAnswer());
} catch(ParsingException ex) { 
	System.out.println(ex); 
}


Working with variables | Trabajo con variables

You can add variables with Parser.addVariable(String name, double value) 

Parser parser = new Parser(); 
parser.addVariable("x", 0.3452);


or you can modify the list of variables directly with:

parser.getUserVars()



Domain check for functions | Verificación de dominio para las funciones.

MathExpressions v1.3 has the following functions:
abs
exp
sign
sqrt
raiz
log
ln
log10
sin
cos
tan
asin
acos
atan
factorial
cot
sec
csc
sinh
cosh
tanh
coth
sech
csch
acsc
asec
asinh
acosh
acsch
atanh
power
max
min
mod
acot
acoth
log2
rand
asech
MathExpressions checks the domain for every function, example: 
try { 
    parser.parse("acos(1.2)+2.5"); 
    System.out.println(parser.getNumericAnswer()); 
} catch(ParsingException ex) { 
     System.out.println(ex); 
} 
it throws an exception mathparser.ParsingException: Fuera de dominio para la función 'acos': 1.2
Custom Functions | Funciones personalizadas (definidas por el usuario).

You can extend the parser implementing the function functionCode from the CustomFunction class. 

Example:
Parser parser = new Parser(); 

CustomFunction cfSum = new CustomFunction("sumatoria") {
    @Override
    public double functionCode(double begin, double end) {
        long sum = 0;
        for(int i = (int)begin; i <= (int)end; i++) {
            sum += i;
        }
        return (double)sum;
    }
};

parser.addCustomFunction("sumatoria", cfSum);

try {
     // The function now is available
     parser.parse("sumatoria(1, 10)^2 + sin(pi-2)");
     System.out.println(parser.getNumericAnswer());
} catch(ParsingException ex) {
     System.out.println(ex);
}

you can also extend the parser with a SimpleFunction (function with only an argument), example: 

Parser parser = new Parser();

SimpleFunction fibFunc = new SimpleFunction("fibonacci") {
     @Override
     public double functionCode(double a) {
          double i = 1.0;
        double j = 0.0;
        for(int k = 1; k <= a; k++) {
            j += i;
            i = j - i;
        }
        return j;
    }
};

parser.addSimpleFunction("fibonacci", fibFunc);
try {
    // The function now is available
    parser.parse("sqrt(fibonacci(8)+4)");
    System.out.println(parser.getNumericAnswer());
} catch(ParsingException ex) {
    System.out.println(ex);
}
Operators | Operadores

The following operators are supported:
+
-
*
/
^
%
=
==
!=
<
>
<=
>=
&&
||
Author

Leonardo Gutiérrez Ramírez 

leorocko13@hotmail.com 
leogutierrezramirez@gmail.com