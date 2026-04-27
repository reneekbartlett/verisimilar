# Directory Structure (Final Form)

verisimilar/
│
├── pom.xml                          # Parent POM (manages modules + versions)
│
├── core/                             # Pure Java synthetic data engine
│   ├── pom.xml
│   └── src/
│       ├── main/java/
│       │   └── com/reneekbartlett/verisimilar/core/
│       │       ├── datasets/          # Static maps + loaders
│       │       │   ├── FirstNameData.java
│       │       │   ├── LastNameData.java
│       │       │   ├── StateData.java
│       │       │   └── ...
│       │       │
│       │       ├── model/             # Domain models
│       │       │   ├── Person.java
│       │       │   ├── Address.java
│       │       │   └── ...
│       │       │
│       │       ├── selector/          # Weighted selectors + registry
│       │       │   ├── WeightedSelector.java
│       │       │   ├── WeightedSelectorImpl.java
│       │       │   ├── WeightedSelectorFactory.java
│       │       │   └── SelectorRegistry.java
│       │       │
│       │       ├── pipeline/          # Constraints + filtering
│       │       │   ├── Constraint.java
│       │       │   ├── ConstraintSet.java
│       │       │   ├── FilterPipeline.java
│       │       │   └── ...
│       │       │
│       │       ├── generator/         # High-level generators
│       │       │   ├── PersonGenerator.java
│       │       │   ├── AddressGenerator.java
│       │       │   └── ...
│       │       │
│       │       └── util/              # CSV parsing, resource loading
│       │           ├── ResourceLoaderUtil.java
│       │           └── CsvParser.java
│       │
│       └── main/resources/
│           └── datasets/
│               ├── first_names.csv
│               ├── last_names.csv
│               ├── states.csv
│               └── ...
│
│
└── api/                               # Spring Boot web service
    ├── pom.xml
    └── src/
        ├── main/java/
        │   └── com/reneekbartlett/verisimilar/api/
        │       ├── SyntheticApiApplication.java
        │       │
        │       ├── controller/        # REST endpoints
        │       │   ├── PersonController.java
        │       │   ├── AddressController.java
        │       │   └── ...
        │       │
        │       ├── dto/               # Request/response DTOs
        │       │   ├── PersonRequest.java
        │       │   ├── PersonResponse.java
        │       │   └── ...
        │       │
        │       ├── config/            # Spring config + bean wiring
        │       │   ├── SelectorConfig.java
        │       │   ├── DatasetConfig.java
        │       │   └── ...
        │       │
        │       ├── service/           # Adapters between API + core engine
        │       │   ├── PersonService.java
        │       │   ├── AddressService.java
        │       │   └── ...
        │       │
        │       ├── validation/        # Custom validators
        │       │   ├── YearRangeValidator.java
        │       │   ├── StateValidator.java
        │       │   └── ...
        │       │
        │       └── exception/         # API error handling
        │           ├── ApiExceptionHandler.java
        │           └── InvalidConstraintException.java
        │
        └── main/resources/
            ├── application.yaml       # Spring Boot config
            └── logging.yaml