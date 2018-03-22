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

import org.jiumao.nist.CryptoRandomStream.BaseCryptoRandomStream;


/**
 * 随机数序列检测：
 * <p>
 * 假设随机序列0/1出现的频率近似为一半
 * 
 * @author ppf@jiumao.org
 * @date 2018年3月22日
 */
public abstract class BaseRandom {
    /** SIGNIFICANCE LEVEL */
    public static final double ALPHA = 0.01;
    /** APERIODIC TEMPLATES: 148=>temp_length=9 */
    public static final int MAXNUMOFTEMPLATES = 148;
    /** MAX TESTS DEFINED */
    public static final int NUMOFTESTS = 15;
    /** MAX PRNGs */
    public static final int NUMOFGENERATORS = 10;
    public static final int MAXFILESPERMITTEDFORPARTITION = 148;
    public static final int TEST_FREQUENCY = 1;
    public static final int TEST_BLOCK_FREQUENCY = 2;
    public static final int TEST_CUSUM = 3;
    public static final int TEST_RUNS = 4;
    public static final int TEST_LONGEST_RUN = 5;
    public static final int TEST_RANK = 6;
    public static final int TEST_FFT = 7;
    public static final int TEST_NONPERIODIC = 8;
    public static final int TEST_OVERLAPPING = 9;
    public static final int TEST_UNIVERSAL = 10;
    public static final int TEST_APEN = 11;
    public static final int TEST_RND_EXCURSION = 12;
    public static final int TEST_RND_EXCURSION_VAR = 13;
    public static final int TEST_SERIAL = 14;
    public static final int TEST_LINEARCOMPLEXITY = 15;

    /**
     * Random Test Class minimum stream length
     */
    protected static final int MINIMUMLENGTH = 100;

    /**
     * "blockLength" length in bits of block division
     */
    protected int blockLength;

    /**
     * "alpha" parameter setting confidence level
     */
    protected double alpha;
    /**
     * "pValue" result to verify randomness properties
     */
    protected double pValue;
    /**
     * "random" result true: if last checked stream was a randomized stream
     * false: if last checked stream was not a randomized stream
     */
    protected boolean random;
    /**
     * "error" RandomTestErrors enumeration value indicating the error (if has
     * been produced) of last stream checked
     */
    protected RandomTestErrors error;
    /**
     * "MathematicalFunctions" object that is being used by random number test
     */
    protected MathematicalFunctions mathFuncs;
    /**
     * boolean indicating if "mathFuncs" object has been internally created
     * true: if "mathFuncs" object has been instantiated within random number
     * test object false: if "mathFuncs" object has been instantiated outside
     * random number test object
     */
    protected boolean autoMathFunc;


    /**
     * Constructor, default
     */
    public BaseRandom() {

        super();

        this.error = RandomTestErrors.NoError;
        this.alpha = 0.0;
        this.pValue = 0.0;
        this.mathFuncs = new MathematicalFunctions();
        if (this.mathFuncs == null) {
            this.error = RandomTestErrors.MathematicalFunctionsError;
            this.autoMathFunc = false;
        }
        else {
            this.autoMathFunc = true;
        }
        this.random = false;
    }


    /**
     * Constructor with a MathematicalFunctions object instantiated
     */
    public BaseRandom(MathematicalFunctions mathFuncObj) {

        this.error = RandomTestErrors.NoError;
        if (mathFuncObj != null) {
            this.mathFuncs = mathFuncObj;
            this.autoMathFunc = false;
        }
        else {
            this.mathFuncs = new MathematicalFunctions();
            if (this.mathFuncs == null) {
                this.error = RandomTestErrors.MathematicalFunctionsError;
                this.autoMathFunc = false;
            }
            else {
                this.autoMathFunc = true;
            }
        }
        this.alpha = 0.0;
        this.pValue = 0.0;
        this.random = false;
    }


    /**
     * Destructor
     */
    public void finalize() {

        this.alpha = 0.0;
        this.pValue = 0.0;
        if ((this.autoMathFunc) && (this.mathFuncs != null)) {
            this.mathFuncs = null;
        }
        this.mathFuncs = null;
        this.random = false;
        this.error = RandomTestErrors.NoError;
    }


    /**
     * Sets the BaseRandomTest alpha margin
     */
    public void setAlpha(double newAlpha) {

        this.alpha = newAlpha;
    }


    /**
     * Gets the BaseRandomTest alpha margin
     */
    public double getAlpha() {

        return this.alpha;
    }


    /**
     * Gets the BaseRandomTest pValue
     */
    public double GetPValue() {

        return getpValue();
    }


    /**
     * Gets the BaseRandomTest error of the last executed BaseCryptoRandomStream
     */
    public RandomTestErrors GetError() {

        return this.error;
    }


    /**
     * Gets the BaseRandomTest random state of the last executed
     * BaseCryptoRandomStream
     */
    public boolean IsRandom() {

        return this.random;
    }


    /**
     * Tests the BaseCryptoRandomStream executed and returns the random value
     */
    abstract public boolean isRandom(BaseCryptoRandomStream stream);


    /**
     * Initialize the object
     */
    public void Initialize() {

        this.pValue = 0.0;
        this.random = false;
        this.error = RandomTestErrors.NoError;
    }


    /**
     * Gets the type of the object
     */
    abstract public RandomTests GetType();


    /**
     * Gets the minimum stream length
     */
    abstract public int GetMinimumLength();


    public double getpValue() {
        return pValue;
    }


    public void setpValue(double pValue) {
        this.pValue = pValue;
    }


    /**
     * Sets the "block" parameter
     * 
     * @param block sets the block length in bits that Block Frequency test will
     *            use for checking randomness
     */
    public void SetBlockLength(int block) {

        this.blockLength = block;
    }


    /**
     * Gets the "block length" parameter, block length in bits
     * 
     * @return int: gets the block length that Block Frequency test is using for
     *         checking randomness
     */
    public int GetBlockLength() {

        return this.blockLength;
    }

}
