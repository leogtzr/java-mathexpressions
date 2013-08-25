package mathparser;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.cosh;
import static java.lang.Math.log;
import static java.lang.Math.log10;
import static java.lang.Math.sin;
import static java.lang.Math.sinh;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;
import static java.lang.Math.tanh;
import java.util.Random;

/**
 * Contiene funciones trigonométricas extra.
 * @author Leo Gutiérrez Ramírez | leogutierrezramirez@gmail.com
 * <br/>
 * 
 * <p>
 * Los resultados de las funciones trigonométricas se dan en radianes.
 * </p>
 * <br/>
 * <p>
 * El código realiza comprobaciones para rango de dominio para las funciones
 * trigonométricas pero esto no es exacto, debido a que se trata con valores
 * numéricos doubles.
 * <br/>
 * El valor del <b>epsilon</b> queda a criterio del programador.
 * <br/>
 * </p>
 * 
 * @author <a href="mailto:leogutierrezramirez@gmail.com"><b>Leo Gutiérrez R.</b></a>
 */
public class MathFunctions {

    private static Random random;
    
    private MathFunctions() {}
    
    static {
        random = new Random(new java.util.Date().getTime());
    }
    
    private static final double EPSILON = 1.0E-15;

    /**
     * Seno.
     * @throws ParsingException.
     */
    public static double math_sin(double x) {
        return (((x % PI) - 0.0) < EPSILON) ? 0.0 : sin(x);
    }

    /**
     * Factorial.
     * @throws ParsingException si se sale del rango de un double.
     */
    public static double factorial(double value) throws ParsingException {

        double res;
        int v = (int) value;

        if (v < 0.0) {
            throw new ParsingException("Valor enterado esperado para la función factorial");
        }

        res = v;
        v--;
        while (v > 1) {
            res *= v;
            v--;
        }

        if (res == 0) {
            res = 1.0;        // 0! is per definition 1
        }
        
        if(Double.isNaN(res) || Double.isInfinite(res)) {
            throw new ParsingException("Número fuera de los límites de un double.");
        }
        
        return res;
    }

    /**
     * Logaritmo natural.
     * @throws ParsingException Si se sale del dominio de la función.
     */
    public static double logarithm(double x) throws ParsingException {
        double result = log(x);

        if (Double.isInfinite(result) || Double.isNaN(result)) {
            throw new ParsingException("Número fuera de dominio: " + result);
        }
        return result;
    }

    /**
     * Arcotangente.
     */
    public static double math_acot(double x) {
        return ((PI / 2.0) - abs(atan(x)));
    }

    /**
     * Arcoseno hiperbólico.
     * @throws ParsingException Si se sale del dominio de la función. 
     */
    public static double math_acosh(double x) throws ParsingException {
        if (x >= 1.0) {
            double result = log(x + sqrt((x * x) - 1.0));
            if (!Double.isInfinite(result) || !Double.isNaN(result)) {
                return result;
            } else {
                throw new ParsingException("Fuera de dominio: " + x);
            }
        } else {
            throw new ParsingException("Fuera de dominio: " + x);
        }
    }

    /**
     * Arcoseno.
     * @throws ParsingException Si se sale del dominio de la función.
     */
    public static double math_acos(double x) throws ParsingException {
        if (x >= -1.0 && x <= 1.0) {
            double result = acos(x);
            if (!Double.isInfinite(result) && !Double.isNaN(result)) {
                return result;
            } else {
                throw new ParsingException("Fuera de dominio para la función 'acos': " + x);
            }
        } else {
            throw new ParsingException("Fuera de dominio para la función 'acos': " + x);
        }
    }

    /**
     * Secante inverso hiperbólico.
     * @throws ParsingException Si se sale del dominio de la función.
     */
    public static double math_asech(double x) throws ParsingException {

        if (x < 0.0) {
            throw new ParsingException("Fuera de rango: " + x);
        }
        if (x < 0.00000000000001) {
            throw new ParsingException("Fuera de rango: " + x);
        }
        return math_acosh(1.0 / x);
    }

