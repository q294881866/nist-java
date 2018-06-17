//
// Creator:    http://www.dicelocksecurity.com
// Version:    vers.5.0.0.1
//
// Copyright 2011 DiceLock Security, LLC. All rights reserved.
//
//                               DISCLAIMER
//
// THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
// AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
// REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
// PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
// OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
// WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
// OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
// ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
// 
// DICELOCK IS A REGISTERED TRADEMARK OR TRADEMARK OF THE OWNERS.
// 
// Environment:
// java version "1.6.0_29"
// Java(TM) SE Runtime Environment (build 1.6.0_29-b11)
// Java HotSpot(TM) Server VM (build 20.4-b02, mixed mode)
// 

package org.jiumao.nist.Base;

/**
 * mathematica funcions used by random number tests implemented
 * 
 * @author Angel Ferré @ DiceLock Security
 * @version 5.0.0.1
 * @since 2011-09-29
 */
public class MathematicalFunctions {

    /**
     * 2**-53
     */
    private static final double MACHEP = 1.11022302462515654042E-16;
    /**
     * 2**1024 * (1-MACHEP)
     */
    private static final double MAXNUM = 1.79769313486231570815E308;
    /**
     * log(MAXNUM)
     */
    private static final double MAXLOG = 7.09782712893383996732E2;
    /**
     * pi number
     */
    private static final double PI = 3.14159265358979323846;
    /**
     * log(pi)
     */
    private static final double LOGPI = 1.14472988584940017414;
    /**
     * log( sqrt( 2*pi )
     */
    private static final double LS2PI = 0.91893853320467274178;
    /**
     * BIG number
     */
    private static final double BIG = 4.503599627370496e15;
    /**
     * inverse BIG number
     */
    private static final double BIGINV = 2.22044604925031308085e-16;

    /**
     * A[]: Stirling's formula expansion of log gamma
     */
    double A_LGAM[] = { 8.11614167470508450300E-4, -5.95061904284301438324E-4, 7.93650340457716943945E-4,
                        -2.77777777730099687205E-3, 8.33333333333331927722E-2 };
    /**
     * B[], C[]: log gamma function between 2 and 3
     */
    double B_LGAM[] = { -1.37825152569120859100E3, -3.88016315134637840924E4, -3.31612992738871184744E5,
                        -1.16237097492762307383E6, -1.72173700820839662146E6, -8.53555664245765465627E5 };
    /**
     * B[], C[]: log gamma function between 2 and 3
     */
    double C_LGAM[] = { -3.51815701436523470549E2, -1.70642106651881159223E4, -2.20528590553854454839E5,
                        -1.13933444367982507207E6, -2.53252307177582951285E6, -2.01889141433532773231E6 };
    private static final double MAXLGM = 2.556348e305;

    /**
     * boolean "error" result indicating if an error has been produced while
     * operating true: an error has been produced false: no error has been
     * produced
     */
    protected boolean error;
    protected MathematicalErrors mathError;


    /**
     * Constructor, default
     */
    public MathematicalFunctions() {

        super();
    }


    /**
     * Logarithm of gamma function
     * 
     * @param x x value to evaluate logarithm of gamma function
     * @return double: logarithm of gamma of value "x"
     */
    public double LGamma(double x) {
        double p, q, u, w, z;
        int i;
        int sgngam;
        boolean loverf;

        sgngam = 1;
        this.error = false;
        this.mathError = MathematicalErrors.None;
        loverf = false;
        if (x < -34.0) {
            q = -x;
            w = this.LGamma(q); /* note this modifies sgngam! */
            p = Math.floor(q);
            if (p == q) {
                loverf = true;
            }
            else {
                i = (int) p;
                if ((i & 1) == 0)
                    sgngam = -1;
                else
                    sgngam = 1;
                z = q - p;
                if (z > 0.5) {
                    p += 1.0;
                    z = p - q;
                }
                z = q * Math.sin(MathematicalFunctions.PI * z);
                if (z == 0.0) {
                    loverf = true;
                }
                else {
                    z = MathematicalFunctions.LOGPI - Math.log(z) - w;
                    return (z);
                }
            }
        }
        if (x < 13.0) {
            z = 1.0;
            p = 0.0;
            u = x;
            while (u >= 3.0) {
                p -= 1.0;
                u = x + p;
                z *= u;
            }
            while ((u < 2.0) && !loverf) {
                if (u == 0.0) {
                    loverf = true;
                }
                else {
                    z /= u;
                    p += 1.0;
                    u = x + p;
                }
            }
            if (!loverf) {
                if (z < 0.0) {
                    sgngam = -1;
                    z = -z;
                }
                else
                    sgngam = 1;
                if (u == 2.0)
                    return (Math.log(z));
                p -= 2.0;
                x = x + p;
                p = x * this.PolEvl(x, this.B_LGAM, 5) / this.P1Evl(x, this.C_LGAM, 6);
                return (Math.log(z) + p);
            }
        }
        if ((x > MathematicalFunctions.MAXLGM) || loverf) {
            this.error = true;
            this.mathError = MathematicalErrors.Overflow;
            return (sgngam * MathematicalFunctions.MAXNUM);
        }
        q = (x - 0.5) * Math.log(x) - x + MathematicalFunctions.LS2PI;
        if (x > 1.0e8)
            return (q);
        p = 1.0 / (x * x);
        if (x >= 1000.0)
            q += ((7.9365079365079365079365e-4 * p - 2.7777777777777777777778e-3) * p
                    + 0.0833333333333333333333) / x;
        else
            q += this.PolEvl(p, this.A_LGAM, 4) / x;
        return (q);
    }


