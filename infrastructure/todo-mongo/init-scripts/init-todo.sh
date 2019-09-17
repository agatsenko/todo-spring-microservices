mongo -u $MONGO_INITDB_ROOT_USERNAME \
      -p $MONGO_INITDB_ROOT_PASSWORD \
      --eval "var TODO_USERS_DB_DBNAME='$MONGO_AUTH_DB'; var TODO_USERS_DB_USERNAME='$MONGO_AUTH_USERNAME'; var TODO_USERS_DB_PASSWORD='$MONGO_AUTH_PASSWROD';" \
      --verbose \
      admin \
      /mongo-js-scripts/helpers.js \
      /mongo-js-scripts/create_todo_users_db.js
