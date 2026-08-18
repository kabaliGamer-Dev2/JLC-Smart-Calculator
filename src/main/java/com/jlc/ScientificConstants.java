package com.jlc;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScientificConstants {

    public static class ConstantInfo {
        public final String symbol;
        public final String name;
        public final double value;
        public final String unit;
        public final String description;

        public ConstantInfo(String symbol, String name, double value, String unit, String description) {
            this.symbol = symbol;
            this.name = name;
            this.value = value;
            this.unit = unit;
            this.description = description;
        }
    }

    private static final Map<String, ConstantInfo> CONSTANTS = new LinkedHashMap<>();

    static {
        // Universal Mathematical Constants
        add("pi", "π (Pi)", Math.PI, "", "Ratio of a circle's circumference to its diameter");
        add("e", "Euler's Number (e)", Math.E, "", "Base of the natural logarithm");
        add("phi", "Golden Ratio (φ)", 1.618033988749895, "", "Golden ratio proportion");

        // Physics & Universal Physical Constants
        add("c", "Speed of Light (c)", 299792458.0, "m/s", "Speed of light in vacuum");
        add("g_const", "Gravitational Constant (G)", 6.67430e-11, "m³/(kg·s²)", "Newtonian constant of gravitation");
        add("g", "Standard Earth Gravity (g)", 9.80665, "m/s²", "Standard acceleration due to gravity");
        add("h", "Planck Constant (h)", 6.62607015e-34, "J·s", "Quantum of electromagnetic action");
        add("hbar", "Reduced Planck Constant (ħ)", 1.054571817e-34, "J·s", "Planck constant divided by 2π");
        add("eps0", "Vacuum Permittivity (ε₀)", 8.8541878128e-12, "F/m", "Electric constant of free space");
        add("mu0", "Vacuum Permeability (μ₀)", 1.25663706212e-6, "N/A²", "Magnetic constant of free space");
        add("sigma", "Stefan-Boltzmann Constant (σ)", 5.670374419e-8, "W/(m²·K⁴)", "Blackbody radiant energy constant");
        add("kboltz", "Boltzmann Constant (k_B)", 1.380649e-23, "J/K", "Relates temperature to particle energy");
        add("q_e", "Elementary Charge (e)", 1.602176634e-19, "C", "Electric charge of a single proton/electron");
        add("m_e", "Electron Mass (m_e)", 9.1093837015e-31, "kg", "Rest mass of an electron");
        add("m_p", "Proton Mass (m_p)", 1.67262192369e-27, "kg", "Rest mass of a proton");
        add("m_n", "Neutron Mass (m_n)", 1.67492749804e-27, "kg", "Rest mass of a neutron");

        // Chemistry & Thermodynamics Constants
        add("navo", "Avogadro's Number (N_A)", 6.02214076e23, "mol⁻¹", "Number of constituent particles in one mole");
        add("r_gas", "Ideal Gas Constant (R)", 8.314462618, "J/(mol·K)", "Molar gas constant");
        add("faraday", "Faraday Constant (F)", 96485.33212, "C/mol", "Magnitude of electric charge per mole of electrons");

        // Astronomy & Astrophysics Constants
        add("msun", "Solar Mass (M_☉)", 1.98847e30, "kg", "Mass of the Sun");
        add("rsun", "Solar Radius (R_☉)", 6.957e8, "m", "Nominal radius of the Sun");
        add("lsun", "Solar Luminosity (L_☉)", 3.828e26, "W", "Nominal luminosity of the Sun");
        add("mearth", "Earth Mass (M_⊕)", 5.9722e24, "kg", "Mass of planet Earth");
        add("rearth", "Earth Radius (R_⊕)", 6.371e6, "m", "Mean volumetric radius of planet Earth");
        add("mmoon", "Moon Mass (M_Moon)", 7.34767309e22, "kg", "Mass of Earth's Moon");
        add("rmoon", "Moon Radius (R_Moon)", 1.7374e6, "m", "Mean radius of the Moon");
        add("d_earth_moon", "Earth-Moon Distance", 3.844e8, "m", "Average distance between Earth and Moon");
        add("au", "Astronomical Unit (AU)", 1.495978707e11, "m", "Average distance from Earth to Sun");
        add("ly", "Light Year (ly)", 9.4607304725808e15, "m", "Distance light travels in one Julian year");
        add("pc", "Parsec (pc)", 3.08567758149137e16, "m", "Distance at which 1 AU subtends an angle of 1 arcsecond");
    }

    private static void add(String sym, String name, double val, String unit, String desc) {
        CONSTANTS.put(sym.toLowerCase(), new ConstantInfo(sym, name, val, unit, desc));
    }

    public static Map<String, ConstantInfo> getAll() {
        return Collections.unmodifiableMap(CONSTANTS);
    }

    public static Double getValue(String symbol) {
        if (symbol == null) return null;
        ConstantInfo info = CONSTANTS.get(symbol.toLowerCase().trim());
        return info != null ? info.value : null;
    }

    public static Map<String, Double> getVariableMap() {
        Map<String, Double> map = new LinkedHashMap<>();
        for (Map.Entry<String, ConstantInfo> e : CONSTANTS.entrySet()) {
            map.put(e.getKey(), e.getValue().value);
        }
        return map;
    }
}
