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
 * 二元矩阵秩检验
 * <P>
 * 该检验主要是看整个序列的分离子矩阵的秩。目的是核对源序列中固定长度子链间的线性依赖关系。
 * 
 * @author ppf@jiumao.org
 * @date 2018年3月22日
 */
public class Rank extends BaseRandom {

    /**
     * Random Test Class enumerator name
     */
    protected static final RandomTests TEST = RandomTests.Rank;
    /**
     * Random Test Class minimum stream length
     */
    protected static final int MINIMUMLENGTH = 38912;

    /**
     * "chiSquare" result
     */
    protected double chiSquared;
    /**
     * "matrixNumber" result
     */
    protected int matrixNumber;
    /**
     * "bitsDiscarded" result, number of bits from the stream that has not been
     * verified
     */
    protected int bitsDiscarded;
    /**
     * "p30, p31, p32" probailities
     */
    protected double p30, p31, p32; // Probabilities
    /**
     * "f30, f31, f32" frequencies
     */
    protected double f30, f31, f32; // Frequencies


    /**
     * Create Matrix
     * 
     * @param M integer "M" value indicating matrix number of rows
     * @param Q integer "Q" value indicating matrix number of columns
     * @return byte[][]: matrix of bytes of M rows and Q columns
     */
    protected byte[][] CreateMatrix(int M, int Q) {
        byte[][] matrix;

        matrix = new byte[M][Q];
        if (matrix == null)
            this.error = RandomTestErrors.InsufficientMemory;
        return matrix;
    }


    /**
     * Define Matrix
     * 
     * @param stream bit stream to fill matrix based on the other parameters
     * @param M integer "M" value indicating matrix number of rows
     * @param Q integer "Q" value indicating matrix number of columns
     * @param m byte matrix of M rows and Q columns that will be filled based on
     *            k parameter
     * @param k integer value that allows to traverse bit stream
     */
    protected void DefineMatrix(BaseCryptoRandomStream stream, int M, int Q, byte[][] m, int k) {
        int i, j;

        for (i = 0; i < M; i++)
            for (j = 0; j < Q; j++) {
                m[i][j] = stream.GetBitPosition(k * (M * Q) + j + i * M);
            }
    }


    /**
     * Deletes matrix
     * 
     * @param matrix matrix that will be erased
     */
    protected void DeleteMatrix(byte[][] matrix) {

        matrix = null;
    }


    /**
     * Perform Elementary Row Operations
     * 
     * @param flag integer indicating direction
     * @param i integer index from where to start direction
     * @param M integer indicating matrix row number
     * @param Q integer indicating matrix column number
     * @param A byte matrix of M rows and Q columns
     */
    protected void PerformElementaryRowOperations(int flag, int i, int M, int Q, byte[][] A) {
        int j, k;

        switch (flag) {
        case 0:
            for (j = i + 1; j < M; j++)
                if (A[j][i] == 1)
                    for (k = i; k < Q; k++)
                        A[j][k] = (byte) ((A[j][k] + A[i][k]) % 2);
            break;
        case 1:
            for (j = i - 1; j >= 0; j--)
                if (A[j][i] == 1)
                    for (k = 0; k < Q; k++)
                        A[j][k] = (byte) ((A[j][k] + A[i][k]) % 2);
            break;
        }
    }


    /**
     * Swap Rows
     * 
     * @param i integer row to swap
     * @param index integer row to be swapped with i row
     * @param Q integer indicating matrix column number
     * @param A byte matrix of M rows and Q columns
     * @return int: always return "1" value
     */
    protected int SwapRows(int i, int index, int Q, byte[][] A) {
        int p;
        byte temp;

        for (p = 0; p < Q; p++) {
            temp = A[i][p];
            A[i][p] = A[index][p];
            A[index][p] = temp;
        }
        return 1;
    }


    /**
     * Find Unit Element And Swap
     * 
     * @param flag integer indicating direction of search, if 0 value, from i to
     *            M, if 1 value from i to 0
     * @param i integer index pointing the column fromwhere to start search
     * @param M integer indicating matrix row number
     * @param Q integer indicating matrix column number
     * @param A byte matrix of M rows and Q columns
     * @return int: integer value of swap row operations performed
     */
    protected int FindUnitElementAndSwap(int flag, int i, int M, int Q, byte[][] A) {
        int index;
        int row_op = 0;

        switch (flag) {
        case 0:
            index = i + 1;
            while ((index < M) && (A[index][i] == 0))
                index++;
            if (index < M)
                row_op = this.SwapRows(i, index, Q, A);
            break;
        case 1:
            index = i - 1;
            while ((index >= 0) && (A[index][i] == 0))
                index--;
            if (index >= 0)
                row_op = this.SwapRows(i, index, Q, A);
            break;
        }
        return row_op;
    }


