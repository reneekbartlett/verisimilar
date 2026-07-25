package com.reneekbartlett.verisimilar.core.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/***
 * https://pe.usps.com/text/pub28/28apc_002.htm
 * https://pe.usps.com/text/pub28/28c2_015.htm
 * 
 * If an address has two consecutive words that appear on the suffix table (Appendix C), 
 * abbreviate the second of the two words according to the suffix table and place it in the suffix field. 
 * The first of the two words is part of the street name. 
 * Spell it out on the mail piece in its entirety after the street name.
 * 
 * Preferred:  "789 MAIN AVENUE DR"
 * Acceptable: "789 MAIN AVENUE DRIVE"
 * 
 * Preferred:  "4513 3RD STREET CIR W"
 * Acceptable: "4513 3RD STREET CIRCLE WEST"
 * 
 * Preferred:  "1000 AVENUE E"
 * Acceptable: "1000 AVE E" 
 */
public enum StreetSuffix implements WeightedEnumData {
    ALLEY("ALY", 0.1000, "ALLEE", "ALLEY", "ALLY", "ALY"),
    ANEX("ANX", 0.0001, "ANEX", "ANNEX", "ANNX", "ANX"),
    ARCADE("ARC", 0.0000, "ARC", "ARCADE"),
    AVENUE("AVE", 0.0000, "AVENUE", "AVE", "AV", "AVEN", "AVENU", "AVN", "AVNUE"),

    BAYOU("BYU", 0.0000, "BAYOU", "BYU", "BAYOO"),
    BEACH("BCH", 0.0000, "BEACH", "BCH"),
    BEND("BND", 0.0000, "BEND", "BND"),
    BLUFF("BLF", 0.0000, "BLUFF", "BLF", "BLUF"),
    BLUFFS("BLFS", 0.0000, "BLUFFS", "BLFS"), // TODO:  Check dataset
    BOTTOM("BTM", 0.0000, "BOTTOM", "BTM", "BOT", "BOTTM"),
    BOULEVARD("BLVD", 0.0000, "BOULEVARD", "BLVD", "BOUL", "BOULV"),
    BRANCH("BR", 0.0000, "BRANCH", "BR", "BRNCH"),
    BRIDGE("BRG", 0.0000, "BRIDGE", "BRG", "BRDGE"),
    BROOK("BRK", 0.0000, "BROOK", "BRK"),
    BROOKS("BRKS", 0.0000, "BROOKS", "BRKS"),
    BURG("BG", 0.0000, "BURG", "BG"),
    BURGS("BGS", 0.0000, "BURGS", "BGS"),
    BYPASS("BYP", 0.0000, "BYPASS", "BYP", "BYPA", "BYPAS", "BYPS"),

    CAMP("CP", 0.0000, "CAMP","CP", "CMP"),
    CANYON("CYN", 0.0000, "CANYON","CYN", "CNYN"),
    CAPE("CPE", 0.0000, "CAPE","CPE"),
    CAUSEWAY("CSWY", 0.0000, "CAUSEWAY","CSWY", "CAUSWA"),
    CENTER("CTR", 0.0000, "CENTER","CTR", "CEN", "CENT", "CENTR", "CENTRE", "CNTER", "CNTR"),
    CENTERS("CTRS", 0.0000, "CENTERS","CTRS"),

    CIRCLE("CIR", 0.0000, "CIRCLE", "CIR", "CIRC", "CIRCL", "CRCL", "CRCLE"),
    CIRCLES("CIRS", 0.0000, "CIRCLES","CIRS"),
    CLIFF("CLF", 0.0000, "CLIFF","CLF"),
    CLIFFS("CLFS", 0.0000, "CLIFFS","CLFS"),
    CLUB("CLB", 0.0000, "CLUB","CLB"),
    COMMON("CMN", 0.0000, "COMMON","CMN"),
    CORNER("COR", 0.0000, "CORNER","COR"),
    CORNERS("CORS", 0.0000, "CORNERS","CORS"),
    COURSE("CRSE", 0.0000, "COURSE","CRSE"),
    COURT("CT", 0.0000, "COURT","CT"),
    COURTS("CTS", 0.0000, "COURTS","CTS"),
    COVE("CV", 0.0000, "COVE","CV"),
    COVES("CVS", 0.0000, "COVES","CVS"),
    CREEK("CRK", 0.0000, "CREEK","CRK"),