    /**
     * Incomplete gamma function
     * 
     * @param a a value to evaluate incomplete gamma function
     * @param x x value to evaluate incomplete gamma function
     * @return double: incomplete gamma of values "a" and "x"
     */
    public double IGamma(double a, double x) {
        double ans, ax, c, r;

        this.error = false;
        this.mathError = MathematicalErrors.None;
        if ((x <= 0) || (a <= 0))
            return (0.0);
        if ((x > 1.0) && (x > a))
            return (1.e0 - this.IGammaC(a, x));
        ax = a * Math.log(x) - x - this.LGamma(a);
        if (ax < -(MathematicalFunctions.MAXLOG)) {
            this.error = true;
            this.mathError = MathematicalErrors.Underflow;
            return (0.0);
        }
        ax = Math.exp(ax);
        r = a;
        c = 1.0;
        ans = 1.0;
        do {
            r += 1.0;
            c *= x / r;
            ans += c;
        } while (c / ans > MathematicalFunctions.MACHEP);
        return (ans * ax / a);
    }


    /**
     * Complemented incomplete gamma integral
     * 
     * @param a a value to evaluate complemented incomplete gamma integral
     * @param x x value to evaluate complemented incomplete gamma integral
     * @return double: complemented incomplete gamma integral of values "a" and
     *         "x"
     */
    public double IGammaC(double a, double x) {
        double ans, ax, c, yc, r, t, y, z;
        double pk, pkm1, pkm2, qk, qkm1, qkm2;

        this.error = false;
        this.mathError = MathematicalErrors.None;
        if ((x <= 0) || (a <= 0))
            return (1.0);
        if ((x < 1.0) || (x < a))
            return (1.e0 - this.IGamma(a, x));
        ax = a * Math.log(x) - x - this.LGamma(a);
        if (ax < -(MathematicalFunctions.MAXLOG)) {
            this.error = true;
            this.mathError = MathematicalErrors.Underflow;
            return (0.0);
        }
        ax = Math.exp(ax);
        y = 1.0 - a;
        z = x + y + 1.0;
        c = 0.0;
        pkm2 = 1.0;
        qkm2 = x;
        pkm1 = x + 1.0;
        qkm1 = z * x;
        ans = pkm1 / qkm1;
        do {
            c += 1.0;
            y += 1.0;
            z += 2.0;
            yc = y * c;
            pk = pkm1 * z - pkm2 * yc;
            qk = qkm1 * z - qkm2 * yc;
            if (qk != 0) {
                r = pk / qk;
                t = Math.abs((ans - r) / r);
                ans = r;
            }
            else
                t = 1.0;
            pkm2 = pkm1;
            pkm1 = pk;
            qkm2 = qkm1;
            qkm1 = qk;
            if (Math.abs(pk) > MathematicalFunctions.BIG) {
                pkm2 *= MathematicalFunctions.BIGINV;
                pkm1 *= MathematicalFunctions.BIGINV;
                qkm2 *= MathematicalFunctions.BIGINV;
                qkm1 *= MathematicalFunctions.BIGINV;
            }
        } while (t > MathematicalFunctions.MACHEP);
        return (ans * ax);
    }


    /**
     * Evaluate polynomial of degree N
     * 
     * @param x x value to evaluate polynomial
     * @param coef coef array of double values to evaluate polynomial
     * @param N N degree to evaluate polynomial
     * @return double: polynomial of degree N of values "x" and "coef" array
     */
    public double PolEvl(double x, double coef[], int N) {
        double ans;
        double p[];
        int i;
        int index;

        index = 0;
        p = coef;
        ans = p[index++];
        i = N;
        do
            ans = ans * x + p[index++];
        while ((--i) != 0);
        return (ans);
    }


    /**
     * Evaluate polynomial when coefficient of x is 1.0.
     * 
     * @param x x value to evaluate polynomial when it is 1.0
     * @param coef coef array of double values to evaluate polynomial when x
     *            value is 1.0
     * @param N N degree to evaluate polynomial when x value is 1.0
     * @return double: polynomial of degree N of values "x" and "coef" array
     *         when x value is 1.0
     */
    public double P1Evl(double x, double coef[], int N) {
        double ans;
        double p[];
        int i;
        int index;

        index = 0;
        p = coef;
        ans = x + p[index++];
        i = N - 1;
        do
            ans = ans * x + p[index++];
        while ((--i) != 0);
        return (ans);
    }


