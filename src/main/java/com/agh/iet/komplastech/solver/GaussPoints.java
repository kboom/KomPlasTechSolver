package com.agh.iet.komplastech.solver;

class GaussPoints {
    GaussPoints() {
        /*
         * Wikipedia m_points = new double[6]; m_weights = new double[6];
         * m_nr_points = 5; m_points[1]=(1.0+0.0)/2.0; m_weights[1]=128.0/225.0;
         * m_points[2]=(1.0+1.0/3.0*Math.sqrt(5.0-2.0*Math.sqrt(10.0/7.0)))/2.0;
         * m_weights[2]=(323.0+13.0*Math.sqrt(70.0))/900.0;
         * m_points[3]=(1.0-1.0/3.0*Math.sqrt(5.0-2.0*Math.sqrt(10.0/7.0)))/2.0;
         * m_weights[3]=(323.0+13.0*Math.sqrt(70.0))/900.0;
         * m_points[4]=(1.0+1.0/3.0*Math.sqrt(5.0+2.0*Math.sqrt(10.0/7.0)))/2.0;
         * m_weights[4]=(323.0-13.0*Math.sqrt(70.0))/900.0;
         * m_points[5]=(1.0-1.0/3.0*Math.sqrt(5.0+2.0*Math.sqrt(10.0/7.0)))/2.0;
         * m_weights[5]=(323.0-13.0*Math.sqrt(70.0))/900.0;
         */
        /*
         * M. Burton Gaussian quadrature rules for C^1 quintic splines m_points
         * = new double[7]; m_weights = new double[7]; m_nr_points = 6;
         * m_points[1]=0.1225148226554413; m_weights[1]=0.3020174288145723;
         * m_points[2]=0.5441518440112252; m_weights[2]=0.4850196082224646;
         * m_points[3]=1.0064654716056596; m_weights[3]=0.4467177201362911;
         * m_points[4]=1.5002730728687338; m_weights[4]=0.3303872093804185;
         * m_points[5]=2.0000387957905171; m_weights[5]=0.4665398664562177;
         * m_points[6]=2.5; m_weights[6]=0.5333333108648244;
         */
        // L. Demkowicz hp1d quadratures
        m_points = new double[11];
        m_weights = new double[11];
        m_nr_points = 10;
        m_points[1] = (1.0 - 0.973906528517171720077964) * 0.5;
        m_points[2] = (1.0 - 0.8650633666889845107320967) * 0.5;
        m_points[3] = (1.0 - 0.6794095682990244062343274) * 0.5;
        m_points[4] = (1.0 - 0.4333953941292471907992659) * 0.5;
        m_points[5] = (1.0 - 0.1488743389816312108848260) * 0.5;
        m_points[6] = (1.0 + 0.1488743389816312108848260) * 0.5;
        m_points[7] = (1.0 + 0.4333953941292471907992659) * 0.5;
        m_points[8] = (1.0 + 0.6794095682990244062343274) * 0.5;
        m_points[9] = (1.0 + 0.8650633666889845107320967) * 0.5;
        m_points[10] = (1.0 + 0.9739065285171717200779640) * 0.5;
        m_weights[1] = 0.0666713443086881375935688 * 0.5;
        m_weights[2] = 0.1494513491505805931457763 * 0.5;
        m_weights[3] = 0.2190863625159820439955349 * 0.5;
        m_weights[4] = 0.2692667193099963550912269 * 0.5;
        m_weights[5] = 0.2955242247147528701738930 * 0.5;
        m_weights[6] = 0.2955242247147528701738930 * 0.5;
        m_weights[7] = 0.2692667193099963550912269 * 0.5;
        m_weights[8] = 0.2190863625159820439955349 * 0.5;
        m_weights[9] = 0.1494513491505805931457763 * 0.5;
        m_weights[10] = 0.0666713443086881375935688 * 0.5;
    }

    double m_points[];
    double m_weights[];
    int m_nr_points;
}
