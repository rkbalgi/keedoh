#! /bin/bash
java -cp ./hsqldb.jar org.hsqldb.server.Server --database.1 file:db/mahout --dbname.1 mahout --database.0 file:db/test --dbname.0 test
