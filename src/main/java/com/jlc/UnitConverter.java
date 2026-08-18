package com.jlc;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnitConverter {

    public static class ConversionResult {
        public final double fromValue;
        public final String fromUnit;
        public final double toValue;
        public final String toUnit;
        public final String category;

        public ConversionResult(double fromValue, String fromUnit, double toValue, String toUnit, String category) {
            this.fromValue = fromValue;
            this.fromUnit = fromUnit;
            this.toValue = toValue;
            this.toUnit = toUnit;
            this.category = category;
        }
    }

    private static final Map<String, Double> LENGTH_TO_METERS = new HashMap<>();
    private static final Map<String, Double> MASS_TO_KG = new HashMap<>();
    private static final Map<String, Double> TIME_TO_SECONDS = new HashMap<>();
    private static final Map<String, Double> ENERGY_TO_JOULES = new HashMap<>();
    private static final Map<String, Double> PRESSURE_TO_PASCALS = new HashMap<>();
    private static final Map<String, Double> FREQ_TO_HERTZ = new HashMap<>();
    private static final Map<String, Double> POWER_TO_WATTS = new HashMap<>();
    private static final Map<String, Double> ANGLE_TO_RADIANS = new HashMap<>();

    static {
        // Length -> meters
        LENGTH_TO_METERS.put("m", 1.0);
        LENGTH_TO_METERS.put("meter", 1.0);
        LENGTH_TO_METERS.put("meters", 1.0);
        LENGTH_TO_METERS.put("km", 1000.0);
        LENGTH_TO_METERS.put("kilometer", 1000.0);
        LENGTH_TO_METERS.put("kilometers", 1000.0);
        LENGTH_TO_METERS.put("cm", 0.01);
        LENGTH_TO_METERS.put("centimeter", 0.01);
        LENGTH_TO_METERS.put("centimeters", 0.01);
        LENGTH_TO_METERS.put("mm", 0.001);
        LENGTH_TO_METERS.put("millimeter", 0.001);
        LENGTH_TO_METERS.put("millimeters", 0.001);
        LENGTH_TO_METERS.put("um", 1e-6);
        LENGTH_TO_METERS.put("micrometer", 1e-6);
        LENGTH_TO_METERS.put("nm", 1e-9);
        LENGTH_TO_METERS.put("nanometer", 1e-9);
        LENGTH_TO_METERS.put("nanometers", 1e-9);
        LENGTH_TO_METERS.put("pm", 1e-12);
        LENGTH_TO_METERS.put("in", 0.0254);
        LENGTH_TO_METERS.put("inch", 0.0254);
        LENGTH_TO_METERS.put("inches", 0.0254);
        LENGTH_TO_METERS.put("ft", 0.3048);
        LENGTH_TO_METERS.put("foot", 0.3048);
        LENGTH_TO_METERS.put("feet", 0.3048);
        LENGTH_TO_METERS.put("yd", 0.9144);
        LENGTH_TO_METERS.put("yard", 0.9144);
        LENGTH_TO_METERS.put("yards", 0.9144);
        LENGTH_TO_METERS.put("mi", 1609.344);
        LENGTH_TO_METERS.put("mile", 1609.344);
        LENGTH_TO_METERS.put("miles", 1609.344);
        LENGTH_TO_METERS.put("au", 1.495978707e11);
        LENGTH_TO_METERS.put("ly", 9.4607304725808e15);
        LENGTH_TO_METERS.put("pc", 3.08567758149137e16);

        // Mass -> kg
        MASS_TO_KG.put("kg", 1.0);
        MASS_TO_KG.put("kilogram", 1.0);
        MASS_TO_KG.put("kilograms", 1.0);
        MASS_TO_KG.put("g", 0.001);
        MASS_TO_KG.put("gram", 0.001);
        MASS_TO_KG.put("grams", 0.001);
        MASS_TO_KG.put("mg", 1e-6);
        MASS_TO_KG.put("milligram", 1e-6);
        MASS_TO_KG.put("ug", 1e-9);
        MASS_TO_KG.put("microgram", 1e-9);
        MASS_TO_KG.put("lb", 0.45359237);
        MASS_TO_KG.put("lbs", 0.45359237);
        MASS_TO_KG.put("pound", 0.45359237);
        MASS_TO_KG.put("pounds", 0.45359237);
        MASS_TO_KG.put("oz", 0.028349523125);
        MASS_TO_KG.put("ounce", 0.028349523125);
        MASS_TO_KG.put("ounces", 0.028349523125);
        MASS_TO_KG.put("ton", 907.18474);
        MASS_TO_KG.put("tonne", 1000.0);

        // Time -> seconds
        TIME_TO_SECONDS.put("s", 1.0);
        TIME_TO_SECONDS.put("sec", 1.0);
        TIME_TO_SECONDS.put("second", 1.0);
        TIME_TO_SECONDS.put("seconds", 1.0);
        TIME_TO_SECONDS.put("ms", 0.001);
        TIME_TO_SECONDS.put("millisecond", 0.001);
        TIME_TO_SECONDS.put("us", 1e-6);
        TIME_TO_SECONDS.put("ns", 1e-9);
        TIME_TO_SECONDS.put("min", 60.0);
        TIME_TO_SECONDS.put("minute", 60.0);
        TIME_TO_SECONDS.put("minutes", 60.0);
        TIME_TO_SECONDS.put("h", 3600.0);
        TIME_TO_SECONDS.put("hr", 3600.0);
        TIME_TO_SECONDS.put("hour", 3600.0);
        TIME_TO_SECONDS.put("hours", 3600.0);
        TIME_TO_SECONDS.put("d", 86400.0);
        TIME_TO_SECONDS.put("day", 86400.0);
        TIME_TO_SECONDS.put("days", 86400.0);
        TIME_TO_SECONDS.put("yr", 31557600.0);
        TIME_TO_SECONDS.put("year", 31557600.0);
        TIME_TO_SECONDS.put("years", 31557600.0);

        // Energy -> Joules
        ENERGY_TO_JOULES.put("j", 1.0);
        ENERGY_TO_JOULES.put("joule", 1.0);
        ENERGY_TO_JOULES.put("joules", 1.0);
        ENERGY_TO_JOULES.put("kj", 1000.0);
        ENERGY_TO_JOULES.put("mj", 1e6);
        ENERGY_TO_JOULES.put("gj", 1e9);
        ENERGY_TO_JOULES.put("cal", 4.184);
        ENERGY_TO_JOULES.put("calorie", 4.184);
        ENERGY_TO_JOULES.put("kcal", 4184.0);
        ENERGY_TO_JOULES.put("ev", 1.602176634e-19);
        ENERGY_TO_JOULES.put("kev", 1.602176634e-16);
        ENERGY_TO_JOULES.put("mev", 1.602176634e-13);
        ENERGY_TO_JOULES.put("kwh", 3.6e6);
        ENERGY_TO_JOULES.put("btu", 1055.056);

        // Pressure -> Pascals
        PRESSURE_TO_PASCALS.put("pa", 1.0);
        PRESSURE_TO_PASCALS.put("pascal", 1.0);
        PRESSURE_TO_PASCALS.put("pascals", 1.0);
        PRESSURE_TO_PASCALS.put("kpa", 1000.0);
        PRESSURE_TO_PASCALS.put("mpa", 1e6);
        PRESSURE_TO_PASCALS.put("bar", 100000.0);
        PRESSURE_TO_PASCALS.put("mbar", 100.0);
        PRESSURE_TO_PASCALS.put("atm", 101325.0);
        PRESSURE_TO_PASCALS.put("psi", 6894.75729);
        PRESSURE_TO_PASCALS.put("torr", 133.322);
        PRESSURE_TO_PASCALS.put("mmhg", 133.322);

        // Frequency -> Hertz
        FREQ_TO_HERTZ.put("hz", 1.0);
        FREQ_TO_HERTZ.put("hertz", 1.0);
        FREQ_TO_HERTZ.put("khz", 1000.0);
        FREQ_TO_HERTZ.put("mhz", 1e6);
        FREQ_TO_HERTZ.put("ghz", 1e9);

        // Power -> Watts
        POWER_TO_WATTS.put("w", 1.0);
        POWER_TO_WATTS.put("watt", 1.0);
        POWER_TO_WATTS.put("watts", 1.0);
        POWER_TO_WATTS.put("kw", 1000.0);
        POWER_TO_WATTS.put("mw", 1e6);
        POWER_TO_WATTS.put("gw", 1e9);
        POWER_TO_WATTS.put("hp", 745.699872);

        // Angle -> Radians
        ANGLE_TO_RADIANS.put("rad", 1.0);
        ANGLE_TO_RADIANS.put("radian", 1.0);
        ANGLE_TO_RADIANS.put("radians", 1.0);
        ANGLE_TO_RADIANS.put("deg", Math.PI / 180.0);
        ANGLE_TO_RADIANS.put("degree", Math.PI / 180.0);
        ANGLE_TO_RADIANS.put("degrees", Math.PI / 180.0);
        ANGLE_TO_RADIANS.put("arcmin", Math.PI / (180.0 * 60.0));
        ANGLE_TO_RADIANS.put("arcsec", Math.PI / (180.0 * 3600.0));
    }

    public static ConversionResult convert(double value, String from, String to) {
        String f = from.toLowerCase().trim();
        String t = to.toLowerCase().trim();

        // Temperature conversion
        if (isTemp(f) && isTemp(t)) {
            double c = toCelsius(value, f);
            double res = fromCelsius(c, t);
            return new ConversionResult(value, from, res, to, "Temperature");
        }

        // Length
        if (LENGTH_TO_METERS.containsKey(f) && LENGTH_TO_METERS.containsKey(t)) {
            double meters = value * LENGTH_TO_METERS.get(f);
            double res = meters / LENGTH_TO_METERS.get(t);
            return new ConversionResult(value, from, res, to, "Length");
        }

        // Mass
        if (MASS_TO_KG.containsKey(f) && MASS_TO_KG.containsKey(t)) {
            double kg = value * MASS_TO_KG.get(f);
            double res = kg / MASS_TO_KG.get(t);
            return new ConversionResult(value, from, res, to, "Mass");
        }

        // Time
        if (TIME_TO_SECONDS.containsKey(f) && TIME_TO_SECONDS.containsKey(t)) {
            double sec = value * TIME_TO_SECONDS.get(f);
            double res = sec / TIME_TO_SECONDS.get(t);
            return new ConversionResult(value, from, res, to, "Time");
        }

        // Energy
        if (ENERGY_TO_JOULES.containsKey(f) && ENERGY_TO_JOULES.containsKey(t)) {
            double joules = value * ENERGY_TO_JOULES.get(f);
            double res = joules / ENERGY_TO_JOULES.get(t);
            return new ConversionResult(value, from, res, to, "Energy");
        }

        // Pressure
        if (PRESSURE_TO_PASCALS.containsKey(f) && PRESSURE_TO_PASCALS.containsKey(t)) {
            double pa = value * PRESSURE_TO_PASCALS.get(f);
            double res = pa / PRESSURE_TO_PASCALS.get(t);
            return new ConversionResult(value, from, res, to, "Pressure");
        }

        // Frequency
        if (FREQ_TO_HERTZ.containsKey(f) && FREQ_TO_HERTZ.containsKey(t)) {
            double hz = value * FREQ_TO_HERTZ.get(f);
            double res = hz / FREQ_TO_HERTZ.get(t);
            return new ConversionResult(value, from, res, to, "Frequency");
        }

        // Power
        if (POWER_TO_WATTS.containsKey(f) && POWER_TO_WATTS.containsKey(t)) {
            double w = value * POWER_TO_WATTS.get(f);
            double res = w / POWER_TO_WATTS.get(t);
            return new ConversionResult(value, from, res, to, "Power");
        }

        // Angle
        if (ANGLE_TO_RADIANS.containsKey(f) && ANGLE_TO_RADIANS.containsKey(t)) {
            double rad = value * ANGLE_TO_RADIANS.get(f);
            double res = rad / ANGLE_TO_RADIANS.get(t);
            return new ConversionResult(value, from, res, to, "Angle");
        }

        return null;
    }

    private static boolean isTemp(String u) {
        return u.equals("c") || u.equals("celsius") || u.equals("f") || u.equals("fahrenheit") || u.equals("k") || u.equals("kelvin");
    }

    private static double toCelsius(double v, String u) {
        if (u.startsWith("c")) return v;
        if (u.startsWith("f")) return (v - 32) * 5.0 / 9.0;
        if (u.startsWith("k")) return v - 273.15;
        return v;
    }

    private static double fromCelsius(double c, String u) {
        if (u.startsWith("c")) return c;
        if (u.startsWith("f")) return (c * 9.0 / 5.0) + 32;
        if (u.startsWith("k")) return c + 273.15;
        return c;
    }

    private static final Pattern CONVERT_REGEX = Pattern.compile("^(\\d+(?:\\.\\d+)?(?:[eE][+-]?\\d+)?)\\s*([a-zA-Z°]+)\\s+(?:to|in|into)\\s+([a-zA-Z°]+)$", Pattern.CASE_INSENSITIVE);

    public static ConversionResult parseAndConvert(String input) {
        if (input == null) return null;
        String clean = input.trim().replace("convert ", "").replace("Convert ", "");
        Matcher m = CONVERT_REGEX.matcher(clean);
        if (m.matches()) {
            double val = Double.parseDouble(m.group(1));
            String from = m.group(2);
            String to = m.group(3);
            return convert(val, from, to);
        }
        return null;
    }
}