    CRESCENT("CRES", 0.0000, "CRESCENT","CRES", "CRSENT", "CRSNT"), //
    CREST("CRST", 0.0000, "CREST","CRST"),
    CROSSING("XING", 0.0000, "CROSSING","XING", "CRSSNG"), //
    CROSSROAD("XRD", 0.0000, "CROSSROAD","XRD"),
    CROSSROADS("XRDS", 0.0000, "CROSSROADS","XRDS"), // TODO: add to dataset
    CURVE("CURV", 0.0000, "CURVE","CURV", "CRV"),

    DALE("DL", 0.0000, "DALE","DL"),
    DAM("DM", 0.0000, "DAM","DM"),
    DIVIDE("DV", 0.0000, "DIVIDE", "DV", "DIV", "DVD"),
    DRIVE("DR", 0.0000, "DRIVE", "DR", "DRIV", "DRV"),
    DRIVES("DRS", 0.0000, "DRIVES", "DRS"),

    ESTATE("EST", 0.0000, "ESTATE","EST"), // TODO: add to dataset
    ESTATES("ESTS", 0.0000, "ESTATES","ESTS"), // TODO: add to dataset
    EXPRESSWAY("EXPY", 0.0000, "EXPRESSWAY","EXPY", "EXP", "EXPR", "EXPRESS", "EXPW", "EXPY"),
    EXTENSION("EXT", 0.0000, "EXTENSION","EXT", "EXTN", "EXTNSN"),
    EXTENSIONS("EXTS", 0.0000, "EXTENSIONS","EXTS"),

    FALL("FALL", 0.0000, "FALL"), // TODO: Add to dataset
    FALLS("FLS", 0.0000, "FALLS","FLS"),
    FERRY("FRY", 0.0000, "FERRY","FRY","FRRY"),
    FIELD("FLD", 0.0000, "FIELD","FLD"),
    FIELDS("FLDS", 0.0000, "FIELDS","FLDS"),
    FLAT("FLT", 0.0000, "FLAT","FLT"),
    FLATS("FLTS", 0.0000, "FLATS","FLTS"),
    FORD("FRD", 0.0000, "FORD","FRD"),
    FORDS("FRDS", 0.0000, "FORDS","FRDS"),
    FOREST("FRST", 0.0000, "FOREST","FRST", "FORESTS"),
    FORGE("FGR", 0.0000, "FORGE","FGR", "FORG"),
    FORGES("FGRS", 0.0000, "FORGES","FGRS"),
    FORK("FRK", 0.0000, "FORK","FRK"),
    FORKS("FRKS", 0.0000, "FORKS","FRKS"),
    FORT("FT", 0.0000, "FORT","FT", "FRT"),
    FREEWAY("FWY", 0.0000, "FREEWAY","FWY", "FREEWY", "FRWAY", "FRWY"),
    GARDEN("GDN", 0.0000, "GARDEN", "GDN", "GARDN", "GRDEN", "GRDN"),
    GARDENS("GDNS", 0.0000, "GARDENS","GDNS", "GRDNS"),
    GATEWAY("GTWY", 0.0000, "GATEWAY","GTWY", "GATEWY", "GATWAY", "GTWAY"),
    GLEN("GLN", 0.0000, "GLEN","GLN"),
    GLENS("GLNS", 0.0000, "GLENS","GLNS"),
    GREEN("GRN", 0.0000, "GREEN","GRN"),
    GREENS("GRNS", 0.0000, "GREENS","GRNS"),
    GROVE("GRV", 0.0000, "GROVE","GRV", "GROV"),
    GROVES("GRVS", 0.0000, "GROVES","GRVS"),

    HARBOR("HBR", 0.0000, "HARBOR","HBR", "HARB", "HARBR", "HRBOR"),
    HARBORS("HBRS", 0.0000, "HARBORS","HBRS"),
    HAVEN("HVN", 0.0000, "HAVEN","HVN"),
    HEIGHTS("HTS", 0.0000, "HEIGHTS","HTS"),
    HIGHWAY("HWY", 0.0000, "HIGHWAY","HWY", "HIGHWY", "HIWAY", "HIWY", "HWAY"),
    HILL("HL", 0.0000, "HILL","HL"),
    HILLS("HLS", 0.0000, "HILLS","HLS"),
    HOLLOW("HOLW", 0.0000, "HOLLOW","HOLW", "HLLW", "HOLLOWS", "HOLWS"),

