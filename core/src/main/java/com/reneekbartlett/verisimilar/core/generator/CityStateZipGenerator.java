package com.reneekbartlett.verisimilar.core.generator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.datasets.key.CityStateZipDatasetKey;
import com.reneekbartlett.verisimilar.core.generator.api.AbstractValueGenerator;
import com.reneekbartlett.verisimilar.core.model.CityStateZip;
import com.reneekbartlett.verisimilar.core.model.Zip4Range;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.CityStateZipSelectionEngine;

/***
 * Select from dataset with CITY$STATE$ZIP INFO TIED TOGETHER
 */
public class CityStateZipGenerator extends AbstractValueGenerator<CityStateZip>{
    private static final Logger LOGGER = LoggerFactory.getLogger(CityStateZipGenerator.class);

    private final CityStateZipSelectionEngine selector;

    public CityStateZipGenerator(CityStateZipSelectionEngine selector) {
        this.selector = selector;
    }

    @Override
    protected CityStateZip generateValue(DatasetResolutionContext ctx, SelectionFilter criteria) {
        return generateCityStateZip(ctx, criteria);
    }

    @Override
    protected Class<CityStateZip> valueType() {
        return CityStateZip.class;
    }

    private CityStateZip generateCityStateZip(DatasetResolutionContext ctx, SelectionFilter filter) {
        CityStateZipDatasetKey cityStateZipKey;
        if(ctx.states() != null) {
            cityStateZipKey = new CityStateZipDatasetKey(ctx.states());
        } else {
            cityStateZipKey = CityStateZipDatasetKey.defaults();
        }

        // TODO: Expand Handling of empty/null/errors
        String[] data;
        try {
            data = selector.select(cityStateZipKey, filter).split("\\$");
            if(data.length != 3) {
                LOGGER.warn("Check cityStateZip value: {}", String.join(" ", data));
            }
        } catch(Exception e) {
            data = new String[] {"XXXX", "XX", "XXXXX" };
        }

        // TODO:  PO BOX
        // TODO:  Add Zip 4 (more complicated than you'd think..)
        //for (Zip4Range r : generatePoBoxZip4Ranges(data[2], 600)) {
        //    LOGGER.debug(r.toString());
        //}

        return new CityStateZip(data[0], data[1], data[2]);
    }

    /**
     * Generate synthetic street-level ZIP+4 ranges.
     * These mimic USPS carrier-route blocks (1000–9999).
     */
    public static List<Zip4Range> generateStreetRanges(String zip5) {
        List<Zip4Range> ranges = new ArrayList<>();

        // Synthetic street blocks (USPS-style)
        int[][] blocks = {
                {1000, 1999},
                {2000, 2999},
                {3000, 3999},
                {4000, 4999},
                {5000, 5999},
                {6000, 6999},
                {7000, 7999},
                {8000, 8999},
                {9000, 9999}
        };

        for (int[] block : blocks) {
            ranges.add(new Zip4Range(zip5, String.format("%04d", block[0]), String.format("%04d", block[1]), "STREET", "Synthetic street-level ZIP+4 block"));
        }

        return ranges;
    }

    /**
     * Combine street ranges + PO Box ranges + USPS universal ZIP+4s.
     */
    public static List<Zip4Range> generateAll(String zip5, int totalPoBoxes) {
        List<Zip4Range> all = new ArrayList<>();
        all.addAll(generateStreetRanges(zip5));
        all.addAll(generatePoBoxZip4Ranges(zip5, totalPoBoxes));
        return all;
    }

    /**
     * Generate synthetic PO Box ZIP+4 ranges following USPS-style logic.
     *
     * @param zip5       5-digit ZIP code (e.g., "01545")
     * @param totalBoxes number of PO Boxes at this ZIP
     * @param blockSize  block size for grouping boxes (USPS commonly uses 100)
     * @return list of Zip4Range records
     */
    public static List<Zip4Range> generatePoBoxZip4Ranges(String zip5, int totalBoxes, int blockSize) {
        List<Zip4Range> ranges = new ArrayList<>();
        for (int start = 1; start <= totalBoxes; start += blockSize) {
            int end = Math.min(start + blockSize - 1, totalBoxes);
            String zip4Start = String.format("%04d", start);
            String zip4End = String.format("%04d", end);
            //ranges.add(new Zip4Range<>(zip5,zip4Start,zip4End,"PO_BOX",String.format("PO BOX %d–%d", start, end)));
            ranges.add(new Zip4Range(zip5,zip4Start,zip4End,"PO_BOX",String.format("PO BOX %d–%d", start, end)));
        }

        // USPS-style universal ZIP+4s
        ranges.add(new Zip4Range(zip5, "9991", "9991", "UNIQUE", "Station / branch"));
        ranges.add(new Zip4Range(zip5, "9998", "9998", "UNIQUE", "Main Post Office"));
        ranges.add(new Zip4Range(zip5, "9999", "9999", "UNIQUE", "Large-volume mail unit"));

        return ranges;
    }

    // Convenience overload with default block size = 100
    public static List<Zip4Range> generatePoBoxZip4Ranges(String zip5, int totalBoxes) {
        return generatePoBoxZip4Ranges(zip5, totalBoxes, 100);
    }

    public static boolean validateState(String state) {
        if(state.length() > 2) {
            return false;
        }
        return true;
    }
}
