
package org.jiumao.nist.random;

import java.nio.DoubleBuffer;

import org.jiumao.nist.Base.BaseRandom;
import org.jiumao.nist.Base.MathematicalFunctions;
import org.jiumao.nist.Base.RandomTestErrors;
import org.jiumao.nist.Base.RandomTests;
import org.jiumao.nist.CryptoRandomStream.BaseCryptoRandomStream;


/**
 * 离散傅里叶变换检验
 * <p>
 * 本检验主要是看对序列进行分步傅里叶变换后的峰值高度。目的是探测待检验信号的周期性，以此揭示其与相应的随机信号之间的偏差程度。做法是观察超过
 * 95%阈值的峰值数目与低于 5%峰值的数目是否有显著不同。
 * 
 * @author ppf@jiumao.org
 * @date 2018年3月22日
 */
public class DiscreteFourierTransform extends BaseRandom {

    /// Random Test Class enumerator name
    protected static final RandomTests TEST = RandomTests.DiscreteFourierTransform;
    /// Random Test Class minimum stream length
    protected static final int MINIMUMLENGTH = 100;

    protected static final int[] NTRYH = { 4, 2, 3, 5 };
    protected static final double TPI = 6.28318530717958647692528676655900577;
    protected static final double HSQT2 = 0.70710678118654752440084436210485;

    protected double percentile;
    protected double observedPeaks;
    protected double expectedPeaks;
    protected double normalizedDifference;


    /**
     * dradf2 method
     * 
     * @param ido int
     * @param l1 int
     * @param cc double array
     * @param ch double array
     * @param wa1 double buffer
     */
    protected void dradf2(int ido, int l1, double[] cc, double[] ch, DoubleBuffer wa1) {
        int i, k;
        int t0, t1, t2, t3, t4, t5, t6;

        t1 = 0;
        t2 = l1 * ido;
        t0 = t2;
        t3 = ido << 1;
        for (k = 0; k < l1; k++) {
            ch[t1 << 1] = cc[t1] + cc[t2];
            ch[(t1 << 1) + t3 - 1] = cc[t1] - cc[t2];
            t1 += ido;
            t2 += ido;
        }
        if (ido >= 2) {
            if (ido > 2) {
                t1 = 0;
                t2 = t0;
                for (k = 0; k < l1; k++) {
                    t3 = t2;
                    t4 = (t1 << 1) + (ido << 1);
                    t5 = t1;
                    t6 = 2 * t1;
                    for (i = 0; i < (ido - 2); i += 2) {
                        t3 += 2;
                        t4 -= 2;
                        t5 += 2;
                        t6 += 2;
                        ch[t6] = cc[t5] + wa1.get(i) * cc[t3] - wa1.get(i + 1) * cc[t3 - 1]; // INI
                                                                                             // WA1
                        ch[t4] = wa1.get(i) * cc[t3] - wa1.get(i + 1) * cc[t3 - 1] - cc[t5];
                        ch[t6 - 1] = cc[t5 - 1] + (wa1.get(i) * cc[t3 - 1] + wa1.get(i + 1) * cc[t3]);
                        ch[t4 - 1] = cc[t5 - 1] - (wa1.get(i) * cc[t3 - 1] + wa1.get(i + 1) * cc[t3]); // END
                                                                                                       // WA1
                    }
                    t1 += ido;
                    t2 += ido;
                }
                if (ido % 2 == 0) {
                    t1 = ido;
                    t2 = t1 - 1;
                    t3 = t2;
                    t2 += t0;
                    for (k = 0; k < l1; k++) {
                        ch[t1] = -cc[t2];
                        ch[t1 - 1] = cc[t3];
                        t1 += ido << 1;
                        t2 += ido;
                        t3 += ido;
                    }
                }
            }
        }
    }


