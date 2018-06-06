package org.jiumao.nist.random;

import org.jiumao.nist.Base.BaseRandom;
import org.jiumao.nist.Base.RandomTests;
import org.jiumao.nist.CryptoRandomStream.BaseCryptoRandomStream;


/**
 * The focus of the Overlapping Template Matching test is the number of
 * occurrences of pre-specified target strings. Both this test and the
 * Non-overlapping Template Matching test use an m-bit window to search for a
 * specific m-bit pattern. As with the Non-overlapping Template Matching test,
 * if the pattern is not found, the window slides one bit position. The
 * difference between this test and the Non-overlapping Template Matching test
 * is that when the pattern is found, the window slides only one bit before
 * resuming the search.
 */
public class OverlappingTemplateMatchings extends BaseRandom {

    int nu[] = { 0, 0, 0, 0, 0, 0 };
    double pi[] = { 0.364091, 0.185659, 0.139381, 0.100571, 0.0704323, 0.139865 };
    static int M = 1032;


    @Override
    public boolean isRandom(BaseCryptoRandomStream stream) {
        int i, k, match;
        double W_obs, eta, sum, chi2, p_value, lambda;
        int j, K = 5;
        int m = stream.GetBitLength(), n = GetBlockLength();
        int[] sequence = new int[m];
        int N = n / M;

        for (i = 0; i < m; i++)
            sequence[i] = 1;

        lambda = (double) (M - m + 1) / Math.pow(2, m);
        eta = lambda / 2.0;
        sum = 0.0;
        for (i = 0; i < K; i++) { /* Compute Probabilities */
            pi[i] = mathFuncs.Pr(i, eta);
            sum += pi[i];
        }
        pi[K] = 1 - sum;

        for (i = 0; i < N; i++) {
            W_obs = 0;
            for (j = 0; j < M - m + 1; j++) {
                match = 1;
                for (k = 0; k < m; k++) {
                    if (sequence[k] != stream.GetBitPosition(i * M + j + k))
                        match = 0;
                }
                if (match == 1)
                    W_obs++;
            }
            if (W_obs <= 4)
                nu[(int) W_obs]++;
            else
                nu[K]++;
        }
        sum = 0;
        chi2 = 0.0; /* Compute Chi Square */
        for (i = 0; i < K + 1; i++) {
            chi2 += Math.pow((double) nu[i] - (double) N * pi[i], 2) / ((double) N * pi[i]);
            sum += nu[i];
        }
        this.pValue = mathFuncs.IGammaC(K / 2.0, chi2 / 2.0);


       // if (isNegative(p_value) || isGreaterThanOne(p_value))

        return this.pValue > ALPHA;
    }


    @Override
    public RandomTests GetType() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public int GetMinimumLength() {
        // TODO Auto-generated method stub
        return 0;
    }

}
