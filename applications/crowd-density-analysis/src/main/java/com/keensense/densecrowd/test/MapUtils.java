package com.keensense.densecrowd.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 常用地图转换工具类（各个地图API采用的坐标系(WGS84坐标系：即地球坐标系，国际上通用的坐标系。谷歌地图用此坐标)）<br>
 * 百度地图API	        百度坐标 (BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系。)<br>
 * 腾讯搜搜地图API	        火星坐标 (GCJ02坐标系：即火星坐标系，WGS84坐标系经加密后的坐标系。)<br>
 * 阿里云地图API	        火星坐标 (GCJ02坐标系：即火星坐标系，WGS84坐标系经加密后的坐标系。)<br>
 * 高德MapABC地图API	火星坐标 (GCJ02坐标系：即火星坐标系，WGS84坐标系经加密后的坐标系。)
 * @author 谢海军
 * 2016年12月2日13:35:16
 */
public class MapUtils {
    public static final double r2d = 57.2957795131;
    public static final double PI = 3.1415926535897932384626433832795;
    public static final double rearth = 6371006.84;

    /**
     * wgs84坐标转上海城市坐标
     * @param lat 维度
     * @param lon 经度
     * @return
     */
    public static Map<String, Double> wgs84Tosh(Double lat, Double lon) {
        double tolat = (31 + (14.0 + 7.55996 / 60.0) / 60.0) / r2d;
        double tolon = (121.0 + (28.0 + 1.80651 / 60.0) / 60) / r2d;

        Double frlat = lat / r2d;
        Double frlon = lon / r2d;
        Double clatt = Math.cos(frlat);
        Double clatf = Math.cos(tolat);
        Double slatt = Math.sin(frlat);
        Double slatf = Math.sin(tolat);
        Double dlon = frlon - tolon;
        Double cdlon = Math.cos(dlon);
        Double sdlon = Math.sin(dlon);
        Double cdist = slatf * slatt + clatf * clatt * cdlon;
        Double temp = (clatt * sdlon) * (clatt * sdlon) + (clatf * slatt - slatf * clatt * cdlon) * (clatf * slatt - slatf * clatt * cdlon);
        Double sdist = Math.sqrt(Math.abs(temp));

        Double gcdist = 0.0;

        if ((Math.abs(sdist) > 1e-7) || (Math.abs(cdist) > 1e-7))
            gcdist = Math.atan2(sdist, cdist);

        Double sbrg = sdlon * clatt;
        Double cbrg = (clatf * slatt - slatf * clatt * cdlon);

        if ((Math.abs(sbrg) > 1e-7) || (Math.abs(cbrg) > 1e-7)) {
            temp = Math.atan2(sbrg, cbrg);
            while (temp < 0) {
                temp = temp + 2 * PI;
            }
        }

        Double hor = gcdist * rearth;
        Double xx = hor * Math.sin(temp);
        Double yy = hor * Math.cos(temp);

        Map<String,Double> model = new HashMap<String,Double>();

        model.put("lat", xx);
        model.put("lon", yy);

        return model;
    }