    /**
     * dradf4 method
     * 
     * @param ido int
     * @param l1 int
     * @param cc double array
     * @param ch double array
     * @param wa1 double buffer
     * @param wa2 double buffer
     * @param wa3 double buffer
     */
    protected void dradf4(int ido, int l1, double[] cc, double[] ch, DoubleBuffer wa1, DoubleBuffer wa2,
            DoubleBuffer wa3) {
        int i, k, t0, t1, t2, t3, t4, t5, t6;
        double ci2, ci3, ci4, cr2, cr3, cr4;

        t0 = l1 * ido;
        t1 = t0;
        t4 = t1 << 1;
        t2 = t1 + (t1 << 1);
        t3 = 0;
        for (k = 0; k < l1; k++) {
            t5 = t3 << 2;
            ch[t5] = (cc[t1] + cc[t2]) + (cc[t3] + cc[t4]);
            ch[(ido << 2) + t5 - 1] = (cc[t3] + cc[t4]) - (cc[t1] + cc[t2]);
            ch[(t5 += (ido << 1)) - 1] = cc[t3] - cc[t4];
            ch[t5] = cc[t2] - cc[t1];
            t1 += ido;
            t2 += ido;
            t3 += ido;
            t4 += ido;
        }
        if (ido >= 2) {
            if (ido > 2) {
                t1 = 0;
                for (k = 0; k < l1; k++) {
                    t2 = t1;
                    t4 = t1 << 2;
                    t6 = ido << 1;
                    t5 = t6 + t4;
                    for (i = 0; i < (ido - 2); i += 2) {
                        t3 = (t2 += 2);
                        t4 += 2;
                        t5 -= 2;
                        t3 += t0;
                        cr2 = wa1.get(i) * cc[t3 - 1] + wa1.get(i + 1) * cc[t3]; // INI
                                                                                 // WA1
                        ci2 = wa1.get(i) * cc[t3] - wa1.get(i + 1) * cc[t3 - 1]; // END
                                                                                 // WA1
                        t3 += t0;
                        cr3 = wa2.get(i) * cc[t3 - 1] + wa2.get(i + 1) * cc[t3]; // INI
                                                                                 // WA2
                        ci3 = wa2.get(i) * cc[t3] - wa2.get(i + 1) * cc[t3 - 1]; // END
                                                                                 // WA2
                        t3 += t0;
                        cr4 = wa3.get(i) * cc[t3 - 1] + wa3.get(i + 1) * cc[t3]; // INI
                                                                                 // WA3
                        ci4 = wa3.get(i) * cc[t3] - wa3.get(i + 1) * cc[t3 - 1]; // END
                                                                                 // WA3
                        ch[t4 - 1] = (cr2 + cr4) + (cc[t2 - 1] + cr3);
                        ch[t4] = (ci2 + ci4) + (cc[t2] + ci3);
                        ch[t5 - 1] = (cc[t2 - 1] - cr3) - (ci2 - ci4);
                        ch[t5] = (cr4 - cr2) - (cc[t2] - ci3);
                        ch[t4 + t6 - 1] = (ci2 - ci4) + (cc[t2 - 1] - cr3);
                        ch[t4 + t6] = (cr4 - cr2) + (cc[t2] - ci3);
                        ch[t5 + t6 - 1] = (cc[t2 - 1] + cr3) - (cr2 + cr4);
                        ch[t5 + t6] = (ci2 + ci4) - (cc[t2] + ci3);
                    }
                    t1 += ido;
                }
                if (ido % 2 == 0) {
                    t1 = t0 + ido - 1;
                    t2 = t1 + (t0 << 1);
                    t3 = ido << 2;
                    t4 = ido;
                    t5 = ido << 1;
                    t6 = ido;
                    for (k = 0; k < l1; k++) {
                        ch[t4 - 1] = (DiscreteFourierTransform.HSQT2 * (cc[t1] - cc[t2])) + cc[t6 - 1];
                        ch[t4 + t5 - 1] =
                                cc[t6 - 1] - (DiscreteFourierTransform.HSQT2 * (cc[t1] - cc[t2]));
                        ch[t4] = (-DiscreteFourierTransform.HSQT2 * (cc[t1] + cc[t2])) - cc[t1 + t0];
                        ch[t4 + t5] = (-DiscreteFourierTransform.HSQT2 * (cc[t1] + cc[t2])) + cc[t1 + t0];
                        t1 += ido;
                        t2 += ido;
                        t4 += t3;
                        t6 += ido;
                    }
                }
            }
        }
    }