    INLET("INLT", 0.0000, "INLET","INLT"),
    ISLAND("IS", 0.0000, "ISLAND","IS", "ISLND"),
    ISLANDS("ISS", 0.0000, "ISLANDS","ISS","ISLNDS"),
    ISLE("ISLE", 0.0000, "ISLE","ISLES"),

    JUNCTION("JCT", 0.0000, "JUNCTION","JCT", "JCTION", "JCTN", "JUNCTN", "JUNCTON"),
    JUNCTIONS("JCTS", 0.0000, "JUNCTIONS","JCTS", "JCTNS"),

    KEY("KY", 0.0000, "KEY","KY"),
    KEYS("KYS", 0.0000, "KEYS","KYS"),
    KNOLL("KNL", 0.0000, "KNOLL","KNL", "KNOL"),
    KNOLLS("KNLS", 0.0000, "KNOLLS","KNLS"),

    LAKE("LK", 0.0000, "LAKE","LK"),
    LAKES("LKS", 0.0000, "LAKES","LKS"),
    LAND("LAND", 0.0000, "LAND"), // TODO: add to dataset
    LANDING("LNDG", 0.0000, "LANDING","LNDG", "LNDNG"),
    LANE("LN", 0.0000, "LANE","LN"),
    LIGHT("LGT", 0.0000, "LIGHT","LGT"),
    LIGHTS("LGTS", 0.0000, "LIGHTS","LGTS"),
    LOAF("LF", 0.0000, "LOAF","LF"),
    LOCK("LCK", 0.0000, "LOCK","LCK"),
    LOCKS("LCKS", 0.0000, "LOCKS","LCKS"),
    LODGE("LDG", 0.0000, "LODGE","LDG", "LDGE", "LODG"),
    LOOP("LOOP", 0.0000, "LOOP", "LOOPS", "LP", "LPS"),

    MALL("MALL", 0.0000, "MALL"),
    MANOR("MNR", 0.0000, "MANOR","MNR"),
    MANORS("MNRS", 0.0000, "MANORS","MNRS"),
    MEADOW("MDW", 0.0000, "MEADOW","MDW", "MEDOW"),
    MEADOWS("MDWS", 0.0000, "MEADOWS","MDWS", "MEDOWS"),
    MILL("ML", 0.0000, "MILL","ML"),
    MILLS("MLS", 0.0000, "MILLS","MLS"),
    MISSION("MSN", 0.0000, "MISSION","MSN"),
    MOTORWAY("MTWY", 0.0000, "MOTORWAY","MTWY", "MOTORWY"),

    MOUNT("MT", 0.0000, "MOUNT","MT", "MNT"),
    MOUNTAIN("MTN", 0.0000, "MOUNTAIN","MTN", "MTIN", "MNTN", "MNTAIN", "MOUNTIN"),
    MOUNTAINS("MTNS", 0.0000, "MOUNTAINS","MTNS"),

    NECK("NCK", 0.0000, "NECK","NCK"),

    ORCHARD("ORCH", 0.0000, "ORCHARD","ORCH", "ORCHRD"),
    OVAL("OVAL", 0.0000, "OVAL","OVL"),
    OVERPASS("OPAS", 0.0000, "OVERPASS","OPAS"), // TODO: add to dataset

    PARK("PARK", 0.0000, "PARK", "PRK"),
    PARKS("PARK", 0.0000, "PARKS","PRKS"), // TODO:  check usps standard

    PARKWAY("PKWY", 0.0000, "PARKWAY","PKWY"),
    PARKWAYS("PKWY", 0.0000, "PARKWAYS","PKWY"), // TODO:  check usps standard

    PASS("PASS", 0.0000, "PASS"),
    PASSAGE("PSGE", 0.0000, "PASSAGE","PSGE", "PSSG"),
    PATH("PATH", 0.0000, "PATH", "PATHS"),
    PIKE("PIKE", 0.0000, "PIKE", "PIKES"),