    public static final Double m_pNorthMove = -3457000.0;
    public static final Double m_pSHa = 6378245.0;
    public static final double m_pSHf = 298.3;
    public static final double m_pWGS84a = 6371006.84;
    public static final double m_pWGS84f = 298.25722356300003;
    /**
     * 上海城市坐标转WGS84坐标
     * @param East 经(东部)
     * @param North 纬(北部)
     * @param InH  内部高度(默认可以用 0.0)
     * @return
     */
    public static Map<String, Double> ShToWGS84(Double East, Double North, Double InH) {
        North = North - m_pNorthMove;

        List<Double> a = AntiGaussProjectionConst(m_pSHa, m_pSHf, East, North);
        Double rB = a.get(0);
        Double rl = a.get(1);
        Double m_pCenterL = DMSToDegree(121.0, 28.0, 0.0);
        Double dL = RadianToDegree(rl) + m_pCenterL;
        Double tB = rB;
        Double tL = DegreeToRadian(dL);
        Double tH = InH;
/*        Double sB = 0.0;
        Double sL = 0.0;
        Double sH = 0.0;*/

        ArrayList<Double> m_pPara = new ArrayList<Double>();
        m_pPara.add(-39.208938);
        m_pPara.add(65.046547);
        m_pPara.add(49.410739);
        m_pPara.add(SecondToRadian(6.125483));
        m_pPara.add(SecondToRadian(-1.281548));
        m_pPara.add(SecondToRadian(-0.861599));
        m_pPara.add(2.916036 * 1e-6);
        List<Double> b = LBH7ParameterSelf(tL, tB, tH, m_pSHa, 1.0 / m_pSHf, m_pPara.get(0),
                m_pPara.get(1), m_pPara.get(2), m_pPara.get(3),
                m_pPara.get(4), m_pPara.get(5), m_pPara.get(6),
                m_pWGS84a, 1.0 / m_pWGS84f);


        ArrayList<Double> a1 = RadianToDMS(b.get(0));
        ArrayList<Double> a2 = RadianToDMS(b.get(1));

        Double b1 = a1.get(0) + a1.get(1) / 60 + a1.get(2) / 3600;
        Double b2 = a2.get(0) + a2.get(1) / 60 + a2.get(2) / 3600;

        /*百度偏移*/
        /*谷歌偏移*/
        b1 = b1 + 0.000935;
        b2 = b2 + 0.002651;

        Map<String,Double> model = new HashMap<String,Double>();

        model.put("lat", b1);
        model.put("lon", b2);

        return model;
    }

    /**
     * WGS-84转GCJ坐标
     * @param wgsLat
     * @param wgsLon
     * @return
     */
    public static Map<String, Double> gcj_encrypt(Double wgsLat, Double wgsLon) {
        Map<String,Double> model = new HashMap<String,Double>();

        if (outOfChina(wgsLat, wgsLon)) {

            model.put("lat", wgsLat);
            model.put("lon", wgsLon);

            return model;
        }

        Map<String, Double> d = delta(wgsLat, wgsLon);

        model.put("lat", wgsLat + (Double) d.get("lat"));
        model.put("lon", wgsLon + (Double) d.get("lon"));

        return model;
    }

    /**
     * GCJ坐标转WGS-84坐标
     * @param gcjLat
     * @param gcjLon
     * @return
     */
    public static Map<String, Double> gcj_decrypt(Double gcjLat, Double gcjLon) {
        Map<String, Double> model = new HashMap<String, Double>();

        if (outOfChina(gcjLat, gcjLon)) {

            model.put("lat", gcjLat);
            model.put("lon", gcjLon);

            return model;
        }

        Map<String, Double> d = delta(gcjLat, gcjLon);

        model.put("lat", gcjLat - (Double) d.get("lat"));
        model.put("lon", gcjLon - (Double) d.get("lon"));

        return model;
    }

    public static final double x_pi = PI * 3000.0 / 180.0;
    /**
     * GCJ-02坐标转百度BD-09坐标
     * @param gcjLat
     * @param gcjLon
     * @return
     */
    public static Map<String, Double> bd_encrypt(Double gcjLat, Double gcjLon) {
        Map<String, Double> model = new HashMap<String, Double>();

        Double x = gcjLon, y = gcjLat;
        Double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        Double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        Double bdLon = z * Math.cos(theta) + 0.0065;
        Double bdLat = z * Math.sin(theta) + 0.006;

        model.put("lat", bdLat);
        model.put("lon", bdLon);

        return model;
    }

    /**
     * 百度BD-09坐标转GCJ-02坐标
     * @param bdLat
     * @param bdLon
     * @return
     */
    public static Map<String, Double> bd_decrypt(Double bdLat, Double bdLon) {
        Map<String, Double> model = new HashMap<String, Double>();

        Double x = bdLon - 0.0065, y = bdLat - 0.006;
        Double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        Double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        Double gcjLon = z * Math.cos(theta);
        Double gcjLat = z * Math.sin(theta);

        model.put("lat", gcjLat);
        model.put("lon", gcjLon);

        return model;
    }