    /**
     * dradfg method
     * 
     * @param ido int
     * @param ip int
     * @param l1 int
     * @param cc double array
     * @param c1 double array
     * @param c2 double array
     * @param ch double array
     * @param ch2 double array
     * @param wa double buffer
     */
    protected void dradfg(int ido, int ip, int l1, int idl1, double[] cc, double[] c1, double[] c2,
            double[] ch, double[] ch2, DoubleBuffer wa) {
        int idij, ipph, i, j, k, l, ic, ik, is;
        int t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10;
        double dc2, ai1, ai2, ar1, ar2, ds2;
        int nbd;
        double dcp, arg, dsp, ar1h, ar2h;
        int idp2, ipp2;

        arg = DiscreteFourierTransform.TPI / (double) ip;
        dcp = Math.cos(arg);
        dsp = Math.sin(arg);
        ipph = (ip + 1) >> 1;
        ipp2 = ip;
        idp2 = ido;
        nbd = (ido - 1) >> 1;
        t0 = l1 * ido;
        t10 = ip * ido;
        if (ido != 1) {
            for (ik = 0; ik < idl1; ik++)
                ch2[ik] = c2[ik];
            t1 = 0;
            for (j = 1; j < ip; j++) {
                t1 += t0;
                t2 = t1;
                for (k = 0; k < l1; k++) {
                    ch[t2] = c1[t2];
                    t2 += ido;
                }
            }
            is = -ido;
            t1 = 0;
            if (nbd > l1) {
                for (j = 1; j < ip; j++) {
                    t1 += t0;
                    is += ido;
                    t2 = -ido + t1;
                    for (k = 0; k < l1; k++) {
                        idij = is - 2;
                        t2 += ido;
                        t3 = t2;
                        for (i = 2; i < ido; i += 2) {
                            idij += 2;
                            t3 += 2;
                            ch[t3 - 1] = wa.get(idij) * c1[t3 - 1] + wa.get(idij + 1) * c1[t3]; // INI
                                                                                                // WA
                            ch[t3] = wa.get(idij) * c1[t3] - wa.get(idij + 1) * c1[t3 - 1]; // END
                                                                                            // WA
                        }
                    }
                }
            }
            else {
                for (j = 1; j < ip; j++) {
                    is += ido;
                    idij = is - 2;
                    t1 += t0;
                    t2 = t1;
                    for (i = 2; i < ido; i += 2) {
                        idij += 2;
                        t2 += 2;
                        t3 = t2;
                        for (k = 0; k < l1; k++) {
                            ch[t3 - 1] = wa.get(idij) * c1[t3 - 1] + wa.get(idij + 1) * c1[t3]; // INI
                                                                                                // WA
                            ch[t3] = wa.get(idij) * c1[t3] - wa.get(idij + 1) * c1[t3 - 1]; // END
                                                                                            // WA
                            t3 += ido;
                        }
                    }
                }
            }
            t1 = 0;
            t2 = ipp2 * t0;
            if (nbd < l1) {
                for (j = 1; j < ipph; j++) {
                    t1 += t0;
                    t2 -= t0;
                    t3 = t1;
                    t4 = t2;
                    for (i = 2; i < ido; i += 2) {
                        t3 += 2;
                        t4 += 2;
                        t5 = t3 - ido;
                        t6 = t4 - ido;
                        for (k = 0; k < l1; k++) {
                            t5 += ido;
                            t6 += ido;
                            c1[t5 - 1] = ch[t5 - 1] + ch[t6 - 1];
                            c1[t6 - 1] = ch[t5] - ch[t6];
                            c1[t5] = ch[t5] + ch[t6];
                            c1[t6] = ch[t6 - 1] - ch[t5 - 1];
                        }
                    }
                }
            }
            else {
                for (j = 1; j < ipph; j++) {
                    t1 += t0;
                    t2 -= t0;
                    t3 = t1;
                    t4 = t2;
                    for (k = 0; k < l1; k++) {
                        t5 = t3;
                        t6 = t4;
                        for (i = 2; i < ido; i += 2) {
                            t5 += 2;
                            t6 += 2;
                            c1[t5 - 1] = ch[t5 - 1] + ch[t6 - 1];
                            c1[t6 - 1] = ch[t5] - ch[t6];
                            c1[t5] = ch[t5] + ch[t6];
                            c1[t6] = ch[t6 - 1] - ch[t5 - 1];
                        }
                        t3 += ido;
                        t4 += ido;
                    }
                }
            }
        }
        for (ik = 0; ik < idl1; ik++)
            c2[ik] = ch2[ik];
        t1 = 0;
        t2 = ipp2 * idl1;
        for (j = 1; j < ipph; j++) {
            t1 += t0;
            t2 -= t0;
            t3 = t1 - ido;
            t4 = t2 - ido;
            for (k = 0; k < l1; k++) {
                t3 += ido;
                t4 += ido;
                c1[t3] = ch[t3] + ch[t4];
                c1[t4] = ch[t4] - ch[t3];
            }
        }
        ar1 = 1.0;
        ai1 = 0.0;
        t1 = 0;
        t2 = ipp2 * idl1;
        t3 = (ip - 1) * idl1;
        for (l = 1; l < ipph; l++) {
            t1 += idl1;
            t2 -= idl1;
            ar1h = dcp * ar1 - dsp * ai1;
            ai1 = dcp * ai1 + dsp * ar1;
            ar1 = ar1h;
            t4 = t1;
            t5 = t2;
            t6 = t3;
            t7 = idl1;
            for (ik = 0; ik < idl1; ik++) {
                ch2[t4++] = c2[ik] + ar1 * c2[t7++];
                ch2[t5++] = ai1 * c2[t6++];
            }
            dc2 = ar1;
            ds2 = ai1;
            ar2 = ar1;
            ai2 = ai1;
            t4 = idl1;
            t5 = (ipp2 - 1) * idl1;
            for (j = 2; j < ipph; j++) {
                t4 += idl1;
                t5 -= idl1;
                ar2h = dc2 * ar2 - ds2 * ai2;
                ai2 = dc2 * ai2 + ds2 * ar2;
                ar2 = ar2h;
                t6 = t1;
                t7 = t2;
                t8 = t4;
                t9 = t5;
                for (ik = 0; ik < idl1; ik++) {
                    ch2[t6++] += ar2 * c2[t8++];
                    ch2[t7++] += ai2 * c2[t9++];
                }
            }
        }
        t1 = 0;
        for (j = 1; j < ipph; j++) {
            t1 += idl1;
            t2 = t1;
            for (ik = 0; ik < idl1; ik++)
                ch2[ik] += c2[t2++];
        }
        if (ido >= l1) {
            t1 = 0;
            t2 = 0;
            for (k = 0; k < l1; k++) {
                t3 = t1;
                t4 = t2;
                for (i = 0; i < ido; i++)
                    cc[t4++] = ch[t3++];
                t1 += ido;
                t2 += t10;
            }
        }
        else {
            for (i = 0; i < ido; i++) {
                t1 = i;
                t2 = i;
                for (k = 0; k < l1; k++) {
                    cc[t2] = ch[t1];
                    t1 += ido;
                    t2 += t10;
                }
            }
        }
        t1 = 0;
        t2 = ido << 1;
        t3 = 0;
        t4 = ipp2 * t0;
        for (j = 1; j < ipph; j++) {
            t1 += t2;
            t3 += t0;
            t4 -= t0;
            t5 = t1;
            t6 = t3;
            t7 = t4;
            for (k = 0; k < l1; k++) {
                cc[t5 - 1] = ch[t6];
                cc[t5] = ch[t7];
                t5 += t10;
                t6 += ido;
                t7 += ido;
            }
        }
        if (ido != 1) {
            if (nbd >= l1) {
                t1 = -ido;
                t3 = 0;
                t4 = 0;
                t5 = ipp2 * t0;
                for (j = 1; j < ipph; j++) {
                    t1 += t2;
                    t3 += t2;
                    t4 += t0;
                    t5 -= t0;
                    t6 = t1;
                    t7 = t3;
                    t8 = t4;
                    t9 = t5;
                    for (k = 0; k < l1; k++) {
                        for (i = 2; i < ido; i += 2) {
                            ic = idp2 - i;
                            cc[i + t7 - 1] = ch[i + t8 - 1] + ch[i + t9 - 1];
                            cc[ic + t6 - 1] = ch[i + t8 - 1] - ch[i + t9 - 1];
                            cc[i + t7] = ch[i + t8] + ch[i + t9];
                            cc[ic + t6] = ch[i + t9] - ch[i + t8];
                        }
                        t6 += t10;
                        t7 += t10;
                        t8 += ido;
                        t9 += ido;
                    }
                }
            }
            else {
                t1 = -ido;
                t3 = 0;
                t4 = 0;
                t5 = ipp2 * t0;
                for (j = 1; j < ipph; j++) {
                    t1 += t2;
                    t3 += t2;
                    t4 += t0;
                    t5 -= t0;
                    for (i = 2; i < ido; i += 2) {
                        t6 = idp2 + t1 - i;
                        t7 = i + t3;
                        t8 = i + t4;
                        t9 = i + t5;
                        for (k = 0; k < l1; k++) {
                            cc[t7 - 1] = ch[t8 - 1] + ch[t9 - 1];
                            cc[t6 - 1] = ch[t8 - 1] - ch[t9 - 1];
                            cc[t7] = ch[t8] + ch[t9];
                            cc[t6] = ch[t9] - ch[t8];
                            t6 += t10;
                            t7 += t10;
                            t8 += ido;
                            t9 += ido;
                        }
                    }
                }
            }
        }
    }


