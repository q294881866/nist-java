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

package org.jiumao.nist.random;

import org.jiumao.nist.Base.BaseRandom;
import org.jiumao.nist.Base.MathematicalFunctions;
import org.jiumao.nist.Base.RandomTestErrors;
import org.jiumao.nist.Base.RandomTests;
import org.jiumao.nist.CryptoRandomStream.BaseCryptoRandomStream;


/**
 * 全局通用统计测试
 * <P>
 * 检验主要是看匹配模块间的bit数。目的是检验序列能否在没有信息损耗的条件下被大大的压缩。一个能被大大压缩的序列被认为是一个非随机序列。
 * 
 * @author ppf@jiumao.org
 * @date 2018年3月22日
 */
public class Universal extends BaseRandom {

    /**
     * Random Test Class enumerator name
     */
    private static final RandomTests TEST = RandomTests.Universal;
    /**
     * Random Test Class minimum stream length
     */
    private static final int MINIMUMLENGTH = 387840;

    /**
     * Expected values for L values (as index)
     */
    private double[] EXPECTEDVALUE =
            { 0, 0, 0, 0, 0, 0, 5.2177052, 6.1962507, 7.1836656, 8.1764248, 9.1723243, 10.170032, 11.168765,
              12.168070, 13.167693, 14.167488, 15.167379 };
    /**
     * Variance values for L parameter (as index)
     */
    private double[] VARIANCE =
            { 0, 0, 0, 0, 0, 0, 2.954, 3.125, 3.238, 3.311, 3.356, 3.384, 3.401, 3.410, 3.416, 3.419, 3.421 };

    /**
     * L value corresponding to bit stream length in bits
     */
    protected int L;
    /**
     * Q value based on L value
     */
    protected int Q;
    /**
     * K value based on L, Q values and bit stream length in bits
     */
    protected int K;
    /**
     * "sigma" result
     */
    protected double sigma;
    /**
     * "phi" result
     */
    protected double phi;
    /**
     * "sum" result
     */
    protected double sum;
    /**
     * "expectedValueResult" result based on L result
     */
    protected double expectedValueResult;
    /**
     * expected "varianceResult" result based on L result
     */
    protected double varianceResult;
    /**
     * "bitsDiscarded" result, number of bits of stream that has been discarded
     * from random test
     */
    protected int bitsDiscarded;


    /**
     * Constructor, default
     */
    public Universal() {

        super();

        this.L = 0;
        this.Q = 0;
        this.K = 0;
        this.sigma = 0.0;
        this.phi = 0.0;
        this.sum = 0.0;
        this.expectedValueResult = 0.0;
        this.varianceResult = 0.0;
        this.bitsDiscarded = 0;
    }


    /**
     * Constructor with a MathematicalFunctions object instantiated
     * 
     * @param mathFuncObj mathematicalFunctions object that will be used by this
     *            object
     */
    public Universal(MathematicalFunctions mathFuncObj) {

        super(mathFuncObj);

        this.L = 0;
        this.Q = 0;
        this.K = 0;
        this.sigma = 0.0;
        this.phi = 0.0;
        this.sum = 0.0;
        this.expectedValueResult = 0.0;
        this.varianceResult = 0.0;
        this.bitsDiscarded = 0;
    }


    /**
     * Destructor, zeroes all data
     * 
     */
    public void finalize() {

        this.L = 0;
        this.Q = 0;
        this.K = 0;
        this.sigma = 0.0;
        this.phi = 0.0;
        this.sum = 0.0;
        this.expectedValueResult = 0.0;
        this.varianceResult = 0.0;
        this.bitsDiscarded = 0;
    }


    /**
     * Gets the BaseRandomTest random state of the last executed
     * BaseCryptoRandomStream
     * 
     * @return boolean indication if last computed CryptoRandomStream was a
     *         randomized stream true: last verified stream was randomized
     *         false: last verified stream was not randomized
     */
    public boolean IsRandom() {

        return super.IsRandom();
    }


