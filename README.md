
# verisimilar - A synthetic data creator

*Verisimilar (adjective) describes something that appears to be true, real, or highly probable, though it may not actually be true. It originates from Latin verisimilis ("like truth"), emphasizing a strong resemblance to reality, often used to describe convincing, yet fictional or artificial, representations.*

##### PLEASE NOTE:  This repository is a work in progress and developed as a personal project. #####

With my 10+ years experience as a Software Engineer for an email database services company, my expertise is centered around applications that import, enhance, and validate data.  My idea with this project was to create a data generator that creates fake data that looks as real as possible, but isn't.  I also wanted to try to focus on speed/efficiency and use flat data files when possible.

I have many, MANY ideas for future features, including:
* Data Reporting/Charting
* User forms to allow custom weighting by features like Gender, Ethnicity, Region.
* Data generation 'modes', i.e. a mode to generate a file containing of 'toxic' data (i.e. domains known to get you blacklisted)
* Use of public data validation API's to validate generated data.
* Add ZIP4 to zipcode


I am using just US data for now and my aim is to try to stay as diverse as possible.  STAY TUNED.

### SETUP

##### PLEASE NOTE:  Documentation is in progress and currently incomplete.  I'm working to configure for other environments, but right now there are some file paths / properties that hard coded to use my local environment.  If you want to actually pull and run, use at own risk. #####

1) Build the api & core jars using /verisimilar/pom.xml in the parent module:
* verisimilar-api-1.0.0-SNAPSHOT.jar
* verisimilar-api-1.0.0-SNAPSHOT.jar

2) Update the properties file: /api/src/main/resources/api.properties 
* Set the spring.config.import property to a yaml file

3) Start the api (configured to port 8085).  Voila!

```
java verisimilar-api-1.0.0-SNAPSHOT.jar -Dspring.config.name=api.properties
```

3) Call the endpoints!

For example, start here:

##### api/generate/person #####

Request:

```
http://localhost:8085/api/generate/person?api_key={KEY_GOES_HERE}
```

Response:

```
JUDITH ROSE HAILU  6296 RIVERBEND FORDS LOFT 280 KINGFIELD ME 04947  2004-01-01 207-280-1610 JUDITHHAILU@GMAIL.COM  FEMALE
```

##### api/generate/person #####

Request:

```
http://localhost:8085/api/generateBulk?api_key={KEY_GOES_HERE}
```

Response:

```
[
  "CATHERINE RUTH SALABARRIA  5565 MEADOW KEY ROOM 209N HANNASTOWN PA 15635  1986-10-04 267-987-9961 MOUNTAINSYNTH2453@YAHOO.COM  FEMALE",
  "QUENTIN EDWARD NERY  3965 GARDEN MDW SERVANT QUARTERS B STERLING NE 68443  1941-11-29 531-678-3199 QUENTIN.CODES@OUTLOOK.COM  MALE",
  "KALEB WILLIAM KARBAN  286 FRONTIER RIDGE BRKS MOBILE HOME 894S GERALDINE AL 35974  1973-04-08 205-273-1508 KALEBK53@ICLOUD.COM  MALE",
  "JUDITH ROSE HAILU  6296 RIVERBEND FORDS LOFT 280 KINGFIELD ME 04947  2004-01-01 207-280-1610 JUDITHHAILU@GMAIL.COM  FEMALE",
  "JENNIFER MILES VIENNEAU  4574 HERON STRA TERRACE 525A SOUTH MONTROSE PA 18843  1946-01-11 267-461-6150 LIMITLESS.OUTLAW@GMAIL.COM  MALE",
  "AMAYA LOUISE WOOLFORD  6994 JUNIPER RIDGE BAYOU LOWER 427 PRINCETON ID 83857  1946-12-08 986-569-4342 HORIZON286@OUTLOOK.COM  FEMALE",
  "ALEXIS LACE HEVEY  6293 WINDMILL DAM SECTION S GRENOLA KS 67346  1940-01-27 785-903-5205 BINARY.SURFER@GMAIL.COM  FEMALE",
  "CIERRA RUTH ZERILLO  2555 JEFFERSON SHRS STUDIO N NAPLES FL 34110  2004-08-11 813-398-4731 DIVERSTALLION53@QQ.COM  FEMALE",
  "BLAINE MICHAEL ZUREK  46 UNITY RIDGE HARBORS DORM 601 VREDENBURGH AL 36481  1996-04-25 256-698-3920 BLAINEZ859@HOTMAIL.COM  MALE",
  "SARAH KATE JENNIGS  1105 STONE BYP BUILDING 353 KEOTA IA 52248  1926-08-08 712-744-6442 SARAH.JENNIGS@YAHOO.COM  FEMALE"
]
```


## Endpoints

Documentation for accepted parameters TBD.

* /api/generate/person
* /api/generateBulk

* /api/generate/fullname
* /api/generate/username
* /api/generate/postalAddress
* /api/generate/email
* /api/generate/phoneNumber