    /**
     * drfti1 method
     * 
     * @param n int
     * @param waFull double array
     * @param ifac int array
     */
    protected void drfti1(int n, double[] waFull, int[] ifac) {
        double arg, argh, argld, fi;
        int ntry = 0, i, j = 0;
        int k1, l1, l2, ib;
        int ld, ii, ip, is, nq, nr;
        int ido, ipm, nfm1;
        int nl = n;
        int nf = 0;
        DoubleBuffer wa;

        wa = DoubleBuffer.wrap(waFull, n, waFull.length - n).slice();
        ntry = DiscreteFourierTransform.NTRYH[0];
        j = 0;
        do {
            nq = nl / ntry;
            nr = nl - ntry * nq;
            if (nr != 0) {
                j++;
                if (j < 4)
                    ntry = DiscreteFourierTransform.NTRYH[j];
                else
                    ntry += 2;
            }
            else {
                nf++;
                ifac[nf + 1] = ntry;
                nl = nq;
                if ((ntry == 2) && (nf != 1)) {
                    for (i = 1; i < nf; i++) {
                        ib = nf - i + 1;
                        ifac[ib + 1] = ifac[ib];
                    }
                    ifac[2] = 2;
                }
            }
        } while (nl != 1);
        ifac[0] = n;
        ifac[1] = nf;
        argh = DiscreteFourierTransform.TPI / n;
        is = 0;
        nfm1 = nf - 1;
        l1 = 1;
        if (nfm1 != 0) {
            for (k1 = 0; k1 < nfm1; k1++) {
                ip = ifac[k1 + 2];
                ld = 0;
                l2 = l1 * ip;
                ido = n / l2;
                ipm = ip - 1;
                for (j = 0; j < ipm; j++) {
                    ld += l1;
                    i = is;
                    argld = (double) ld * argh;
                    fi = 0.0;
                    for (ii = 2; ii < ido; ii += 2) {
                        fi += 1.;
                        arg = fi * argld;
                        wa.put(i, Math.cos(arg));
                        i++;
                        wa.put(i, Math.sin(arg));
                        i++;
                    }
                    is += ido;
                }
                l1 = l2;
            }
        }
    }


