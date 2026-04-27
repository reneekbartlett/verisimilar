//package com.reneekbartlett.verisimilar.api.util;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardCopyOption;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//import java.util.concurrent.CompletionException;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Semaphore;
//import java.util.concurrent.ThreadLocalRandom;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//
//import com.reneekbartlett.verisimilar.core.model.FullName;
////import com.reneekbartlett.verisimilar.util.PostalAddressGenerator.CityStateZip;
//import com.reneekbartlett.verisimilar.core.model.PostalAddress;
//import com.reneekbartlett.verisimilar.core.io.AsyncFileWriter;
//
////import com.reneekbartlett.verisimilar.io.AsyncFileWriter;
////import com.reneekbartlett.verisimilar.io.ResourceMapLoader;
////import com.reneekbartlett.verisimilar.io.ResourceValueLoader;
//
//import com.reneekbartlett.verisimilar.core.model.Ethnicity;
//import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
//import com.reneekbartlett.verisimilar.core.model.Generation;
//import com.reneekbartlett.verisimilar.core.model.GeneratorConfig;
//import com.reneekbartlett.verisimilar.core.model.GeneratorConfig.FieldWeight;
//import com.reneekbartlett.verisimilar.core.model.USState;
//import com.reneekbartlett.verisimilar.core.util.BirthdayGeneratorUtil;
//import com.reneekbartlett.verisimilar.core.util.EmailAddressGeneratorUtil;
//import com.reneekbartlett.verisimilar.core.util.FullNameGeneratorUtil;
//import com.reneekbartlett.verisimilar.core.util.PhoneNumberGeneratorUtil;
//import com.reneekbartlett.verisimilar.core.util.PostalAddressGeneratorUtil;
//
//public class PersonGeneratorUtil {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(PersonGeneratorUtil.class);
//
//    private static final String FIELD_DELIM = "\t";
//    private static final Long MAX_LINES = 10000L;
//
//    private static final String OUTFILE_DIR = "/opt/reneekbartlett/outfiles/";
//
//    public record FakePersonEntry<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>(
//            String firstName,
//            String middleName,
//            String lastName,
//            LocalDate birthday,
//            String addr1,
//            String addr2,
//            String city,
//            String state,
//            String zip,
//            String phoneNumber,
//            String emailAddress
//            ){
//    }
//
////    public record Person(
////            FullName fullName,
////            LocalDate birthday,
////            PostalAddress postalAddress,
////            String phoneNumber,
////            String emailAddress
////            ){
////
////        public String firstName() {
////            return this.fullName.firstName();
////        }
////
////        public String lastName() {
////            return this.fullName.lastName();
////        }
////
////        @Override
////        public String toString() {
////            return new StringBuilder()
////                    .append(this.fullName.toString()).append(FIELD_DELIM)
////                    .append(this.postalAddress.toString()).append(FIELD_DELIM)
////                    .append(this.birthday).append(FIELD_DELIM)
////                    .append(this.phoneNumber).append(FIELD_DELIM)
////                    .append(this.emailAddress)
////                    .toString();
////        }
////    }
//
//    // TODO:  Add parameters
//    public static String get() {
//        return getRecord().toString();
//    }
//
//    private record WeightedTotals<T1, T2>(T1 cumulative, T2 totalWeight) {}
//
//    private static WeightedTotals<double[], Double> getTotalWeight(FieldWeight[] fields) {
//        double total = 0.0;
//        int i2 = 0;
//        double[] cumulative = new double[fields.length];
//        for (FieldWeight fw : fields) {
//            total += fw.weight();
//            cumulative[i2++] = total;
//        }
//        return new WeightedTotals<>(cumulative, total);
//    }
//
//    // TODO: 3/21
//    //public static Person<FullName<String, String, String>, LocalDate, PostalAddress<String, String, String, String, String>, String, String> getRecord() {
//    public static FakePersonEntry<String,String,String,LocalDate,String,String,String,String,String,String,String> getRecord() {
//        // TODO:  Use defaults?
//        USState[] f1 = new USState[] { USState.MA, USState.RI, USState.CT };
//        GenderIdentity[] f2 = new GenderIdentity[] { GenderIdentity.FEMALE, GenderIdentity.MALE };
//        Generation[] f3 = new Generation[] { Generation.MILLENNIAL };
//        Ethnicity[] f4 = new Ethnicity[] { Ethnicity.ASIAN, Ethnicity.HISPANIC_OR_LATINO };
//        int[] f5 = new int[] { 1980, 1990 };
//
//        GeneratorConfig cfg = GeneratorConfig.builder()
//                //.states(f1)
//                //.genders(f2)
//                //.generations(f3)
//                //.ethnicities(f4)
//                .build();
//
//        // Generate the birthday first.
//        LocalDate birthday;
//        if(cfg.getYearMin() > 0) {
//            int year1 = cfg.getYearMin(); //Integer.valueOf(cfg.getYearMin());
//            int year2 = cfg.getYearMax(); //Integer.valueOf(cfg.getYearMax());
//            // TODO: support filter for year2
//            birthday = BirthdayGeneratorUtil.get(year1, year2);
//        } else {
//            birthday = BirthdayGeneratorUtil.get();
//        }
//
//        // Then the Postal.
//        PostalAddress postalAddress;
//        if(cfg.hasStateFilter()) {
//            // TODO: Pick random from filtered states
//            //FieldWeight[] states = cfg.getStates();
//            FieldWeight[] states = null;
//
//            var t = getTotalWeight(states);
//            double stateRand = ThreadLocalRandom.current().nextDouble() * t.totalWeight();
//            int stateIdx = Arrays.binarySearch(t.cumulative(), stateRand);
//            LOGGER.debug("stateRand=" + stateRand + ", stateIndx=" + stateIdx + ", t=" + t);
//            if (stateIdx < 0) stateIdx = -stateIdx - 1;
//            String randState = states[stateIdx].name();
//            postalAddress = PostalAddressGeneratorUtil.getRecord(randState);
//        } else {
//            postalAddress = PostalAddressGeneratorUtil.getRecord("");
//        }
//
//        String state = postalAddress.state();
//
//        //
//        // TODO:  These 3 could probably be async
//        //
//
//        // TODO: Add Birthday and Postal to weight name
//        FullName fullName = FullNameGeneratorUtil.getRecord(cfg);
//
//        String phoneNumber = PhoneNumberGeneratorUtil.get(state);
//
//        String emailAddress = EmailAddressGeneratorUtil.get(fullName.firstName(), fullName.middleName(), fullName.lastName(), birthday);
//
//        //return new Person<>(fullName, birthday, postalAddress, phoneNumber, emailAddress);
//        return new FakePersonEntry<>(fullName.firstName(), 
//                fullName.middleName(),
//                fullName.lastName(),
//                birthday,
//                postalAddress.address1().toUpperCase(),
//                postalAddress.address2().toUpperCase(),
//                postalAddress.city().toUpperCase(),
//                postalAddress.state(),
//                postalAddress.zip(),
//                phoneNumber,
//                emailAddress);
//    }
//
//    // TODO:  Add option to narrow down types of data generated (i.e. country, age, etc.)
//    // TODO:  Add outdir to parameter?
//    public static String[] generateBulk(long count) throws Exception {
//        if(count > MAX_LINES) {
//            count = MAX_LINES;
//            LOGGER.info("Over max!  Limiting to " + MAX_LINES + " lines.");
//        }
//
//        int fileCount;
//        int linesPerFile;
//        if(count >= Integer.MAX_VALUE) {
//            fileCount = 2;
//            linesPerFile = Integer.MAX_VALUE;
//        } else {
//            fileCount = 1;
//            linesPerFile = (int)count;
//        }
//
//        String[] outFiles = new String[fileCount];
//        Semaphore semaphore = new Semaphore(10);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyddMM_HHmmssSSS");
//        String ts = LocalDateTime.now().format(formatter);
//
//        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
//            for(int x = 0; x < fileCount; x++) {
//                final int fileIndex = x;
//                Path filePath = Path.of(OUTFILE_DIR, "people_"+count+"_"+ts+"_"+fileIndex+".csv");
//
//                // rename file, don't overwrite/append
//                if (Files.exists(filePath)) {
//                    rotateFile(filePath);
//                }
//
//                //var people = new Person[linesPerFile];
//                var people = new FakePersonEntry[linesPerFile];
//                for (int i = 0; i < linesPerFile; i++) {
//                    people[i] = getRecord();
//                    LOGGER.info(people[i].toString());
//                }
//
//                semaphore.acquire();
//
//                // TODO:  HEADER?
//                executor.submit(() -> {
//                    try {
//                        //AsyncFileWriter.writeObjectsAsync(people, filePath).join();
//                        AsyncFileWriter.writeCsvAsync(people, filePath).join();
//                        LOGGER.info("Write complete! " + filePath);
//                    } catch (CompletionException | IOException ex) {
//                        LOGGER.error("Async write failed for " + filePath, ex);
//                    } finally {
//                        semaphore.release();
//                    }
//                    return null;
//                });
//
//                outFiles[x] = filePath.toString();
//            }
//        }
//
//        return outFiles;
//    }
//
//    private static Optional<String> getFileExtensionFromName(String fileName) {
//        return Optional.ofNullable(fileName)
//                .filter(f -> f.contains("."))
//                .map(f -> f.substring(fileName.lastIndexOf(".") + 1));
//    }
//
//    private static void rotateFile(Path filePath) throws IOException {
//        String fileName = filePath.getFileName().toString();
//        String ext = getFileExtensionFromName(fileName).orElse("");
//        String rotatedName = fileName + "_" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())
//                + (ext.isEmpty() ? "" : "." + ext);
//        Files.move(filePath, filePath.resolveSibling(rotatedName), StandardCopyOption.REPLACE_EXISTING);
//    }
//
////    private static Path writeFile(Path filePath, List<String> emails, Map<String, Integer> fields, char delimiter, boolean header) throws Exception {
////        validateFields(fields);
////        int fieldCount = fields.size();
////        String[] orderedFieldNames = new String[fieldCount];
////        fields.forEach((name, pos) -> orderedFieldNames[pos - 1] = name);
////        try (BufferedWriter writer = Files.newBufferedWriter(filePath); PrintWriter out = new PrintWriter(writer)) {
////            // Write header
////            if (header) {
////                writeRecord(out, delimiter, orderedFieldNames);
////            }
////            // Generate rows
////            for (String email : emails) {
////                String rand = Long.toString(System.currentTimeMillis(), Character.MAX_RADIX);
////                String[] row = new String[fieldCount];
////                for (int i = 0; i < fieldCount; i++) {
////                    String field = orderedFieldNames[i];
////                    row[i] = switch (field) {
////                        case "email", "md5_email" -> email;
////                        default -> "test-" + field + rand;
////                    };
////                }
////                writeRecord(out, delimiter, row);
////            }
////        }
////        return filePath;
////    }
////
////    private static void validateFields(Map<String, Integer> fields) throws Exception{
////        if(fields.isEmpty()) {
////            throw new Exception("no fields");
////        }
////    }
////
////    private static void writeRecord(PrintWriter writer, char delimiter, Object[] values) {
////        for (int i = 0; i < values.length; i++) {
////            if (i > 0) {
////                writer.write(delimiter);
////            }
////            Object value = values[i];
////            String valueStr = value == null ? "" : value.toString();
////            writer.write(valueStr);
////        }
////        writer.write('\n');
////        writer.flush();
////    }
//}
