load('/docker-entrypoint-initdb.d/common/helpers.js');

let usersDb = createDb('todo_users', 'todo_users', 'todo_users');

createCollection(
  usersDb,
  'users',
  {
    validator: {
      $jsonSchema: {
        bsonType: 'object',
        properties: {
          name: {
            bsonType: 'string'
          },
          email: {
            bsonType: 'string'
          },
          passwordHash: {
            bsonType: 'string'
          }
        },
        required: ['name', 'email', 'passwordHash']
      }
    },
    validationLevel: 'strict',
    validationAction: 'error'
  },
  function(db, collName) {
    db.runCommand({
      createIndexes: collName,
      indexes: [
        {
          key: {
            name: 1
          },
          name: 'udx_users_name',
          unique: true
        },
        {
          key: {
            email: 1
          },
          name: 'udx_users_email',
          unique: true
        }
      ]
    });
  }
);

usersDb.users.insertMany([
  {
    _id: uuidToBinary('f7630005-9342-489d-a8ca-aaa25b2ad35d'),
    name: 'tester',
    email: 'f76300059342489da8caaaa25b2ad35d@f76300059342489da8caaaa25b2ad35d.mail',
    passwordHash: '$2a$10$SetVaJ9eB5wXcy71.6qMaeMGwuTeFMX5vAvXbDYX/zJol//k5hm9K'
  }
]);