    /**
     * drftf1 method
     * 
     * @param n int
     * @param c double array
     * @param ch double array
     * @param ifac int array
     */
    protected void drftf1(int n, double[] c, double[] ch, int[] ifac) {
        int i, k1, l1, l2;
        int na, kh, nf;
        int ip, iw, ido, idl1, ix2, ix3;
        DoubleBuffer wa_iw_1;
        DoubleBuffer wa_ix2_1;
        DoubleBuffer wa_ix3_1;

        nf = ifac[1];
        na = 1;
        l2 = n;
        iw = n;
        for (k1 = 0; k1 < nf; k1++) {
            kh = nf - k1;
            ip = ifac[kh + 1];
            l1 = l2 / ip;
            ido = n / l2;
            idl1 = ido * l1;
            iw -= (ip - 1) * ido;
            na = 1 - na;
            if (ip == 4) {
                ix2 = iw + ido;
                ix3 = ix2 + ido;
                wa_iw_1 = DoubleBuffer.wrap(ch, n + iw - 1, ch.length - (n + iw - 1)).slice();
                wa_ix2_1 = DoubleBuffer.wrap(ch, n + ix2 - 1, ch.length - (n + ix2 - 1)).slice();
                wa_ix3_1 = DoubleBuffer.wrap(ch, n + ix3 - 1, ch.length - (n + ix3 - 1)).slice();
                if (na != 0) {
                    this.dradf4(ido, l1, ch, c, wa_iw_1, wa_ix2_1, wa_ix3_1);
                }
                else {
                    this.dradf4(ido, l1, c, ch, wa_iw_1, wa_ix2_1, wa_ix3_1);
                }
            }
            else {
                if (ip == 2) {
                    wa_iw_1 = DoubleBuffer.wrap(ch, n + iw - 1, ch.length - (n + iw - 1)).slice();
                    if (na == 0) {
                        this.dradf2(ido, l1, c, ch, wa_iw_1);
                    }
                    else {
                        this.dradf2(ido, l1, ch, c, wa_iw_1);
                    }
                }
                else {
                    if (ido == 1)
                        na = 1 - na;
                    wa_iw_1 = DoubleBuffer.wrap(ch, n + iw - 1, ch.length - (n + iw - 1)).slice();
                    if (na == 0) {
                        this.dradfg(ido, ip, l1, idl1, c, c, c, ch, ch, wa_iw_1);
                        na = 1;
                    }
                    else {
                        this.dradfg(ido, ip, l1, idl1, ch, ch, ch, c, c, wa_iw_1);
                        na = 0;
                    }
                }
            }
            l2 = l1;
        }
        if (na != 1) {
            for (i = 0; i < n; i++)
                c[i] = ch[i];
        }
    }