    PINE("PNE", 0.0000, "PINE","PNE"),
    PINES("PNES", 0.0000, "PINES","PNES"), // TODO: add to dataset

    PLACE("PL", 0.0000, "PLACE","PL"),
    PLAIN("PLN", 0.0000, "PLAIN","PLN"),
    PLAINS("PLNS", 0.0000, "PLAINS","PLNS"),
    PLAZA("PLZ", 0.0000, "PLAZA","PLZ", "PLZA"),
    POINT("PT", 0.0000, "POINT","PT"),
    POINTS("PTS", 0.0000, "POINTS","PTS"),
    PORT("PRT", 0.0000, "PORT","PRT"),
    PORTS("PRTS", 0.0000, "PORTS","PRTS"),
    PRAIRIE("PR", 0.0000, "PRAIRIE","PR", "PRR"),

    RADIAL("RADL", 0.0000, "RADIAL","RADL", "RAD", "RADIEL", "RADL"),

    RAMP("RAMP", 0.0000, "RAMP"), // TODO: add to dataset

    RANCH("RNCH", 0.0000, "RANCH","RNCH", "RANCHES", "RNCH", "RNCHS"),

    RAPID("RPD", 0.0000, "RAPID", "RPD"), // TODO: add to dataset
    RAPIDS("RPDS", 0.0000, "RAPIDS", "RPDS"), // TODO: add to dataset

    REST("RST", 0.0000, "REST", "RST"), // TODO: add to dataset

    RIDGE("RDG", 0.0000, "RIDGE","RDG", "RDGE"),
    RIDGES("RDGS", 0.0000, "RIDGES","RDGS", "RDGES"),

    RIVER("RIV", 0.0000, "RIVER","RIV", "RVR", "RIVR"),
    ROAD("RD", 0.0000, "ROAD","RD"),
    ROADS("RDS", 0.0000, "ROADS","RDS"),

    ROUTE("RTE", 0.0000, "ROUTE","RTE"),
    ROW("ROW", 0.0000, "ROW"),
    RUE("RUE", 0.0000, "RUE"),
    RUN("RUN", 0.0000, "RUN"),

    SHOAL("SHL", 0.0000, "SHOAL","SHL"),
    SHOALS("SHLS", 0.0000, "SHOALS","SHLS"),
    SHORE("SHR", 0.0000, "SHORE","SHR", "SHOAR"),
    SHORES("SHRS", 0.0000, "SHORES","SHRS", "SHOARS"),

    SKYWAY("SKWY", 0.0000, "SKYWAY","SKWY"),
    
    SPRING("SPG", 0.0000, "SPRING","SPG", "SPNG", "SPRNG"),
    SPRINGS("SPGS", 0.0000, "SPRINGS","SPGS", "SPNGS", "SPRNGS"),
    SPUR("SPUR", 0.0000, "SPUR", "SPURS"), // TODO: check if spurs has a standard?

    SQUARE("SQ", 0.0000, "SQUARE","SQ", "SQR", "SQRE", "SQU"),
    SQUARES("SQS", 0.0000, "SQUARES","SQS", "SQRS"),

    STATION("STA", 0.0000, "STATION","STA", "STATN", "STN"),

    STRAVENUE("STRA", 0.0000, "STRAVENUE","STRA", "STRAV", "STRAVEN", "STRAVN", "STRVN", "STRVNUE"),

    STREAM("STRM", 0.0000, "STREAM","STRM", "STREME"),
    STREET("ST", 0.0000, "STREET","ST", "STRT", "STR"),
    STREETS("STS", 0.0000, "STREETS","STS"),
    SUMMIT("SMT", 0.0000, "SUMMIT","SMT", "SUMIT", "SUMITT"),

    TERRACE("TER", 0.0000, "TERRACE","TER", "TERR"),
    THROUGHWAY("TRWY", 0.0000, "THROUGHWAY","TRWY"),

    TRACE("TRCE", 0.0000, "TRACE","TRCE", "TRACES"),

