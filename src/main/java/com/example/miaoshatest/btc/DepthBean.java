package com.example.miaoshatest.btc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DepthBean implements Serializable {
    /**
     * ch : market.BTC-USD.depth.step0
     * status : ok
     * ts : 1620826531833
     * tick : {"mrid":95890673618,"id":1620826531,"bids":[[56228.8,22537],[56228.7,101],[56228.6,1],[56228.5,1058],[56228.4,1],[56225.6,943],[56225.5,56],[56225.1,1],[56224.9,39],[56224.3,8],[56223.8,20],[56222.5,1],[56222.1,100],[56222,300],[56221.9,10],[56220.9,545],[56220.8,4172],[56220.6,20],[56220.5,2],[56220.1,1],[56217.7,1090],[56217.6,400],[56217.5,1],[56217.1,10],[56217,631],[56215.1,20],[56212.4,160],[56212.1,1],[56211.3,50],[56211.2,2],[56210.1,20],[56209.6,80],[56209.4,52],[56209.3,3],[56207.3,38],[56207.2,151],[56207.1,68],[56206.1,381],[56205.5,94],[56204,3301],[56203.7,92],[56203.6,864],[56203,39],[56201.9,646],[56201.6,643],[56201.5,895],[56201,86],[56200,266],[56199.6,1],[56199.2,88],[56199,166],[56198.9,1848],[56197.9,47],[56197.6,62],[56196.8,95],[56196.6,157],[56196.3,5],[56196.1,119],[56195.7,60],[56194.8,60],[56194.7,81],[56194.4,5],[56194.3,80],[56194.2,80],[56193.9,100],[56193.3,100],[56192.1,92],[56191.8,6068],[56191.5,2],[56190.3,2],[56190.2,60],[56190.1,2],[56189.8,4],[56189.3,80],[56188.6,2],[56188.2,80],[56187.8,271],[56187.5,141],[56187.2,1],[56186.6,38],[56185.9,94],[56185.1,40],[56184.8,1043],[56184.7,450],[56184.2,92],[56183.9,300],[56183.8,8],[56183.5,88],[56183.3,600],[56183.2,100],[56182.5,40],[56182.4,10],[56182.2,100],[56181.6,120],[56181.5,86],[56181.1,1399],[56181,300],[56180.8,1],[56180,20],[56179.9,90],[56179.7,88],[56179.1,435],[56179,30],[56177.2,90],[56177,99],[56176.7,1],[56174.7,2],[56174.3,9],[56173.6,50],[56173.3,8],[56173,9],[56172.7,720],[56170.4,1],[56170.3,3],[56169.2,80],[56167.8,705],[56167.6,600],[56167.3,1],[56166.9,1],[56166.5,400],[56166.3,100],[56165.7,1],[56165.3,80],[56165.1,100],[56164.9,40],[56163.9,267],[56163.7,516],[56162.9,975],[56162.8,4834],[56161.9,777],[56161.4,40],[56160,50],[56158.9,164],[56157.9,1],[56156.2,135],[56155.8,23],[56154.4,501],[56154,90],[56153.8,1],[56153.6,1],[56153.5,180],[56153,1],[56152.2,4],[56151.8,2],[56150.7,1],[56150.4,1786],[56149.9,300],[56146.2,2776],[56145.9,100],[56141.6,80]],"asks":[[56228.9,12408],[56229,3],[56229.1,3],[56229.2,3],[56229.3,3],[56234.2,80],[56236.4,1],[56237.6,1],[56240,50],[56240.8,400],[56241.9,21],[56243.1,762],[56245,80],[56245.1,41],[56246.6,56],[56246.7,39],[56247.1,100],[56247.2,40],[56248.1,922],[56248.2,629],[56248.4,3],[56250,500],[56250.3,1],[56251.5,204],[56252.2,94],[56252.8,108],[56253.1,141],[56254,92],[56254.4,1],[56255.4,252],[56255.8,2],[56255.9,80],[56256.4,500],[56256.6,80],[56256.7,86],[56257.1,89],[56257.4,1144],[56257.5,2],[56258,60],[56258.4,15],[56258.5,88],[56258.6,60],[56258.7,150],[56259.6,140],[56259.7,81],[56260,20],[56260.1,80],[56260.6,80],[56260.8,60],[56261.1,97],[56261.5,5],[56261.8,50],[56262,87],[56262.1,60],[56262.4,60],[56263.4,60],[56263.8,100],[56264.1,10],[56264.2,1],[56265.6,199],[56265.7,487],[56265.8,2418],[56265.9,1000],[56267.1,40],[56267.5,80],[56267.9,4],[56268.7,20],[56269,4807],[56269.5,988],[56269.9,240],[56271.2,2],[56271.3,400],[56271.4,20],[56271.6,3663],[56271.8,94],[56272.4,1573],[56272.5,10],[56272.7,450],[56272.9,9],[56273.2,10],[56273.3,80],[56273.5,18],[56274.1,8047],[56274.3,160],[56274.7,100],[56276.2,33],[56276.4,23],[56276.9,39],[56277.2,80],[56277.6,15],[56277.7,101],[56278,53],[56278.1,1],[56278.2,1],[56278.4,80],[56278.6,40],[56279.4,1823],[56280,20],[56280.7,97],[56281.1,2290],[56281.5,87],[56281.6,40],[56281.7,90],[56282.7,10],[56283.3,100],[56283.4,600],[56284,300],[56284.4,5],[56285.1,391],[56285.4,20],[56285.9,279],[56286.3,3],[56287.2,44],[56287.4,3],[56288,1],[56288.7,195],[56289.2,2],[56289.5,2],[56289.6,4],[56289.7,123],[56289.9,20],[56290.7,39],[56291.2,1],[56291.5,56],[56291.9,1],[56292,1],[56292.6,808],[56293.5,2],[56293.9,40],[56294.9,94],[56295.1,1113],[56295.5,8],[56295.6,5],[56296.6,392],[56298.4,855],[56298.9,10],[56299,2],[56299.3,89],[56299.4,39],[56300,24],[56300.1,643],[56300.6,21],[56301.1,88],[56301.2,666],[56301.6,271],[56301.8,5328],[56301.9,1],[56302.1,362],[56302.8,125],[56303.7,97]],"ts":1620826531757,"version":1620826531,"ch":"market.BTC-USD.depth.step0"}
     */

    private String ch;
    private String status;
    private long ts;
    private TickBean tick;

    @Data
    public static class TickBean implements Serializable {
        /**
         * mrid : 95890673618
         * id : 1620826531
         * bids : [[56228.8,22537],[56228.7,101],[56228.6,1],[56228.5,1058],[56228.4,1],[56225.6,943],[56225.5,56],[56225.1,1],[56224.9,39],[56224.3,8],[56223.8,20],[56222.5,1],[56222.1,100],[56222,300],[56221.9,10],[56220.9,545],[56220.8,4172],[56220.6,20],[56220.5,2],[56220.1,1],[56217.7,1090],[56217.6,400],[56217.5,1],[56217.1,10],[56217,631],[56215.1,20],[56212.4,160],[56212.1,1],[56211.3,50],[56211.2,2],[56210.1,20],[56209.6,80],[56209.4,52],[56209.3,3],[56207.3,38],[56207.2,151],[56207.1,68],[56206.1,381],[56205.5,94],[56204,3301],[56203.7,92],[56203.6,864],[56203,39],[56201.9,646],[56201.6,643],[56201.5,895],[56201,86],[56200,266],[56199.6,1],[56199.2,88],[56199,166],[56198.9,1848],[56197.9,47],[56197.6,62],[56196.8,95],[56196.6,157],[56196.3,5],[56196.1,119],[56195.7,60],[56194.8,60],[56194.7,81],[56194.4,5],[56194.3,80],[56194.2,80],[56193.9,100],[56193.3,100],[56192.1,92],[56191.8,6068],[56191.5,2],[56190.3,2],[56190.2,60],[56190.1,2],[56189.8,4],[56189.3,80],[56188.6,2],[56188.2,80],[56187.8,271],[56187.5,141],[56187.2,1],[56186.6,38],[56185.9,94],[56185.1,40],[56184.8,1043],[56184.7,450],[56184.2,92],[56183.9,300],[56183.8,8],[56183.5,88],[56183.3,600],[56183.2,100],[56182.5,40],[56182.4,10],[56182.2,100],[56181.6,120],[56181.5,86],[56181.1,1399],[56181,300],[56180.8,1],[56180,20],[56179.9,90],[56179.7,88],[56179.1,435],[56179,30],[56177.2,90],[56177,99],[56176.7,1],[56174.7,2],[56174.3,9],[56173.6,50],[56173.3,8],[56173,9],[56172.7,720],[56170.4,1],[56170.3,3],[56169.2,80],[56167.8,705],[56167.6,600],[56167.3,1],[56166.9,1],[56166.5,400],[56166.3,100],[56165.7,1],[56165.3,80],[56165.1,100],[56164.9,40],[56163.9,267],[56163.7,516],[56162.9,975],[56162.8,4834],[56161.9,777],[56161.4,40],[56160,50],[56158.9,164],[56157.9,1],[56156.2,135],[56155.8,23],[56154.4,501],[56154,90],[56153.8,1],[56153.6,1],[56153.5,180],[56153,1],[56152.2,4],[56151.8,2],[56150.7,1],[56150.4,1786],[56149.9,300],[56146.2,2776],[56145.9,100],[56141.6,80]]
         * asks : [[56228.9,12408],[56229,3],[56229.1,3],[56229.2,3],[56229.3,3],[56234.2,80],[56236.4,1],[56237.6,1],[56240,50],[56240.8,400],[56241.9,21],[56243.1,762],[56245,80],[56245.1,41],[56246.6,56],[56246.7,39],[56247.1,100],[56247.2,40],[56248.1,922],[56248.2,629],[56248.4,3],[56250,500],[56250.3,1],[56251.5,204],[56252.2,94],[56252.8,108],[56253.1,141],[56254,92],[56254.4,1],[56255.4,252],[56255.8,2],[56255.9,80],[56256.4,500],[56256.6,80],[56256.7,86],[56257.1,89],[56257.4,1144],[56257.5,2],[56258,60],[56258.4,15],[56258.5,88],[56258.6,60],[56258.7,150],[56259.6,140],[56259.7,81],[56260,20],[56260.1,80],[56260.6,80],[56260.8,60],[56261.1,97],[56261.5,5],[56261.8,50],[56262,87],[56262.1,60],[56262.4,60],[56263.4,60],[56263.8,100],[56264.1,10],[56264.2,1],[56265.6,199],[56265.7,487],[56265.8,2418],[56265.9,1000],[56267.1,40],[56267.5,80],[56267.9,4],[56268.7,20],[56269,4807],[56269.5,988],[56269.9,240],[56271.2,2],[56271.3,400],[56271.4,20],[56271.6,3663],[56271.8,94],[56272.4,1573],[56272.5,10],[56272.7,450],[56272.9,9],[56273.2,10],[56273.3,80],[56273.5,18],[56274.1,8047],[56274.3,160],[56274.7,100],[56276.2,33],[56276.4,23],[56276.9,39],[56277.2,80],[56277.6,15],[56277.7,101],[56278,53],[56278.1,1],[56278.2,1],[56278.4,80],[56278.6,40],[56279.4,1823],[56280,20],[56280.7,97],[56281.1,2290],[56281.5,87],[56281.6,40],[56281.7,90],[56282.7,10],[56283.3,100],[56283.4,600],[56284,300],[56284.4,5],[56285.1,391],[56285.4,20],[56285.9,279],[56286.3,3],[56287.2,44],[56287.4,3],[56288,1],[56288.7,195],[56289.2,2],[56289.5,2],[56289.6,4],[56289.7,123],[56289.9,20],[56290.7,39],[56291.2,1],[56291.5,56],[56291.9,1],[56292,1],[56292.6,808],[56293.5,2],[56293.9,40],[56294.9,94],[56295.1,1113],[56295.5,8],[56295.6,5],[56296.6,392],[56298.4,855],[56298.9,10],[56299,2],[56299.3,89],[56299.4,39],[56300,24],[56300.1,643],[56300.6,21],[56301.1,88],[56301.2,666],[56301.6,271],[56301.8,5328],[56301.9,1],[56302.1,362],[56302.8,125],[56303.7,97]]
         * ts : 1620826531757
         * version : 1620826531
         * ch : market.BTC-USD.depth.step0
         */

        private long mrid;
        private int id;
        private long ts;
        private int version;
        private String ch;
        private List<List<Double>> bids;
        private List<List<Double>> asks;
    }
}