    /**
     * Constructor, default
     */
    public DiscreteFourierTransform() {

        super();

        this.percentile = 0.0;
        this.observedPeaks = 0.0;
        this.expectedPeaks = 0.0;
        this.normalizedDifference = 0.0;
    }


    /**
     * Constructor with a MathematicalFunctions object instantiated
     * 
     * @param mathFuncObj mathematicalFunctions object that will be used by this
     *            object
     */
    public DiscreteFourierTransform(MathematicalFunctions mathFuncObj) {

        super(mathFuncObj);

        this.percentile = 0.0;
        this.observedPeaks = 0.0;
        this.expectedPeaks = 0.0;
        this.normalizedDifference = 0.0;
    }


    /**
     * Destructor, zeroes all data
     * 
     */
    public void finalize() {

        this.percentile = 0.0;
        this.observedPeaks = 0.0;
        this.expectedPeaks = 0.0;
        this.normalizedDifference = 0.0;
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
        double upperBound;
        double[] m;
        double[] X;
        int i, count;
        double[] wsave;
        int[] ifac;

        if (bitStream.GetBitLength() < this.GetMinimumLength()) {
            this.error = RandomTestErrors.InsufficientNumberOfBits;
            this.random = false;
            return this.random;
        }
        this.error = RandomTestErrors.NoError;
        X = new double[bitStream.GetBitLength() + 1];
        wsave = new double[2 * bitStream.GetBitLength()];
        ifac = new int[15];
        m = new double[(bitStream.GetBitLength() / 2) + 1];

        if ((X == null) || (wsave == null) || (ifac == null) || (m == null)) {
            this.error = RandomTestErrors.InsufficientMemory;
            if (X != null)
                X = null;
            if (wsave != null)
                wsave = null;
            if (ifac != null)
                ifac = null;
            if (m != null)
                m = null;
            this.random = false;
            return this.random;
        }
        else {
            for (i = 0; i < X.length; i++) {
                X[i] = 0.0;
            }
            for (i = 0; i < wsave.length; i++) {
                wsave[i] = 0.0;
            }
            for (i = 0; i < ifac.length; i++) {
                ifac[i] = 0;
            }
            for (i = 0; i < m.length; i++) {
                m[i] = 0.0;
            }
            for (i = 0; i < (int) bitStream.GetBitLength(); i++)
                X[i] = 2 * (int) bitStream.GetBitPosition(i) - 1;
            this.drfti1(bitStream.GetBitLength(), wsave, ifac);
            this.drftf1(bitStream.GetBitLength(), X, wsave, ifac);
            m[0] = Math.sqrt(X[0] * X[0]);
            for (i = 0; i < (int) (bitStream.GetBitLength() / 2); i++) {
                m[i + 1] = Math.sqrt(Math.pow(X[(2 * i) + 1], 2) + Math.pow(X[(2 * i) + 2], 2));
            }
            count = 0;
            upperBound = Math.sqrt(2.995732274 * bitStream.GetBitLength());
            for (i = 0; i < (int) bitStream.GetBitLength() / 2; i++) {
                if (m[i] < upperBound) {
                    count++;
                }
            }
            this.percentile = (double) count / (bitStream.GetBitLength() / 2) * 100;
            this.observedPeaks = count;
            this.expectedPeaks = 0.95 * bitStream.GetBitLength() / 2.0;
            this.normalizedDifference = (this.observedPeaks - this.expectedPeaks)
                    / Math.sqrt(bitStream.GetBitLength() / 4.0 * 0.95 * 0.05);
            this.pValue = this.mathFuncs.ErFc(Math.abs(this.normalizedDifference) / Math.sqrt(2.0));
            if (this.pValue < this.alpha) {
                this.random = false;
            }
            else {
                this.random = true;
            }
        }
        if (X != null)
            X = null;
        if (wsave != null)
            wsave = null;
        if (ifac != null)
            ifac = null;
        if (m != null)
            m = null;
        return this.random;
    }


