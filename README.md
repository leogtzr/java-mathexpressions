MathExpressions v1.3

		

MathExpressions, a library to evaluate math expressions.


```java
Parser parser = new Parser(); 

try { 
	parser.parse("1.2 * sin(pi+2) - cos(factorial(5))"); 

	System.out.println(parser.getNumericAnswer());

} catch(ParsingException ex) { 

	System.out.println(ex); 
}
```

Custom Functions and variables:

```java
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
parser.addVariable("x", 8);

try {
    parser.parse("sqrt(fibonacci(x)+4)");
    double result = parser.getNumericAnswer();
    System.out.println(result);
} catch(ParsingException ex) {
    System.out.println(ex);
}
```


Operators

```
[x] +
[x] -
[x] *
[x] /
[x] ^
[x] %
[x] =
[x] ==
[x] !=
[x] <
[x] << bit shift left
[x] >
[x] >> bit shift right
[x] <=
[x] >=
[x] &&
[x] ||
```

Functions

```
[x] abs
[x] exp
[x] sign
[x] sqrt
[x] raiz
[x] log
[x] ln
[x] log10
[x] sin
[x] cos
[x] tan
[x] asin
[x] acos
[x] atan
[x] factorial
[x] cot
[x] sec
[x] csc
[x] sinh
[x] cosh
[x] tanh
[x] coth
[x] sech
[x] csch
[x] acsc
[x] asec
[x] asinh
[x] acosh
[x] acsch
[x] atanh
[x] power
[x] max
[x] min
[x] mod
[x] acot
[x] acoth
[x] log2
[x] rand
[x] asech
```

check out the doc for more information.