    /**
     * Determine Rank
     * 
     * @param m integer to determine rank
     * @param M integer indicating matrix row number
     * @param Q integer indicating matrix column number
     * @param A byte matrix of M rows and Q columns
     * @return int: integer that determines rank
     */
    protected int DetermineRank(int m, int M, int Q, byte[][] A) {
        int i, j, rank, allZeroes;

        rank = m;
        for (i = 0; i < M; i++) {
            allZeroes = 1;
            for (j = 0; j < Q; j++) {
                if (A[i][j] == 1) {
                    allZeroes = 0;
                    break;
                }
            }
            if (allZeroes == 1)
                rank--;
        }
        return rank;
    }


    /**
     * Computes rank
     * 
     * @param M integer indicating matrix row number
     * @param Q integer indicating matrix column number
     * @param matrix byte matrix of M rows adn Q columns
     * @return int: rank computed on byte matrix
     */
    protected int ComputeRank(int M, int Q, byte[][] matrix) {
        int i;
        int rank;
        int m = (int) this.mathFuncs.min(M, Q);

        for (i = 0; i < m - 1; i++) {
            if (matrix[i][i] == 1)
                this.PerformElementaryRowOperations(0, i, M, Q, matrix);
            else {
                if (this.FindUnitElementAndSwap(0, i, M, Q, matrix) == 1)
                    this.PerformElementaryRowOperations(0, i, M, Q, matrix);
            }
        }
        for (i = m - 1; i > 0; i--) {
            if (matrix[i][i] == 1)
                this.PerformElementaryRowOperations(1, i, M, Q, matrix);
            else {
                if (this.FindUnitElementAndSwap(1, i, M, Q, matrix) == 1)
                    this.PerformElementaryRowOperations(1, i, M, Q, matrix);
            }
        }
        rank = this.DetermineRank(m, M, Q, matrix);
        return rank;
    }


    /**
     * Constructor, default
     */
    public Rank() {

        super();

        this.chiSquared = 0.0;
        this.matrixNumber = 0;
        this.bitsDiscarded = 0;
        this.p30 = 0.0;
        this.p31 = 0.0;
        this.p32 = 0.0;
        this.f30 = 0.0;
        this.f31 = 0.0;
        this.f32 = 0.0;
    }


    /**
     * Constructor with a MathematicalFunctions object instantiated
     * 
     * @param mathFuncObj mathematicalFunctions object that will be used by this
     *            object
     */
    public Rank(MathematicalFunctions mathFuncObj) {

        super(mathFuncObj);

        this.chiSquared = 0.0;
        this.matrixNumber = 0;
        this.bitsDiscarded = 0;
        this.p30 = 0.0;
        this.p31 = 0.0;
        this.p32 = 0.0;
        this.f30 = 0.0;
        this.f31 = 0.0;
        this.f32 = 0.0;
    }


