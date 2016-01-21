package cn.goodjobs.common.util;

import android.location.Location;


public class GeoUtils
{
    public static final double EARTHRADIUS = 6370996.81;
    public static final int SELFTYPE = 0;
    public static final int XIAOQUTYPE = 1;


    public static long distance(Location loc1, Location loc2)
    {
        return distance(loc1.getLatitude(), loc1.getLongitude(), loc2.getLatitude(), loc2.getLongitude());
    }

    public static long distance(double lat1, double lon1, double lat2, double lon2)
    {
        double delta_lat = lat2 - lat1;
        double delta_lon = lon2 - lon1;

        double alpha = delta_lat / 2;
        double beta = delta_lon / 2;
        double a = Math.sin(deg2rad(alpha)) * Math.sin(deg2rad(alpha)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.sin(deg2rad(beta)) * Math.sin(deg2rad(beta));
        double c = Math.asin(Math.min(1, Math.sqrt(a)));
        double distance = 2 * EARTHRADIUS * c;

        return Math.round(distance);
    }

    public static String friendlyDistance(long d)
    {
        if (d < 1000 && d > 0) {
            return (int) d + "m";
        } else if (d == 0) {
            return "";
        } else {
            return String.format("%.1fkm", d / 1000f);
        }
    }

    public static String friendlyDistance(double d)
    {
        if (d < 1000) {
            return (int) d + "米";
        } else {
            return String.format("%.1f千米", d / 1000);
        }
    }

    private static double deg2rad(double deg)
    {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad)
    {
        return (rad * 180.0 / Math.PI);
    }

    private static double[] MCBAND = {12890594.86, 8362377.87, 5591021, 3481989.83, 1678043.12, 0};

    private static double[] LLBAND = {75, 60, 45, 30, 15, 0};

    private static double[][] MC2LL = {
            {1.410526172116255e-8, 0.00000898305509648872, -1.9939833816331, 200.9824383106796, -187.2403703815547,
                    91.6087516669843, -23.38765649603339, 2.57121317296198, -0.03801003308653, 17337981.2},
            {-7.435856389565537e-9, 0.000008983055097726239, -0.78625201886289, 96.32687599759846, -1.85204757529826,
                    -59.36935905485877, 47.40033549296737, -16.50741931063887, 2.28786674699375, 10260144.86},
            {-3.030883460898826e-8, 0.00000898305509983578, 0.30071316287616, 59.74293618442277, 7.357984074871,
                    -25.38371002664745, 13.45380521110908, -3.29883767235584, 0.32710905363475, 6856817.37},
            {-1.981981304930552e-8, 0.000008983055099779535, 0.03278182852591, 40.31678527705744, 0.65659298677277,
                    -4.44255534477492, 0.85341911805263, 0.12923347998204, -0.04625736007561, 4482777.06},
            {3.09191371068437e-9, 0.000008983055096812155, 0.00006995724062, 23.10934304144901, -0.00023663490511,
                    -0.6321817810242, -0.00663494467273, 0.03430082397953, -0.00466043876332, 2555164.4},
            {2.890871144776878e-9, 0.000008983055095805407, -3.068298e-8, 7.47137025468032, -0.00000353937994,
                    -0.02145144861037, -0.00001234426596, 0.00010322952773, -0.00000323890364, 826088.5}};

    private static double[][] LL2MC = {
            {-0.0015702102444, 111320.7020616939, 1704480524535203.0, -10338987376042340.0, 26112667856603880.0,
                    -35149669176653700.0, 26595700718403920.0, -10725012454188240.0, 1800819912950474.0, 82.5},
            {0.0008277824516172526, 111320.7020463578, 647795574.6671607, -4082003173.641316, 10774905663.51142,
                    -15171875531.51559, 12053065338.62167, -5124939663.577472, 913311935.9512032, 67.5},
            {0.00337398766765, 111320.7020202162, 4481351.045890365, -23393751.19931662, 79682215.47186455,
                    -115964993.2797253, 97236711.15602145, -43661946.33752821, 8477230.501135234, 52.5},
            {0.00220636496208, 111320.7020209128, 51751.86112841131, 3796837.749470245, 992013.7397791013,
                    -1221952.21711287, 1340652.697009075, -620943.6990984312, 144416.9293806241, 37.5},
            {-0.0003441963504368392, 111320.7020576856, 278.2353980772752, 2485758.690035394, 6070.750963243378,
                    54821.18345352118, 9540.606633304236, -2710.55326746645, 1405.483844121726, 22.5},
            {-0.0003218135878613132, 111320.7020701615, 0.00369383431289, 823725.6402795718, 0.46104986909093,
                    2351.343141331292, 1.58060784298199, 8.77738589078284, 0.37238884252424, 7.45}};

    public static double[] convertMC2LL(double[] cy)
    {
        double cz[] = {Math.abs(cy[0]), Math.abs(cy[1])};

        double[] cB = {};
        for (int cA = 0; cA < MCBAND.length; cA++) {
            if (cz[1] >= MCBAND[cA]) {
                cB = MC2LL[cA];
                break;
            }
        }
        return convertor(cy, cB);
    }

    public static double[] convertLL2MC(double[] point)
    {
        double[] cL = {};
        for (int cK = 0; cK < LLBAND.length; cK++) {
            if (point[1] >= LLBAND[cK]) {
                cL = LL2MC[cK];
                break;
            }
        }

        if (cL == null || cL.length == 0) {
            for (int cK = LLBAND.length - 1; cK >= 0; cK--) {
                if (point[1] <= LLBAND[cK]) {
                    cL = LL2MC[cK];
                    break;
                }
            }
        }
        return convertor(point, cL);
    }

    public static double[] convertor(double[] cz, double[] cA)
    {
        double T = cA[0] + cA[1] * Math.abs(cz[0]);
        double cy = Math.abs(cz[1]) / cA[9];
        double cB = cA[2] + cA[3] * cy + cA[4] * cy * cy + cA[5] * cy * cy * cy + cA[6] * cy * cy * cy * cy + cA[7]
                * cy * cy * cy * cy * cy + cA[8] * cy * cy * cy * cy * cy * cy;
        T *= (cz[0] < 0 ? -1 : 1);
        cB *= (cz[1] < 0 ? -1 : 1);
        return new double[]{T, cB};
    }

}
