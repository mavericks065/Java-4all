##################
# Useful commands:
##################

mongo
show dbs
use mobileservicemdb
show collections
db.announces.find().pretty()
db.MY_COLLECTION.drop()

########################
# On Eclipse or IntelliJ
########################

Parameters for launching the server : 
main class : org.vertx.java.platform.impl.cli.Starter
arguments : runmod com.sss~mobileserviceserver~0.0.1 -conf /Users/mba/Business/workspace/mobileserviceserver/src/main/resources/mod.json

or on windows
arguments : runmod com.sss~mobileserviceserver~0.0.1 -conf D:\GitHub\mobileserviceserver\src\main\resources\mod.json

###########
# FOR PROD:
###########

IP: 54.77.19.75
DNS: ec2-54-77-19-75.eu-west-1.compute.amazonaws.com

To deploy : 
mvn assembly:single -DdescriptorId=jar-with-dependencies

#########
# Vert.x:
#########

To run:
vertx run com.sss.VertxServer -cp mobileserviceserver-0.0.1-jar-with-dependencies.jar
To stop:
ps aux | grep vertx
kill -9 *ps id*


##############################################################################
# MongoDB: (http://docs.mongodb.org/manual/tutorial/manage-mongodb-processes/)
##############################################################################

To run:
mongod --dbpath ~/data/db
or as a daemon 
mongod --dbpath ~/data/db --fork --logpath ~/mongodb.log

To stop:
mongod --dbpath ~/data/db --shutdown

# Seed database
Run the script initSystem.sh

# To test if server is running:
http://54.77.19.75:8888/login/users/email=paulo@test.com&password=test

# Old Way of initializing :
important : import the file users.json to have a collection :
mongoimport -d mobileservicemdb -c users < users.json
mongoimport -d mobileservicemdb -c announces < announces.json
mongoimport -d mobileservicemdb -c categories < categories.json
mongoimport -d mobileservicemdb -c tracking < tracking.json

db.users.ensureIndex({ email : 1}, {unique : true })

http://docs.mongodb.org/manual/tutorial/build-a-2d-index/
http://docs.mongodb.org/manual/core/geospatial-indexes/