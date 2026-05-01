//package com.reneekbartlett.verisimilar.core.model;
//
//import java.util.EnumSet;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//@SuppressWarnings("unused")
//public class GeneratorConfig {
//    private GeneratorMode generatorMode;
//    private int minYear;
//    private int maxYear;
//    private int[] years;
//    private Set<Generation> generations;
//
//    private Set<GenderIdentity> genders;
//
//    private EnumSet<USState> states;
//    private EnumSet<Ethnicity> ethnicities;
//
//    private Set<FieldWeight<USState>> statesWithWeights;
//    private Set<FieldWeight<Generation>> generationWeights;
//    private Set<FieldWeight<Ethnicity>> ethnicityWeights;
//    private Set<FieldWeight<GenderIdentity>> genderWeights;
//
//    //public Optional<Boolean> getYears() { return Optional.ofNullable(isHeadOfHousehold); }
//
//    @Override
//    public String toString() {
//        return new StringBuilder()
//            //.append(String.join(",", years)).append(" ")
//            .append(this.genders).append(" ")
//            .append(this.states).append(" ")
//            .append(this.ethnicities).append(" ")
//            .toString().toUpperCase();
//    }
//
//    // use builder
//    private GeneratorConfig() {}
//
//    public static GeneratorConfig getDefault() {
//        return GeneratorConfig.builder()
//                .generatorMode(GeneratorMode.DEFAULT)
//                .build();
//    }
//
//    private GeneratorConfig(Builder b){
//        this.generatorMode = b.generatorMode;
//        this.years = b.years;
//        this.genders = b.genders;
//        this.states = b.states;
//        this.ethnicities = b.ethnicities;
//        this.generations = b.generations;
//        
//        // domainTypes
//        // 
//
//        if(years != null) {
//            int min = 0;
//            int max = 0;
//            for(int y : years) {
//                min = Math.min(min, y);
//                max = Math.max(max, y);
//            }
//            this.minYear = min;
//            this.maxYear = max;
//        }
//
//        if(generations != null) {
//            Set<FieldWeight<Generation>> generationWeights = new HashSet<>(generations.size());
//            for(Generation g : generations) {
//                //generationWeights[i] = new FieldWeight<Generation>(g.name(), Generation.class, g, g.getWeight());
//                generationWeights.add(new FieldWeight<Generation>(g.name(), Generation.class, g, g.getWeight()));
//            }
//            this.generationWeights = generationWeights;
//        }
//
//        //FieldWeight[] stateWeights = {
//        //        new FieldWeight<USState>("MA", USState.class, USState.MA, 0.5)
//        //};
//        if(states != null) {
//            Set<FieldWeight<USState>> stateWeights = new HashSet<>(states.size());
//            int i = 0;
//            for(USState s : states) {
//                //stateWeights[i] = new FieldWeight<USState>(s.name(), USState.class, s, s.getWeight());
//                stateWeights.add(new FieldWeight<USState>(s.name(), USState.class, s, s.getWeight()));
//                i++;
//            }
//            this.statesWithWeights = stateWeights;
//        }
//
//        //FieldWeight[] defaultGenderWeights = {
//        //        new FieldWeight<GenderIdentity>("F", GenderIdentity.class, GenderIdentity.FEMALE, 0.5),
//        //        new FieldWeight<GenderIdentity>("M", GenderIdentity.class, GenderIdentity.MALE, 0.5),
//        //};
//        if(genders != null) {
//            Set<FieldWeight<GenderIdentity>> gWeights = new HashSet<>(genders.size());
//            int i = 0;
//            for(GenderIdentity g : genders) {
//                //gWeights[i] = new FieldWeight<GenderIdentity>(g.name(), GenderIdentity.class, g, g.getWeight());
//                gWeights.add(new FieldWeight<GenderIdentity>(g.name(), GenderIdentity.class, g, g.getWeight()));
//                i++;
//            }
//            this.genderWeights = gWeights;
//        }
//        
//        if(ethnicities != null) {
//            //FieldWeight<Ethnicity>[] eWeights = new FieldWeight[ethnicities.length];
//            Set<FieldWeight<Ethnicity>> eWeights = new HashSet<>(ethnicities.size());
//            int i = 0;
//            for(Ethnicity e : ethnicities) {
//                //eWeights[i] = new FieldWeight<Ethnicity>(e.name(), Ethnicity.class, e, e.getWeight());
//                eWeights.add(new FieldWeight<Ethnicity>(e.name(), Ethnicity.class, e, e.getWeight()));
//                i++;
//            }
//            this.ethnicityWeights = eWeights;
//        }
//    }
//
//    public GeneratorMode getConfigMode() {
//        return this.generatorMode;
//    }
//
//    public int getYearMin() {
//        return minYear;
//    }
//
//    public int getYearMax() {
//        return maxYear;
//    }
//
//    public boolean hasYearFilter() {
//        return false;
//    }
//
//    public boolean hasStateFilter() {
//        return states != null ? true : false;
//    }
//
//    public boolean hasGenerationFilter() {
//        return generations != null ? true : false;
//    }
//
//    public Set<FieldWeight<Generation>> getGenerations() {
//        return this.generationWeights;
//    }
//
//    public Set<FieldWeight<USState>> getStates() {
//        return this.statesWithWeights;
//    }
//
//    public Set<FieldWeight<GenderIdentity>> getGenders() {
//        return this.genderWeights;
//    }
//
//    public Set<FieldWeight<Ethnicity>> getEthnicities() {
//        return this.ethnicityWeights;
//    }
//
//    private Set<FieldWeight<Generation>> getGenerationWeights() {
//        //int options = Generation.values().length;
//        Set<FieldWeight<Generation>> generationWeights = Set.of(
//                new FieldWeight<Generation>("MILLENNIAL", Generation.class, Generation.MILLENNIAL, 0.5)
//        );
//        return generationWeights;
//    }
//
//    public static Builder builder() { return new Builder(); }
//
//    public record FieldWeight<T>(String name, Class<?> targetType, T value, double weight){
//        public FieldWeight {
//            if (weight < 0.0) throw new IllegalArgumentException("Weight cannot be negative");
//        }
//    }
//
//    public static final class Builder {
//        private GeneratorMode generatorMode;
//        private Set<Generation> generations;
//        private int[] years;
//        
//        private Set<GenderIdentity> genders;
//        private Set<USState> states;
//        private Set<Ethnicity> ethnicities;
//
//        private Builder() {}
//
//        public Builder generatorMode(GeneratorMode generatorMode) { this.generatorMode = generatorMode; return this; }
//        public Builder years(int[] years) { this.years = years; return this; }
//        public Builder genders(Set<GenderIdentity> genders) { this.genders = genders; return this; }
//        public Builder states(Set<USState> states) { this.states = states; return this; }
//        public Builder ethnicities(Set<Ethnicity> ethnicities) { this.ethnicities = ethnicities; return this; }
//        public Builder generations(Set<Generation> generations) { this.generations = generations; return this; }
//
//        public GeneratorConfig build() {
//            Map<String, String> fieldWeight = new HashMap<>();
//
//            if(states == null) {
//                //this.states = DEFAULT_STATES;
//            }
//
//            if(genders == null) {
//                genders = null;
//            }
//
//            return new GeneratorConfig(this);
//        }
//    }
//}
