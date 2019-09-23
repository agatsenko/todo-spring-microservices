let authDb = createDb(TODO_AUTH_DB_DBNAME, TODO_AUTH_DB_USERNAME, TODO_AUTH_DB_PASSWORD);

createCollection(
  authDb,
  'users',
  {
    validator: {
      $jsonSchema: {
        bsonType: 'object',
        required: ['version', 'username', 'password', 'email', 'enabled', 'roles'],
        properties: {
          version: {
            bsonType: 'long'
          },
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
            bsonType: 'array',
            oneOf: {
              bsonType: 'string'
            }
          }
        }
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

createCollection(
  authDb,
  'oauth2_tokens',
  {
    validator: {
      $jsonSchema: {
        bsonType: 'object',
        required: ['authentication', 'clientId', 'scope'],
        properties: {
          authentication: {
            bsonType: 'binData'
          },
          clientId: {
            bsonType: 'string'
          },
          userId: {
            bsonType: 'binData'
          },
          username: {
            bsonType: 'string'
          },
          accessTokenId: {
            bsonType: 'binData'
          },
          accessTokenValue: {
            bsonType: 'string'
          },
          accessTokenExpiration: {
            bsonType: 'date'
          },
          refreshTokenId: {
            bsonType: 'binData'
          },
          refreshTokenValue: {
            bsonType: 'string'
          },
          refreshTokenExpiration: {
            bsonType: 'date'
          },
          scope: {
            bsonType: 'array',
            oneOf: {
              bsonType: 'string'
            }
          }
        }
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
            clientId: 1
          },
          name: 'idx_oauth2_tokens_clientId',
          unique: false
        },
        {
          key: {
            userId: 1
          },
          name: 'idx_oauth2_tokens_userId',
          unique: false
        },
        {
          key: {
            username: 1
          },
          name: 'idx_oauth2_tokens_username',
          unique: false
        },
        {
          key: {
            accessTokenId: 1
          },
          name: 'udx_oauth2_tokens_accessTokenId',
          unique: true
        },
        {
          key: {
            refreshTokenId: 1
          },
          name: 'udx_oauth2_tokens_refreshTokenId',
          unique: true
        }
      ]
    });
  }
);

authDb.users.insertMany([
  {
    _id: uuidToBinary('f7630005-9342-489d-a8ca-aaa25b2ad35d'),
    version: NumberLong("1"),
    username: 'test_user',
    password: '$2a$10$p6a4k8w38ZQ3j3pEGCFrjubwgPE/14HyfTLDhbb43IAWfGJgMISBq',
    email: 'test_user@mail.net',
    enabled: true,
    roles: ['USER']
  },
  {
    _id: uuidToBinary('e0c43a52-72e5-4176-87e2-b28a55748434'),
    version: NumberLong("1"),
    username: 'test_usersmanager',
    password: '$2a$10$U64a/W7trFqk9.T0v9TkcOtEbtprWZ8mTbXQddIPvtf6xkpqVP.ym',
    email: 'test_usersmanager@mail.net',
    enabled: true,
    roles: ['USERS_MANAGER']
  },
  {
    _id: uuidToBinary('c69af56b-9d10-4a48-8f0d-b2991e459dcd'),
    version: NumberLong("1"),
    username: 'test_user_usersmanager',
    password: '$2a$10$Ixhl4i1PkRIuYgwWZgfniOOl.ppM7rwjNQwcyPVYiDk891SV6.w/6',
    email: 'test_user_usersmanager@mail.net',
    enabled: true,
    roles: ['USER', 'USERS_MANAGER']
  }
]);