    /**
     * Tests the BaseCryptoRandomStream executed and returns the random value
     * 
     * @param bitStream bitStream to be verified for randomness properties
     * @return boolean indication if CryptoRandomStream is a randomized stream
     *         true: last verified stream was randomized false: last verified
     *         stream was not randomized
     */
    public boolean isRandom(BaseCryptoRandomStream bitStream) {
        int i, j, p;
        double arg, sqrt2, c;
        int T[], decRep;

        if (bitStream.GetBitLength() < this.GetMinimumLength()) {
            this.error = RandomTestErrors.InsufficientNumberOfBits;
            this.random = false;
            this.pValue = 0.0;
            return this.random;
        }
        this.error = RandomTestErrors.NoError;
        bitStream.SetBitPosition(0);
        if (bitStream.GetBitLength() >= 1059061760)
            this.L = 16;
        else if (bitStream.GetBitLength() >= 496435200)
            this.L = 15;
        else if (bitStream.GetBitLength() >= 231669760)
            this.L = 14;
        else if (bitStream.GetBitLength() >= 107560960)
            this.L = 13;
        else if (bitStream.GetBitLength() >= 49643520)
            this.L = 12;
        else if (bitStream.GetBitLength() >= 22753280)
            this.L = 11;
        else if (bitStream.GetBitLength() >= 10342400)
            this.L = 10;
        else if (bitStream.GetBitLength() >= 4654080)
            this.L = 9;
        else if (bitStream.GetBitLength() >= 2068480)
            this.L = 8;
        else if (bitStream.GetBitLength() >= 904960)
            this.L = 7;
        else if (bitStream.GetBitLength() >= 387840)
            this.L = 6;

        this.Q = 10 * (int) Math.pow(2, this.L);
        this.K = (int) ((double) Math.floor((bitStream.GetBitLength() / this.L)) - (double) this.Q);
        if ((double) this.Q < 10 * Math.pow(2, this.L)) {
            this.random = false;
            this.error = RandomTestErrors.LOrQOutOfRange;
        }
        else {
            this.varianceResult = this.VARIANCE[this.L];
            this.expectedValueResult = this.EXPECTEDVALUE[this.L];
            this.bitsDiscarded = bitStream.GetBitLength() - (this.Q + this.K) * this.L;
            c = 0.7 - 0.8 / (double) this.L
                    + (4 + 32 / (double) this.L) * Math.pow(this.K, -3 / (double) this.L) / 15;
            this.sigma = c * Math.sqrt(this.varianceResult / (double) this.K);
            sqrt2 = Math.sqrt(2);
            this.sum = 0.0;
            p = (int) Math.pow(2, this.L);
            T = new int[p];
            if (T == null) {
                this.error = RandomTestErrors.InsufficientMemory;
                this.random = false;
                return this.random;
            }
            for (i = 0; i < p; i++)
                T[i] = 0;
            for (i = 1; i <= this.Q; i++) {
                decRep = 0;
                for (j = 0; j < this.L; j++)
                    decRep += bitStream.GetBitPosition((i - 1) * this.L + j)
                            * (long) Math.pow(2, this.L - 1 - j);
                T[decRep] = i;
            }
            for (i = this.Q + 1; i <= this.Q + this.K; i++) {
                decRep = 0;
                for (j = 0; j < this.L; j++) {
                    decRep += bitStream.GetBitPosition((i - 1) * this.L + j)
                            * (long) Math.pow(2, this.L - 1 - j);
                }
                this.sum += Math.log(i - T[decRep]) / Math.log(2);
                T[decRep] = i;
            }
            this.phi = this.sum / (double) this.K;
            arg = Math.abs(this.phi - this.expectedValueResult) / (sqrt2 * this.sigma);
            this.pValue = this.mathFuncs.ErFc(arg);
            if (this.mathFuncs.isNegative(this.pValue) || this.mathFuncs.isGreaterThanOne(this.pValue))
                this.error = RandomTestErrors.PValueOutOfRange;
            if (this.pValue < this.alpha) {
                this.random = false;
            }
            else {
                this.random = true;
            }
        }
        T = null;
        return this.random;
    }


    /**
     * Initializes the object
     * 
     */
    public void Initialize() {

        super.Initialize();
        this.L = 0;
        this.Q = 0;
        this.K = 0;
        this.sigma = 0.0;
        this.phi = 0.0;
        this.sum = 0.0;
        this.expectedValueResult = 0.0;
        this.varianceResult = 0.0;
        this.bitsDiscarded = 0;
    }


    /**
     * Gets the type of the object
     * 
     * @return RandomTests: the concrete type class of the random number test,
     *         Universal test for this class
     */
    public RandomTests GetType() {

        return Universal.TEST;
    }


    /**
     * Gets the minimum random stream length
     * 
     * @return int: minimum length in bits of streams that can be checked by
     *         this test
     */
    public int GetMinimumLength() {

        return Universal.MINIMUMLENGTH;
    }


    /**
     * Gets the "L" result
     * 
     * @return int: returns L value for stream length in bits
     */
    public int GetL() {

        return this.L;
    }


    /**
     * Gets the "Q" result
     * 
     * @return int: returns Q value based on L value
     */
    public int GetQ() {

        return this.Q;
    }


    /**
     * Gets the "K" result
     * 
     * @return int: returns K value based on L, Q values and stream in bits
     */
    public int GetK() {

        return this.K;
    }


    /**
     * Gets the "sigma" result
     * 
     * @return double: sigma result
     */
    public double GetSigma() {

        return this.sigma;
    }


    /**
     * Gets the "phi" result
     * 
     * @return double: phi result
     */
    public double GetPhi() {

        return this.phi;
    }


    /**
     * Gets the "sum" result
     * 
     * @return double: sum result
     */
    public double GetSum() {

        return this.sum;
    }


    /**
     * Gets the "expectedValue" result
     * 
     * @return double: expected value result for L value
     */
    public double GetExpectedValue() {

        return this.expectedValueResult;
    }


    /**
     * Gets the "variance" result
     * 
     * @return double: expected variance result for L value
     */
    public double GetVariance() {

        return this.varianceResult;
    }


    /**
     * Gets the "bitsDiscarded" result
     * 
     * @return int: number of bits of stream that have not been used for
     *         randomness test
     */
    public int GetBitsDiscarded() {

        return this.bitsDiscarded;
    }

}
