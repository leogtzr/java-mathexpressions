MathExpressions v1.3

		

MathExpressions, a library to evaluate math expressions.



Parser parser = new Parser(); 
try { 
	parser.parse("1.2 * sin(pi+2) - cos(factorial(5))"); 
	System.out.println(parser.getNumericAnswer());
} catch(ParsingException ex) { 
	System.out.println(ex); 
}


check out the doc for more information.