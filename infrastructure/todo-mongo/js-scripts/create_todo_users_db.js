//load('/docker-entrypoint-initdb.d/common/helpers.js');

//let usersDb = createDb('todo_users', 'todo_users', 'todo_users');
let usersDb = createDb(TODO_USERS_DB_DBNAME, TODO_USERS_DB_USERNAME, TODO_USERS_DB_PASSWORD);

createCollection(
  usersDb,
  'users',
  {
    validator: {
      $jsonSchema: {
        bsonType: 'object',
        properties: {
          username: {
            bsonType: 'string'
          },
          password: {
            bsonType: 'string'
          },
          email: {
            bsonType: 'string'
          },
          enabled: {
            bsonType: 'bool'
          },
          roles: {
            bsonType: 'array'
          }
        },
        required: ['username', 'password', 'email', 'enabled', 'roles']
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
            username: 1
          },
          name: 'udx_users_username',
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
    username: 'test_user',
    password: '$2a$10$p6a4k8w38ZQ3j3pEGCFrjubwgPE/14HyfTLDhbb43IAWfGJgMISBq',
    email: 'test_user@mail.net',
    enabled: true,
    roles: ['USER']
  },
  {
    _id: uuidToBinary('e0c43a52-72e5-4176-87e2-b28a55748434'),
    username: 'test_usersmanager',
    password: '$2a$10$U64a/W7trFqk9.T0v9TkcOtEbtprWZ8mTbXQddIPvtf6xkpqVP.ym',
    email: 'test_usersmanager@mail.net',
    enabled: true,
    roles: ['USERS_MANAGER']
  },
  {
    _id: uuidToBinary('c69af56b-9d10-4a48-8f0d-b2991e459dcd'),
    username: 'test_user_usersmanager',
    password: '$2a$10$Ixhl4i1PkRIuYgwWZgfniOOl.ppM7rwjNQwcyPVYiDk891SV6.w/6',
    email: 'test_user_usersmanager@mail.net',
    enabled: true,
    roles: ['USER', 'USERS_MANAGER']
  }
]);