    /**
     * Initializes the object
     * 
     */
    public void Initialize() {

        super.Initialize();
        this.percentile = 0.0;
        this.observedPeaks = 0.0;
        this.expectedPeaks = 0.0;
        this.normalizedDifference = 0.0;
    }


    /**
     * Gets the type of the object
     * 
     * @return RandomTests the concrete type class of the random number test,
     *         DiscreteFourierTransform test for this class
     */
    public RandomTests GetType() {

        return DiscreteFourierTransform.TEST;
    }


    /**
     * Gets the minimum random stream length
     * 
     * @return int: minimum length in bits of streams that can be checked by
     *         this test
     */
    public int GetMinimumLength() {

        return DiscreteFourierTransform.MINIMUMLENGTH;
    }


    /**
     * Gets the "percentile" result
     * 
     * @return double: "percentile" result of last computed CryptoRandomStream
     */
    public double GetPercentile() {

        return this.percentile;
    }


    /**
     * Gets the "observedPeaks" result
     * 
     * @return double: "observedPeaks" result of last computed
     *         CryptoRandomStream
     */
    public double GetObservedPeaks() {

        return this.observedPeaks;
    }


    /**
     * Gets the "expectedPeaks" result
     * 
     * @return double: "expectedPeaks" result of last computed
     *         CryptoRandomStream
     */
    public double GetExpectedPeaks() {

        return this.expectedPeaks;
    }


    /**
     * Gets the "normalizedDifference" result
     * 
     * @return double: "normalizedDifference" result of last computed
     *         CryptoRandomStream
     */
    public double GetNormalizedDifference() {

        return this.normalizedDifference;
    }

}