    public static Boolean outOfChina(Double lat, Double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;

        return false;
    }
    public static Map<String, Double> delta(Double lat, Double lon) {
        Double a = 6378245.0; //  a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
        Double ee = 0.00669342162296594323; //  ee: 椭球的偏心率。
        Double dLat = transformLat(lon - 105.0, lat - 35.0);
        Double dLon = transformLon(lon - 105.0, lat - 35.0);
        Double radLat = lat / 180.0 * PI;
        Double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        Double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * PI);

        Map<String,Double> model = new HashMap<String,Double>();

        model.put("lat", dLat);
        model.put("lon", dLon);

        return model;
    }
    public static Double transformLat(Double x, Double y) {
        Double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;

        return ret;
    }
    public static Double transformLon(Double x, Double y) {
        Double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;

        return ret;
    }


    public static final double MPD = 60.0;
    public static final double SPD = 3600.0;
    public static final double SPM = 60.0;
    public static ArrayList<Double> RadianToDMS(Double radian) {
        Boolean isNegative;

        Double degree = 0.0;
        Double minute = 0.0;
        Double second = 0.0;

        isNegative = false;
        if (radian < 0.0) {
            isNegative = false;
            radian = Math.abs(radian);
        } else {
            isNegative = false;
            degree = radian * DPR;
            minute = (degree - Math.floor(degree)) * MPD;

            degree = Math.floor(degree);
            second = (minute - Math.floor(minute)) * SPM;
            minute = Math.floor(minute);

            if (isNegative) {
                degree = -degree;
                minute = -minute;
                second = -second;
            }
        }

        ArrayList<Double> datalist = new ArrayList<Double>();

        datalist.add(degree);
        datalist.add(minute);
        datalist.add(second);

        return datalist;
    }
    public static ArrayList<Double> LBH7ParameterSelf(Double Ls, Double Bs, Double Hs, Double fA, Double fF, Double dX,
                                                      Double dY, Double dZ, Double ex, Double ey, Double ez, Double m, Double at, Double ft) {
        Double Xs, Ys, Zs, Xt, Yt, Zt, Lt, Bt, Ht;
        ArrayList<Double> datalist = new ArrayList<Double>();

        ArrayList<Double> a = LBHToXYZ(fA, 1.0 / fF, Ls, Bs, Hs);

        Xs = a.get(0);
        Ys = a.get(1);
        Zs = a.get(2);

        ArrayList<Double> b = XYZ7Parameter(Xs, Ys, Zs, dX, dY, dZ, ex, ey, ez, m);

        Xt = b.get(0);
        Yt = b.get(1);
        Zt = b.get(2);

        ArrayList<Double> c = XYZToLBHBowring(at, 1.0 / ft, Xt, Yt, Zt);

        Lt = c.get(0);
        Bt = c.get(1);
        Ht = c.get(2);

        datalist.add(Lt);
        datalist.add(Bt);
        datalist.add(Ht);

        return datalist;
    }

    public static final double EQUALDE = 0.00000000000001;
    public static ArrayList<Double> AntiGaussProjectionConst(Double curra, Double currinvf, Double East, Double North) {
        Double currf, currb, curre12, curre22, curre14, curre16, curre18, currAp, currBp, currCp, currDp, currEp;
        Double A2, A4, A6, A8, currB2, currB4, currB6, currB8, phi, Bf, Nf, tf, cosBf, etaf2;
        Double B, l;

        ArrayList<Double> datalist = new ArrayList<Double>();

        if ((Math.abs(East) < EQUALDE) && (Math.abs(North) < EQUALDE)) {
            B = 0.0;
            l = 0.0;
        }

        currf = 1 / currinvf;
        currb = curra * (1 - currf);
        curre12 = (curra * curra - currb * currb) / (curra * curra);
        curre22 = (curra * curra - currb * currb) / (currb * currb);
        curre14 = curre12 * curre12;
        curre16 = curre14 * curre12;
        curre18 = curre14 * curre14;

        currAp = 1 + 3.0 / 4.0 * curre12 + 45.0 / 64.0 * curre14 + 175.0 / 256.0 * curre16 + 11025.0 / 16384.0 * curre18;
        currBp = 3.0 / 4.0 * curre12 + 15.0 / 16.0 * curre14 + 525.0 / 512.0 * curre16 + 2205.0 / 2048.0 * curre18;
        currCp = 15.0 / 64.0 * curre14 + 105.0 / 256.0 * curre16 + 2205.0 / 4096.0 * curre18;
        currDp = 35.0 / 512.0 * curre16 + 315.0 / 2048.0 * curre18;
        currEp = 315.0 / 16384.0 * curre18;
        A2 = currBp / (2 * currAp);
        A4 = -currCp / (4 * currAp);
        A6 = currDp / (6 * currAp);
        A8 = -currEp / (8 * currAp);

        currB2 = A2 - A2 * A4 - A4 * A6 - 0.5 * A2 * A2 * A2 - A2 * A4 * A4 + 0.5 * A2 * A2 * A6 - 18.3 * A2 * A2 * A2 * A4;
        currB4 = A4 + A2 * A2 - 2.0 * A2 * A6 - 4.0 * A2 * A2 * A4 - 1.3 * A2 * A2 * A2 * A2;
        currB6 = A6 + 3.0 * A2 * A4 - 3.0 * A2 * A8 + 1.5 * A2 * A2 * A2 - 4.5 * A2 * A4 * A4 - 9.0 * A2 * A2 * A6 - 12.5 * A2 * A2 * A2 * A4;
        currB8 = A8 + 2.0 * A4 * A4 + 4.0 * A2 * A6 + 8.0 * A2 * A2 * A4 + 2.7 * A2 * A2 * A2 * A2;

        phi = North / (curra * (1 - curre12) * currAp);
        Bf = phi + currB2 * Math.sin(2 * phi) + currB4 * Math.sin(4 * phi) + currB6 * Math.sin(6 * phi) + currB8 * Math.sin(8 * phi);

        if (Math.abs(Math.abs(Bf) - PI / 2.0) < EQUALDE) {
            B = Bf;
            l = 0.0;

            datalist.add(B);
            datalist.add(l);

            return datalist;
        }

        Nf = curra / Math.sqrt(1 - curre12 * Math.sin(Bf) * Math.sin(Bf));
        tf = Math.tan(Bf);
        cosBf = Math.cos(Bf);
        etaf2 = curre22 * cosBf * cosBf;

        B = Bf + tf * (-1 - etaf2) * East * East / (2 * Nf * Nf)
                + tf * (5 + 3 * tf * tf + 6 * etaf2 - 6 * tf * tf * etaf2 - 3 * etaf2 * etaf2 - 9 * tf * tf * etaf2 * etaf2) * East * East * East * East / (24 * Nf * Nf * Nf * Nf)
                + tf * (-61 - 90 * tf * tf - 45 * tf * tf * tf * tf - 107 * etaf2 + 162 * tf * tf * etaf2 + 45 * tf * tf * tf * tf * etaf2) * East * East * East * East * East * East / (720 * Nf * Nf * Nf * Nf * Nf * Nf);
        l = East / (Nf * cosBf)
                + (-1 - 2 * tf * tf - etaf2) * East * East * East / (6 * Nf * Nf * Nf * cosBf)
                + (5 + 28 * tf * tf + 24 * tf * tf * tf * tf + 6 * etaf2 + 8 * tf * tf * etaf2) * East * East * East * East * East / (120 * Nf * Nf * Nf * Nf * Nf * cosBf);

        datalist.add(B);
        datalist.add(l);

        return datalist;

    }
    public static final double DPM = 0.016666666666666666666666666666667;
    public static final double DPS = 0.00027777777777777777777777777777778;
    public static Double DMSToDegree(Double degree, Double minute, Double second) {
        Boolean isNegative;

        if ((degree < 0.0) || (minute < 0.0) || (second < 0.0)) {
            isNegative = true;
            degree = Math.abs(degree);
            minute = Math.abs(minute);
            second = Math.abs(second);
        } else
            isNegative = false;

        degree = degree + minute * DPM + second * DPS;

        if (isNegative) {
            return -degree;
        } else
            return degree;
    }

    public static final double DPR = 57.295779513082320876798154814105;
    public static final double RPD = 0.017453292519943295769236907684886;
    public static final double RPS = 0.0000048481368110953599358991410235795;
    public static Double RadianToDegree(Double radian) {
        return radian * DPR;
    }
    public static Double DegreeToRadian(Double degree) {
        return degree * RPD;
    }
    public static Double SecondToRadian(Double second) {
        return second * RPS;
    }

    public static ArrayList<Double> XYZToLBHBowring(Double curra, Double currinvf, Double X, Double Y, Double Z) {
        Double L, B, H;
        Double Rxy, f, e12, e22, tanu, cosu, sinu, temp;
        Double sinB;
        ArrayList<Double> datalist = new ArrayList<Double>();

        if ((X == 0) && (Y == 0)) {
            if (Z < 0) {
                L = 0.0;
                B = -PI / 2;
                H = -(Z + curra * (1 - 1 / currinvf));
            } else if (Z > 0) {
                L = 0.0;
                B = PI / 2;
                H = Z - curra * (1 - 1 / currinvf);
            } else {
                L = 0.0;
                B = 0.0;
                H = -curra;
            }
        }

        Rxy = Math.sqrt(X * X + Y * Y);
        //Get L
        L = Math.acos(X / Rxy);
        if (Y < 0) L = -L;
        //Get B
        f = 1 / currinvf;
        e12 = (2 - f) * f;
        e22 = e12 / (1 - e12);
        tanu = Z * Math.sqrt(1 + e22) / Rxy;
        cosu = 1 / Math.sqrt(1 + tanu * tanu);
        sinu = tanu * cosu;
        temp = Rxy - curra * e12 * cosu * cosu * cosu;
        if (temp == 0) {
            if (Z < 0)
                B = -PI / 2;
            else
                B = PI / 2;
        } else
            B = Math.atan((Z + curra * (1 - f) * e22 * sinu * sinu * sinu) / temp);
        //Get H
        sinB = Math.sin(B);
        if (Math.abs(B) < 4.8e-10)
            H = Rxy / Math.cos(B) - curra / Math.sqrt(1 - e12 * sinB * sinB);
        else
            H = Z / sinB - curra / Math.sqrt(1 - e12 * sinB * sinB) * (1 - e12);

        datalist.add(L);
        datalist.add(B);
        datalist.add(H);

        return datalist;
    }

    public static ArrayList<Double> LBHToXYZ(Double curra, Double currinvf, Double L, Double B, Double H) {
        Double e12, N, X, Y, Z;
        ArrayList<Double> datalist = new ArrayList<Double>();

        e12 = (2.0 - 1.0 / currinvf) / currinvf;
        N = curra / Math.sqrt(1 - e12 * Math.sin(B) * Math.sin(B));
        X = (N + H) * Math.cos(B) * Math.cos(L);
        Y = (N + H) * Math.cos(B) * Math.sin(L);
        Z = (N * (1 - e12) + H) * Math.sin(B);

        datalist.add(X);
        datalist.add(Y);
        datalist.add(Z);

        return datalist;
    }

    public static ArrayList<Double> XYZ7Parameter(Double Xs, Double Ys, Double Zs, Double dX, Double dY, Double dZ, Double ex,
                                                  Double ey, Double ez, Double m) {
        Double Xt, Yt, Zt;
        ArrayList<Double> datalist = new ArrayList<Double>();

        Xt = Xs * (1 + m) + Ys * ez - Zs * ey + dX;
        Yt = Ys * (1 + m) - Xs * ez + Zs * ex + dY;
        Zt = Zs * (1 + m) + Xs * ey - Ys * ex + dZ;

        datalist.add(Xt);
        datalist.add(Yt);
        datalist.add(Zt);

        return datalist;
    }

    public static void main(String args[]) {
        Map<String, Double> model = MapUtils.gcj_encrypt(24.802784, 113.564010);
        System.out.print(model);
    }

}