    /**
     * Tangente inverso.
     */
    public static double math_atan(double x) {
        if (abs(x - 0.0) < EPSILON) {
            return 0.0;
        }
        return atan(x);
    }

    /**
     * Seno inverso hiperbólico.
     * @throws ParsingException Si se sale del dominio de la función.
     */
    public static double math_asinh(double x) throws ParsingException {
        if (abs(x - 0.0) < EPSILON) {
            return 0.0;
        }

        double result = log(x + sqrt((x * x) + 1.0));

        if (Double.isInfinite(result) || Double.isNaN(result)) {
            throw new ParsingException("Número fuera de dominio para la función asinh:" + x);
        }

        return result;
    }

    /**
     * Tangente inverso hiperbólico.
     * @throws ParsingException Si se sale del dominio de la función.
     */
    public static double math_atanh(double x) throws ParsingException {
        if (abs(x - 0.0) < EPSILON) {
            return 0.0;
        }
        if (abs(x) >= 1.0) {
            throw new ParsingException("Fuera de dominio: " + x);
        }
        double y = (1.0 / 2.0) * log((1.0 + x) / (1.0 - x));
        if (!Double.isNaN(y) || !Double.isInfinite(y)) {
            return y;
        } else {
            throw new ParsingException("Fuera de dominio:" + x);
        }
    }

    /**
     * Cotangente inverso hiperbólico.
     * @throws ParsingException Si se sale del dominio de la función.
     */
    public static double math_acoth(double x) throws ParsingException {
        if (abs(x) < 1.0) {
            throw new ParsingException("Fuera de dominio para la función 'acoth': " + x);
        }
        double result = 0.5 * (log((x + 1.0) / x) - log((x - 1.0) / x));
        if (Double.isNaN(result)) {
            throw new ParsingException("Fuera de dominio para la función 'acoth': " + x);
        }
        return result;
    }

    /**
     * Seno inverso.
     * @throws ParsingException Si se sale del dominio de la función.
     */
    public static double math_asin(double x) throws ParsingException {
        /// XXX Warning.
        if (abs(x - 0.0) < EPSILON) {
            return 0.0;
        }
        if (x < -1.0 || x > 1.0) {
            throw new ParsingException("Fuera de dominio para la función 'asinh': " + x);
        }

        double result = asin(x);
        if (!Double.isNaN(result) || !Double.isInfinite(result)) {
            return result;
        } else {
            throw new ParsingException("Número fuera de dominio: " + x);
        }
    }

    /**
     * Cosecante inverso.
     * @throws ParsingException Si se sale del dominio de la función.
     */
    public static double math_acsc(double x) throws ParsingException {
        if (x <= 1.0 && x >= 1.0) {
            throw new ParsingException("Fuera de dominio para la función 'acoth': " + x);
        }
        return math_asin(1.0 / x);
    }

    /**
     * Cosecante inverso hiperbólico.
     * @throws ParsingException Si se sale del dominio de la función.
     */
    public static double math_acsch(double x) throws ParsingException {

        if (abs(x - 0.0) < EPSILON) {
            throw new ParsingException("Fuera de dominio." + x);
        }
        double result = log(sqrt(1.0 + (1.0 / (x * x))) + (1.0 / x));
        if (Double.isNaN(result)) {
            throw new ParsingException("Fuera de dominio." + result);
        }
        return result;
    }

    /**
     * Secante inverso.
     * @throws ParsingException Si se sale del dominio de la función.
     */
    public static double math_asec(double x) throws ParsingException {
		
        double y = acos(1.0 / x);
        if (!Double.isInfinite(y) || !Double.isNaN(y)) {
            return y;
        } else {
            throw new ParsingException("Fuera de dominio para la función asec: " + x);
        }
    }
    
    /**
     * Cotangente.
     * @throws ParsingException Si se sale del dominio de la función.
     */
    public static double math_cot(double x) throws ParsingException {
		double result;

        double b = (cos(2 * x) - 1.0);
        if(abs(b - 0.0) < EPSILON) {
            throw new ParsingException("Fuera de rango." + x);
        }
        result = -(sin(2 * x)/b);
        return result;
	}
    
