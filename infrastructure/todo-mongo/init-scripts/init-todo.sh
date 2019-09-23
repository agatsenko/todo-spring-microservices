mongo -u $MONGO_INITDB_ROOT_USERNAME \
      -p $MONGO_INITDB_ROOT_PASSWORD \
      --eval "var TODO_AUTH_DB_DBNAME='$MONGO_AUTH_DB'; var TODO_AUTH_DB_USERNAME='$MONGO_AUTH_USERNAME'; var TODO_AUTH_DB_PASSWORD='$MONGO_AUTH_PASSWORD';" \
      --verbose \
      admin \
      /mongo-js-scripts/helpers.js \
      /mongo-js-scripts/create_todo_auth_db.js

mongo -u $MONGO_INITDB_ROOT_USERNAME \
      -p $MONGO_INITDB_ROOT_PASSWORD \
      --eval "var TODO_LIST_DB_DBNAME='$MONGO_LIST_DB'; var TODO_LIST_DB_USERNAME='$MONGO_LIST_USERNAME'; var TODO_LIST_DB_PASSWORD='$MONGO_LIST_PASSWORD';" \
      --verbose \
      admin \
      /mongo-js-scripts/helpers.js \
      /mongo-js-scripts/create_todo_list_db.js
