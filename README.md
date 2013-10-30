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


check out the doc for more information.


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
[x] >
[x] <=
[x] >=
[x] &&
[x] ||
```