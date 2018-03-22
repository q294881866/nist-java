package org.jiumao.nist.random;

import org.jiumao.nist.Base.BaseRandom;
import org.jiumao.nist.Base.RandomTests;
import org.jiumao.nist.CryptoRandomStream.BaseCryptoRandomStream;


/**
 * The focus of this test is the length of a linear feedback shiftregister
 * (LFSR). The purpose of this test is to determine whether or not the sequence
 * is complex enough to be considered random. Random sequences are characterized
 * by longer LFSRs. An LFSR that is too short implies non- randomness.
 */
public class LinearComplexity extends BaseRandom {
    /**
     * "blockLength" length in bits of block division
     */
    protected int blockLength;


    @Override
    public boolean isRandom(BaseCryptoRandomStream stream) {
        int i, ii, j, d, N, L, m, N_, parity, sign, K = 6;
        double T_, mean, nu[] = new double[7], chi2;
        double pi[] = { 0.01047, 0.03125, 0.12500, 0.50000, 0.25000, 0.06250, 0.020833 };
        int M = blockLength;
        int n = stream.GetBitLength();
        int[] T = new int[M], P = new int[M], B_ = new int[M], C = new int[M];

        N = (int) Math.floor(n / M);

        for (i = 0; i < K + 1; i++)
            nu[i] = 0.00;
        for (ii = 0; ii < N; ii++) {
            for (i = 0; i < M; i++) {
                B_[i] = 0;
                C[i] = 0;
                T[i] = 0;
                P[i] = 0;
            }
            L = 0;
            m = -1;
            d = 0;
            C[0] = 1;
            B_[0] = 1;

            /* DETERMINE LINEAR COMPLEXITY */
            N_ = 0;
            while (N_ < M) {
                d = (int) stream.GetBitPosition(ii * M + N_);
                for (i = 1; i <= L; i++)
                    d += C[i] * stream.GetBitPosition(ii * M + N_ - i);
                d = d % 2;
                if (d == 1) {
                    for (i = 0; i < M; i++) {
                        T[i] = C[i];
                        P[i] = 0;
                    }
                    for (j = 0; j < M; j++)
                        if (B_[j] == 1)
                            P[j + N_ - m] = 1;
                    for (i = 0; i < M; i++)
                        C[i] = (C[i] + P[i]) % 2;
                    if (L <= N_ / 2) {
                        L = N_ + 1 - L;
                        m = N_;
                        for (i = 0; i < M; i++)
                            B_[i] = T[i];
                    }
                }
                N_++;
            }
            if ((parity = (M + 1) % 2) == 0)
                sign = -1;
            else
                sign = 1;
            mean = M / 2.0 + (9.0 + sign) / 36.0 - 1.0 / Math.pow(2, M) * (M / 3.0 + 2.0 / 9.0);
            if ((parity = M % 2) == 0)
                sign = 1;
            else
                sign = -1;
            T_ = sign * (L - mean) + 2.0 / 9.0;

            if (T_ <= -2.5)
                nu[0]++;
            else if (T_ > -2.5 && T_ <= -1.5)
                nu[1]++;
            else if (T_ > -1.5 && T_ <= -0.5)
                nu[2]++;
            else if (T_ > -0.5 && T_ <= 0.5)
                nu[3]++;
            else if (T_ > 0.5 && T_ <= 1.5)
                nu[4]++;
            else if (T_ > 1.5 && T_ <= 2.5)
                nu[5]++;
            else
                nu[6]++;
        }
        chi2 = 0.00;
        for (i = 0; i < K + 1; i++)
            chi2 += Math.pow(nu[i] - N * pi[i], 2) / (N * pi[i]);
        this.pValue = mathFuncs.IGammaC(K / 2.0, chi2 / 2.0);

        return this.pValue < alpha;
    }


    @Override
    public RandomTests GetType() {
        return null;
    }


    @Override
    public int GetMinimumLength() {
        // TODO Auto-generated method stub
        return 0;
    }


    public int getBlockLength() {
        return blockLength;
    }


    public void setBlockLength(int blockLength) {
        this.blockLength = blockLength;
    }
}