    /**
     * Cotangente hiperbólico.
     * @throws ParsingException Si se sale del dominio de la función.
     */
    public static double math_coth(double x) throws ParsingException {
		if(abs(x - 0.0) < EPSILON) {
            throw new ParsingException("Fuera de rango." + x);
		} else {
			return (1.0 / tanh(x));
		}
	}
    
    /**
     * Cosecante.
     * @throws ParsingException Si se sale del dominio de la función.
     */
    public static double math_csc(double x) throws ParsingException {
		if(abs((abs(x) % PI) - 0.0) < EPSILON) {
            throw new ParsingException("Fuera de rango." + x);
		} else {
			return (1.0 / sin(x));
		}
	}
    
    /**
     * Cosecante hiperbólico.
     * @throws ParsingException Si se sale del dominio de la función.
     */
    public static double math_csch(double x) throws ParsingException {
		 if((abs(sinh(x) - 0.0) < EPSILON) ||
			(abs(x - 0.0) < EPSILON)) {
             throw new ParsingException("Fuera de rango." + x);
		 } else {
			 return (1.0 / sinh(x));
		 }
	}
    
    /**
     * Logaritmo.
     * @throws ParsingException Si se sale del dominio de la función.
     */
    public static double math_log(double x) throws ParsingException {
		if(abs(x) < 0.0) {
            throw new ParsingException("Fuera de dominio." + x);
        }
		if(x > 0.0) {
			return log(x);
		} else {
            throw new ParsingException("Fuera de dominio." + x);
        }
	}

    /**
     * Logaritmo en base 10.
     * @throws ParsingException Si se sale del dominio de la función.
     */
	public static double math_log10(double x) throws ParsingException {
		if(x < 0.0) {
            throw new ParsingException("Fuera de rango." + x);
		} else if(x == 0.0 || abs(x) < 0.00000000000001) {
            throw new ParsingException("Fuera de rango." + x);
		} else {
			return log10(x);
        }
	}

    /**
     * Logaritmo en base 2.
     * @throws ParsingException Si se sale del dominio de la función.
     */
	public static double math_log2(double x) throws ParsingException {
		if(x < 0.0) {
            throw new ParsingException("Fuera de rango." + x);
		} else if(x == 0.0 || abs(x) < 0.00000000000001) {
            throw new ParsingException("Fuera de rango." + x);
		} else {
			return log(x) / log(2.0);
		}
	}

    /**
     * Secante.
     * @throws ParsingException Si se sale del dominio de la función.
     */
	public static double math_sec(double x) throws ParsingException {
		if(abs(cos(x)) < 0.00000000000001) {
			throw new ParsingException("Fuera de rango." + x);
		} else {
            return 1.0 / cos(x);
        }
	}

    /**
     * Secante hiperbólico.
     * @throws ParsingException Si se sale del dominio de la función.
     */
	public static double math_sech(double x) throws ParsingException {
		return ((2.0 * cosh(x)) / (cosh(2.0 * x) + 1.0));
	}

    /**
     * Raíz cuadrada.
     * @throws ParsingException si el argumento es negativo.
     */
	public static double math_sqrt(double x) throws ParsingException {
		if(abs(x) < EPSILON) {
			return 0.0;
		}
		if(x > 0.0) {
			return sqrt(x);
		} else {
            throw new ParsingException("Fuera de rango." + x);
		}
	}

    /**
     * Tangente.
     * @throws ParsingException Si se sale del dominio de la función.
     */
	public static double math_tan(double x) throws ParsingException {
		if((x % PI) == (PI / 2.0)) {
            throw new ParsingException("Fuera de rango." + x);
		} else if(abs((x % PI)) < EPSILON) {
			return 0.0;
		} else {
            return tan(x);
        }
    }

	/**
     * Devuelve un número aleatorio entre 0 y 1.
     */
    public static double rand_0_to_1() {
        return random.nextDouble();
	}

    /**
     * Devuelve un número aleatorio entre los números especificados.
     */
	public static int rand_int_between(int min, int max) {
        return random.nextInt(max - min + 1) + min;
	}
    
}