    /**
     * Error function in double precision
     * 
     * @param x x value to evaluate error function in double precision
     * @return double: error function in double precision of value "x"
     */
    public double ErF(double x) {
        final double TWO_SQRTPI = 1.128379167095512574;
        final double REL_ERROR = 1E-12;
        double sum = x, term = x, xsqr = x * x;
        int j = 1;

        if (Math.abs(x) > 2.2)
            return 1.0 - this.ErFc(x);
        do {
            term *= xsqr / j;
            sum -= term / (2 * j + 1);
            j++;
            term *= xsqr / j;
            sum += term / (2 * j + 1);
            j++;
        } while (Math.abs(term) / sum > REL_ERROR);
        return TWO_SQRTPI * sum;
    }


    /**
     * Error function in double precision
     * 
     * @param x x value to evaluate error function in double precision
     * @return double: error function in double precision of value "x"
     */
    public double ErFc(double x) {
        final double ONE_SQRTPI = 0.564189583547756287;
        final double REL_ERROR = 1E-12;
        double a = 1, b = x, c = x, d = x * x + 0.5;
        double q1, q2 = b / d, n = 1.0, t;

        if (Math.abs(x) < 2.2)
            return 1.0 - this.ErF(x);
        if (x < 0)
            return 2.0 - this.ErFc(-x);
        do {
            t = a * n + b * x;
            a = b;
            b = t;
            t = c * n + d * x;
            c = d;
            d = t;
            n += 0.5;
            q1 = q2;
            q2 = b / d;
        } while (Math.abs(q1 - q2) / q2 > REL_ERROR);
        return ONE_SQRTPI * Math.exp(-x * x) * q2;
    }


    /**
     * Statistical Normal function
     * 
     * @param x x value to evaluate statistical normal function
     * @return double: statistical normal function of value "x"
     */
    public double Normal(double x) {
        final double SQRT2 = 1.414213562373095048801688724209698078569672;
        double arg, result;

        if (x > 0) {
            arg = x / SQRT2;
            result = 0.5 * (1 + this.ErF(arg));
        }
        else {
            arg = -x / SQRT2;
            result = 0.5 * (1 - this.ErF(arg));
        }
        return (result);
    }


    /**
     * Get mathematical error boolean if last executed function failed
     * 
     * @return boolean: boolean indicating if last executed function produced an
     *         error
     */
    public boolean GetError() {

        return this.error;
    }


    /**
     * Get mathematical error boolean if last executed function failed
     * 
     * @return MathematicalErrors: MathematicalErrors enumeration indicating
     *         error of last executed function
     */
    public MathematicalErrors GetMathError() {

        return this.mathError;
    }


    // DEFINES AS METHODS
    /**
     * Returns the greater of two double values
     * 
     * @param x double x value to evaluate greater value
     * @param y double y value to evaluate greater value
     * @return double: returns greater value of "x" and "y"
     */
    public double max(double x, double y) {

        return ((x) < (y) ? (y) : (x));
    }


    /**
     * Returns the lesser of two double values
     * 
     * @param x double x value to evaluate greater value
     * @param y double y value to evaluate greater value
     * @return double: returns greater value of "x" and "y"
     */
    public double min(double x, double y) {

        return ((x) > (y) ? (y) : (x));
    }


    /**
     * Returns if a double is non positive
     * 
     * @param x double x value to evaluate if it is non positive
     * @return boolean: true: if it is non positive false: if it is positive
     */
    public double isNonPositive(double x) {

        return ((x) <= 0.e0 ? 1 : 0);
    }


    /**
     * Returns if a double is positive
     * 
     * @param x double x value to evaluate if it is positive
     * @return boolean: true: if it is positive false: if it is non positive
     */
    public boolean isPositive(double x) {

        return ((x) > 0.e0 ? true : false);
    }


    /**
     * Returns if a double is negative
     * 
     * @param x double x value to evaluate if it is negative
     * @return boolean: true: if it is negative false: if it is non negative
     */
    public boolean isNegative(double x) {

        return ((x) < 0.e0 ? true : false);
    }


    /**
     * Returns if a double is greater than 1
     * 
     * @param x double x value to evaluate if it is greater than 1
     * @return boolean: true: if it is greater than 1 false: if it is non
     *         greater than 1
     */
    public boolean isGreaterThanOne(double x) {

        return ((x) > 1.e0 ? true : false);
    }


    /**
     * Returns if a double equals 0
     * 
     * @param x double x value to evaluate if it equals 0
     * @return boolean: true: if it equals 0 false: if it no equals 0
     */
    public boolean isZero(double x) {

        return ((x) == 0.e0 ? true : false);
    }


    /**
     * Returns if a double equals 1
     * 
     * @param x double x value to evaluate if it equals 1
     * @return boolean: true: if it equals 1 false: if it no equals 1
     */
    public boolean isOne(double x) {

        return ((x) == 1.e0 ? true : false);
    }


    public double Pr(int u, double eta) {
        int l;
        double sum, p;

        if (u == 0)
            p = Math.exp(-eta);
        else {
            sum = 0.0;
            for (l = 1; l <= u; l++)
                sum += Math.exp(-eta - u * Math.log(2) + l * Math.log(eta) - LGamma(l + 1) +  LGamma(u)
                        - LGamma(l) - LGamma(u - l + 1));
            p = sum;
        }
        return p;
    }

}
