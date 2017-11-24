use TP_CLOUD;
db.createCollection('users');
db.users.createIndex({ lastName: -1});
db.users.createIndex({ position: '2dsphere'});