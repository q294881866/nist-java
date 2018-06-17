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
 * 块内最长连续“1”测试
 * <p>
 * 该检验主要是看长度为M-bits的子块中的最长“1”游程。
 * 这项检验的目的是判定待检验序列的最长“1”游程的长度是否同随机序列的相同。
 * 注意：最长“1”游程长度上的一个不规则变化意味着相应的“0”游程长度上也有一个不规则变化，因此，仅仅对“1”游程进行检验室足够的。
 * 
 * @author ppf@jiumao.org
 * @date 2018年3月22日
 */
public class LongestRunOfOnes extends BaseRandom {

    /**
     * Random Test Class enumerator name
     */
    protected static final RandomTests TEST = RandomTests.LongestRunOfOnes;
    /**
     * Random Test Class minimum stream length
     */
    protected static final int MINIMUMLENGTH = 128;

    /**
     * NUMASSIGNMENTS constant, number of assigments
     */
    protected static final int NUMASSIGNMENTS = 7;

    /**
     * "longRunCase" result
     */
    protected int longRunCase;
    /**
     * "substringNumber" result
     */
    protected int substringNumber;
    /**
     * "substringLength" result
     */
    protected int substringLength;
    /**
     * "chiSquared" result
     */
    protected double chiSquared;
    /**
     * "assignment" results, 7 assigments
     */
    protected int[] assignment;


    /**
     * Constructor, default
     */
    public LongestRunOfOnes() {

        super();

        this.longRunCase = 0;
        this.substringNumber = 0;
        this.substringLength = 0;
        this.chiSquared = 0.0;
        this.assignment = new int[LongestRunOfOnes.NUMASSIGNMENTS];
        if (this.assignment == null) {
            this.error = RandomTestErrors.InsufficientMemory;
        }
        else {
            for (int i = 0; i < LongestRunOfOnes.NUMASSIGNMENTS; i++) {
                this.assignment[i] = 0;
            }
        }
    }


    /**
     * Constructor with a MathematicalFunctions object instantiated
     * 
     * @param mathFuncObj mathematicalFunctions object that will be used by this
     *            object
     */
    public LongestRunOfOnes(MathematicalFunctions mathFuncObj) {

        super(mathFuncObj);

        this.longRunCase = 0;
        this.substringNumber = 0;
        this.substringLength = 0;
        this.chiSquared = 0.0;
        this.assignment = new int[LongestRunOfOnes.NUMASSIGNMENTS];
        if (this.assignment == null) {
            this.error = RandomTestErrors.InsufficientMemory;
        }
        else {
            for (int i = 0; i < LongestRunOfOnes.NUMASSIGNMENTS; i++) {
                this.assignment[i] = 0;
            }
        }
    }