    /**
     * Destructor, zeroes all data
     * 
     */
    public void finalize() {

        this.chiSquared = 0.0;
        this.matrixNumber = 0;
        this.bitsDiscarded = 0;
        this.p30 = 0.0;
        this.p31 = 0.0;
        this.p32 = 0.0;
        this.f30 = 0.0;
        this.f31 = 0.0;
        this.f32 = 0.0;
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
        int r;
        double product;
        int i, k;
        double arg1;
        double R;
        byte[][] matrix = this.CreateMatrix(32, 32);

        if (matrix != null) {
            if (bitStream.GetBitLength() < this.GetMinimumLength()) {
                this.error = RandomTestErrors.InsufficientNumberOfBits;
                this.random = false;
                return this.random;
            }
            this.error = RandomTestErrors.NoError;
            this.matrixNumber = (int) Math.floor((double) bitStream.GetBitLength() / (32 * 32));
            if (this.mathFuncs.isZero(this.matrixNumber)) {
                this.error = RandomTestErrors.InsufficientNumberOfBits;
                this.pValue = 0.00;
                this.random = false;
            }
            else {
                this.bitsDiscarded = bitStream.GetBitLength() % (32 * 32);
                r = 32;
                product = 1;
                for (i = 0; i <= r - 1; i++)
                    product *= ((1.e0 - Math.pow(2, i - 32)) * (1.e0 - Math.pow(2, i - 32)))
                            / (1.e0 - Math.pow(2, i - r));
                this.p32 = Math.pow(2, r * (32 + 32 - r) - 32 * 32) * product;
                r = 31;
                product = 1;
                for (i = 0; i <= r - 1; i++)
                    product *= ((1.e0 - Math.pow(2, i - 32)) * (1.e0 - Math.pow(2, i - 32)))
                            / (1.e0 - Math.pow(2, i - r));
                this.p31 = Math.pow(2, r * (32 + 32 - r) - 32 * 32) * product;
                this.p30 = 1 - (this.p32 + this.p31);
                this.f32 = 0;
                this.f31 = 0;
                for (k = 0; k < this.matrixNumber; k++) {
                    this.DefineMatrix(bitStream, 32, 32, matrix, k);
                    R = this.ComputeRank(32, 32, matrix);
                    if (R == 32)
                        this.f32++;
                    if (R == 31)
                        this.f31++;
                }
                this.f30 = (double) this.matrixNumber - (this.f32 + this.f31);
                this.chiSquared = ((Math.pow(this.f32 - this.matrixNumber * this.p32, 2)
                        / (double) (this.matrixNumber * this.p32)
                        + Math.pow(this.f31 - this.matrixNumber * this.p31, 2)
                                / (double) (this.matrixNumber * this.p31)
                        + Math.pow(this.f30 - this.matrixNumber * this.p30, 2)
                                / (double) (this.matrixNumber * this.p30)));
                arg1 = -this.chiSquared / 2.e0;
                this.pValue = Math.exp(arg1);
                if (this.pValue < this.alpha) {
                    this.random = false;
                }
                else {
                    this.random = true;
                }
                matrix = null;
            }
            if (this.mathFuncs.isNegative(this.pValue) || this.mathFuncs.isGreaterThanOne(this.pValue)) {
                this.error = RandomTestErrors.PValueOutOfRange;
                this.random = false;
            }
        }
        else {
            this.error = RandomTestErrors.InsufficientMemory;
            this.random = false;
        }
        return this.random;
    }


    /**
     * Initializes the object
     * 
     */
    public void Initialize() {

        super.Initialize();
        this.chiSquared = 0.0;
        this.matrixNumber = 0;
        this.bitsDiscarded = 0;
        this.p30 = 0.0;
        this.p31 = 0.0;
        this.p32 = 0.0;
        this.f30 = 0.0;
        this.f31 = 0.0;
        this.f32 = 0.0;
    }


    /**
     * Gets the type of the object
     * 
     * @return RandomTests: the concrete type class of the random number test,
     *         Rank test for this class
     */
    public RandomTests GetType() {

        return Rank.TEST;
    }


    /**
     * Gets the minimum random stream length
     * 
     * @return int: minimum length in bits of streams that can be checked by
     *         this test
     */
    public int GetMinimumLength() {

        return Rank.MINIMUMLENGTH;
    }


    /**
     * Gets the "chiSquared" result
     * 
     * @return double: "chiSquared" result
     */
    public double GetChiSquared() {

        return this.chiSquared;
    }


    /**
     * Gets the "matrixNumber" result
     * 
     * @return int: "matrixNumber" result, number of 32 x 32 byte matrixes that
     *         divides bit stream
     */
    public int GetMatrixNumber() {

        return this.matrixNumber;
    }


    /**
     * Gets the "bitsDiscarded" result
     * 
     * @return int: "bitsDiscarded" result, bit stream number of bits that have
     *         not been tested
     */
    public int GetBitsDiscarded() {

        return this.bitsDiscarded;
    }


    /**
     * Gets the "p30" result
     * 
     * @return double: "p30" probability result
     */
    public double GetP30() {

        return this.p30;
    }


    /**
     * Gets the "p31" result
     * 
     * @return double: "p31" probability result
     */
    public double GetP31() {

        return this.p31;
    }


    /**
     * Gets the "p32" result
     * 
     * @return double: "p32" probability result
     */
    public double GetP32() {

        return this.p32;
    }


    /**
     * Gets the "f30" result
     * 
     * @return double: "f30" frequency result
     */
    public double GetF30() {

        return this.f30;
    }


    /**
     * Gets the "f31" result
     * 
     * @return double: "f31" frequency result
     */
    public double GetF31() {

        return this.f31;
    }


    /**
     * Gets the "f32" result
     * 
     * @return double: "f32" frequency result
     */
    public double GetF32() {

        return this.f32;
    }

}
