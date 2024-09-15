package com.github.sn0wkzy.currencies.common.util;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberFormatter {

    private static final Pattern PATTERN = Pattern.compile("([A-z]*)([0-9]*)");

    private final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,###.##");
    private final DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat("##.##");

    private final Object[][] FORMATS = {
        {
            "", "K", "M", "B", "T", "Q", "QQ", "S", "SS", "O", "N", "D", "UN", "DD", "TR", "QT", "QN", "SD", "SPD",
            "OD", "ND", "VG", "UVG", "DVG", "TVG", "QTV", "QNV", "SEV", "SPV", "OVG", "NVG", "ITG"
        },
        {
            1D,
            1000.0,
            1000000.0D,
            1.0E9D,
            1.0E12D,
            1.0E15D,
            1.0E18D,
            1.0E21D,
            1.0E24D,
            1.0E27D,
            1.0E30D,
            1.0E33D,
            1.0E36D,
            1.0E39D,
            1.0E42D,
            1.0E45,
            1.0E48,
            1.0E51D,
            1.0E54D,
            1.0E57D,
            1.0E60D,
            1.0E63D,
            1.0E66,
            1.0E69D,
            1.0E72D,
            1.0E75,
            1.0E78,
            1.0E81D,
            1.0E84D,
            1.0E87D,
            1.0E90D,
            1.0E93D
        }
    };
    private final double LOG = 6.907755278982137D;

    public String formatWithSuffix(double number) {
        if (number <= 999999) return format(number);
        int index = (int) (Math.log(number) / LOG);

        return CURRENCY_FORMAT.format(number / (double) FORMATS[1][index]) + FORMATS[0][index];
    }

    public String format(double value) {
        return CURRENCY_FORMAT.format(value);
    }

    public String formatPercentage(double value) {
        return PERCENTAGE_FORMAT.format(value);
    }

    public boolean isInvalid(double value) {
        return value < 0 || Double.isNaN(value) || Double.isInfinite(value);
    }

    public double deform(String s) {
        final String string = s.toUpperCase();
        final Matcher matcher = PATTERN.matcher(string);

        final List<Object> formats = Arrays.asList(FORMATS[0]);

        int i = 0;
        double value = 0;

        String charName = null;
        while (matcher.find()) {
            if (i == 1) {
                charName = matcher.group();
                break;
            }
            value = Double.parseDouble(matcher.group());
            i++;
        }

        if (charName == null) return Double.parseDouble(s);

        int index = formats.contains(charName) ? formats.indexOf(charName) : 0;
        return value * (double) FORMATS[1][index];
    }
}