    /**
     * Destructor, zeroes all data
     * 
     */
    public void finalize() {

        this.longRunCase = 0;
        this.substringNumber = 0;
        this.substringLength = 0;
        this.chiSquared = 0.0;
        for (int i = 0; i < LongestRunOfOnes.NUMASSIGNMENTS; i++) {
            this.assignment[i] = 0;
        }
        this.assignment = null;
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
        double run = 0, v_n_obs = 0;
        double[] pi;
        int K;
        int i = 0, j = 0;
        int[] k;
        int[] nu = { 0, 0, 0, 0, 0, 0, 0 };

        pi = new double[7];
        k = new int[7];
        if (bitStream.GetBitLength() < 128) {
            this.error = RandomTestErrors.InsufficientNumberOfBits;
            this.random = false;
            return this.random;
        }
        if (bitStream.GetBitLength() < 6272) {
            K = 3;
            substringLength = 8;
            pi[0] = 0.21484375;
            pi[1] = 0.3671875;
            pi[2] = 0.23046875;
            pi[3] = 0.1875;
            k[0] = 1;
            k[1] = 2;
            k[2] = 3;
            k[3] = 4;
        }
        else {
            if (bitStream.GetBitLength() < 750000) {
                K = 5;
                substringLength = 128;
                pi[0] = 0.1174035788;
                pi[1] = 0.242955959;
                pi[2] = 0.249363483;
                pi[3] = 0.17517706;
                pi[4] = 0.102701071;
                pi[5] = 0.112398847;
                k[0] = 4;
                k[1] = 5;
                k[2] = 6;
                k[3] = 7;
                k[4] = 8;
                k[5] = 9;
            }
            else {
                K = 6;
                substringLength = 10000;
                pi[0] = 0.0882;
                pi[1] = 0.2092;
                pi[2] = 0.2483;
                pi[3] = 0.1933;
                pi[4] = 0.1208;
                pi[5] = 0.0675;
                pi[6] = 0.0727;
                k[0] = 10;
                k[1] = 11;
                k[2] = 12;
                k[3] = 13;
                k[4] = 14;
                k[5] = 15;
                k[6] = 16;
            }
        }

        this.error = RandomTestErrors.NoError;
        this.substringNumber = (int) Math.floor(bitStream.GetBitLength() / substringLength);
        bitStream.SetBitPosition(0);
        for (i = 0; i < this.substringNumber; i++) {
            v_n_obs = 0.e0;
            run = 0.e0;
            for (j = 0; j < substringLength; j++) {
                if (bitStream.GetBitPosition(i * substringLength + j) == 1) {
                    run++;
                    v_n_obs = this.mathFuncs.max(v_n_obs, run);
                }
                else {
                    run = 0.e0;
                }
            }
            if (v_n_obs < k[0]) {
                nu[0]++;
            }
            for (j = 0; j <= K; j++) {
                if (v_n_obs == k[j]) {
                    nu[j]++;
                }
            }
            if (v_n_obs > k[K]) {
                nu[K]++;
            }
        }
        this.chiSquared = 0.0;
        for (i = 0; i <= K; i++) {
            this.chiSquared += (((double) nu[i] - (double) this.substringNumber * pi[i])
                    * (nu[i] - (double) this.substringNumber * pi[i]))
                    / ((double) this.substringNumber * pi[i]);
        }
        this.pValue = mathFuncs.IGammaC(K / 2.0, (this.chiSquared / 2.0));
        if (this.mathFuncs.isNegative(this.pValue) || this.mathFuncs.isGreaterThanOne(this.pValue)) {
            this.error = RandomTestErrors.PValueOutOfRange;
            this.random = false;
        }
        else {
            if (this.pValue < this.alpha) {
                this.random = false;
            }
            else {
                this.random = true;
            }
        }
        for (i = 0; i < K + 1; i++)
            this.assignment[i] = nu[i];
        return this.random;
    }


    /**
     * Initializes the object
     * 
     */
    public void Initialize() {

        super.Initialize();
        this.longRunCase = 0;
        this.substringNumber = 0;
        this.substringLength = 0;
        this.chiSquared = 0.0;
        for (int i = 0; i < LongestRunOfOnes.NUMASSIGNMENTS; i++) {
            this.assignment[i] = 0;
        }
    }


    /**
     * Gets the type of the object
     * 
     * @return RandomTests: the concrete type class of the random number test,
     *         LongestRunOfOnes test for this class
     */
    public RandomTests GetType() {

        return LongestRunOfOnes.TEST;
    }


    /**
     * Gets the minimum random stream length
     * 
     * @return int: minimum length in bits of streams that can be checked by
     *         this test
     */
    public int GetMinimumLength() {

        return LongestRunOfOnes.MINIMUMLENGTH;
    }


    /**
     * Sets the longRunCase parameter
     * 
     * @param run Sets longRunCase parameter
     */
    public void SetLongRunCase(int run) {

        this.longRunCase = run;
    }


    /**
     * Gets the longRunCase parameter
     * 
     * @return int: gives established "longRunCase" parameter
     */
    public int GetLongRunCase() {

        return this.longRunCase;
    }


    /**
     * Gets the "substringNumber" result
     * 
     * @return int: gets the number of substring portions
     */
    public int GetSubstringNumber() {

        return this.substringNumber;
    }


    /**
     * Gets the "substringLength" result
     * 
     * @return int: gets length of each substring portion
     */
    public int GetSubstringLength() {

        return this.substringLength;
    }


    /**
     * Gets the chiSquared result
     * 
     * @return double: returns "chiSquared" result
     */
    public double GetChiSquared() {

        return this.chiSquared;
    }


    /**
     * Gets the Assignment result
     * 
     * @return int array: Gets the assignment array
     */
    public int[] GetAssignment() {

        return this.assignment;
    }


    /**
     * Gets the Assignment result based on the index
     * 
     * @param index index of assignment array to get its value
     * @return int: value of assignment corresponding to index parameter
     */
    public int GetAssignmentOfIndex(int index) {

        return this.assignment[index];
    }

}