    TRACK("TRAK", 0.0000, "TRACK","TRAK", "TRACKS", "TRK", "TRKS"),
    TRAFFICWAY("TRFY", 0.0000, "TRAFFICWAY","TRFY"), // TODO: add alts

    TRAIL("TRL", 0.0000, "TRAIL","TRL", "TRAILS", "TRLS"),

    TRAILER("TRLR", 0.0000, "TRAIL","TRL", "TRAILER", "TRLR", "TRLRS"), // TODO: add to dataset

    TUNNEL("TUNL", 0.0000, "TUNNEL","TUNL", "TUNEL", "TUNL", "TUNLS", "TUNNELS", "TUNNL"),
    TURNPIKE("TPKE", 0.0000, "TURNPIKE","TPKE", "TRNPK", "TURNPK"),

    UNDERPASS("UPAS", 0.0000, "UNDERPASS","UPAS"), // TODO: add alts
    UNION("UN", 0.0000, "UNION","UN", "UNN"),
    UNIONS("UNS", 0.0000, "UNIONS","UNS","UNNS"),

    VALLEY("VLY", 0.0000, "VALLEY","VLY", "VALLY", "VLLY"),
    VALLEYS("VLYS", 0.0000, "VALLEYS","VLYS", "VALLYS", "VLLYS"),

    VIADUCT("VIA", 0.0000, "VIADUCT","VIA", "VDCT", "VIADCT"),
    VIEW("VW", 0.0000, "VIEW","VW"),
    VIEWS("VWS", 0.0000, "VIEWS","VWS"),

    VILLAGE("VLG", 0.0000, "VILLAGE", "VLG", "VILL", "VILLAG", "VILLG", "VILLIAGE"),
    VILLAGES("VLGS", 0.0000, "VILLAGES","VLGS"),
    VILLE("VL", 0.0000, "VILLE","VL"),
    VISTA("VIS", 0.0000, "VISTA","VIS", "VIST", "VST", "VSTA"),

    WALK("WALK", 0.0000, "WALK","WALKS"),
    WALKS("WALK", 0.0000, "WALKS","WALK"), // TODO: Combine with WALK?

    WALL("WALL", 0.0000, "WALL"),
    WAY("WAY", 0.0000, "WAY", "WY"),
    WAYS("WAYS", 0.0000, "WAYS"),
    WELL("WL", 0.0000, "WELL", "WL"),
    WELLS("WLS", 0.0000, "WELLS", "WLS"),

    NONE("", 0.0000, " ");

    private final String label;
    private final double weight;
    private final String[] searchStr;

    private StreetSuffix(String label, double weight, String... searchStr) {
        this.label = label; // usps standard
        this.weight = weight;
        this.searchStr = searchStr;
    }

    public String getLabel() {
        return label;
    }

    public double getWeight() {
        return weight;
    }

    public String[] getSearchStrings() {
        return searchStr;
    }

    public static EnumSet<StreetSuffix> defaultDatasets(){
        return EnumSet.allOf(StreetSuffix.class);
    }

    public static Map<StreetSuffix, Double> defaultMap() {
        Map<StreetSuffix, Double> defaultMap = HashMap.newHashMap(defaultDatasets().size());
        for(StreetSuffix streetSuffix : defaultDatasets()) {
            defaultMap.put(streetSuffix, streetSuffix.getWeight());
        }
        return defaultMap;
    }

    public static StreetSuffix fromLabel(String label) {
        for (StreetSuffix streetSuffix : EnumSet.allOf(StreetSuffix.class)) {
            if (streetSuffix.name().equalsIgnoreCase(label) || streetSuffix.getLabel().equalsIgnoreCase(label)) {
                return streetSuffix;
            }
        }
        return null;
    }

    public static EnumSet<StreetSuffix> convertToEnumSet(Set<String> suffixes){
        EnumSet<StreetSuffix> streetSuffixEnumSet = EnumSet.noneOf(StreetSuffix.class);
        for(String streetSuffix : suffixes) {
            StreetSuffix ss = StreetSuffix.fromLabel(streetSuffix);
            if(ss != null) {
                streetSuffixEnumSet.add(ss);
            }
        }
        return streetSuffixEnumSet;
    }

    public static EnumSet<StreetSuffix> getWeightedEnumDataSet() {
        return defaultDatasets();
    }
}
