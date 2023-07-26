# Yoga
The is a web-based application that allows users to manage their yoga practice, schedule, and progress.

# Yoga Backend

## Requirements

### Software
* The following requirement are necessary for this project 
  1. java 8
  2. MySQL

### Test Library
* The following requirement are necessary for this project 
  1. JUnit
  2. JUnit  
  3. Jacoco

## Setting yp your project
1. Set up a MySQL Server .
2. Execute "ressources/sql/script.sql" .
3. Edit the file "back/src/main/resources/application.properties" with your database credentials

## Testing
1. open maven .
2. select clean then test.
3. open **target/site/jacoco/index.html** to view the coverage


# Yoga FontEnd

## Requirements
  1. node js


## Start the project
1. Git clone: > git clone https://github.com/osiris1004/OC-Project-5.git
2. Go inside folder: > cd front
3. Install dependencies: > npm install
4. Launch Front-end: > npm run start;


## Ressources

### Mockoon env

1. Download Mockoon here: https://mockoon.com/download/
2. After installing you could load the environement > ressources/postman/yoga.postman_collection.json
3. directly inside Mockoon  > File > Open environmement
4. For launching the Mockoon server click on play bouton
Mockoon documentation: https://mockoon.com/docs/latest/about/

### MySQL
* By default the admin account is:
      login: yoga@studio.com
      password: test!1234

### Postman collection

For Postman import the collection > ressources/postman/yoga.postman_collection.json
by following the documentation: > https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman


### MySQL

SQL script for creating the schema is available `ressources/sql/script.sql`

By default the admin account is:
- login: yoga@studio.com
- password: test!1234


## Test

#### E2E

1. Launching e2e test: ```npm run e2e```
2. Generate coverage report (you should launch e2e test before): ```npm run e2e:coverage```
3. lunches the report: ```front/coverage/lcov-report/index.html```


#### Unitary test
Launching test: ```npm run test```

