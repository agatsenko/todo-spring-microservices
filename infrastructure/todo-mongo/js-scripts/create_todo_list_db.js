let listDb = createDb(TODO_LIST_DB_DBNAME, TODO_LIST_DB_USERNAME, TODO_LIST_DB_PASSWORD);

createCollection(
  listDb,
  'task_lists',
  {
    validator: {
      $jsonSchema: {
        bsonType: 'object',
        required: ['version', 'userId', 'name', 'tasks'],
        properties: {
          version: {
            bsonType: 'long'
          },
          userId: {
            bsonType: 'binData'
          },
          name: {
            bsonType: 'string'
          },
          tasks: {
            bsonType: 'array',
            oneOf: {
              bsonType: 'object',
              required: ['id', 'description', 'completed', 'order'],
              properties: {
                id: {
                  bsonType: 'binData'
                },
                description: {
                  bsonType: 'string'
                },
                completed: {
                  bsonType: 'bool'
                },
                order: {
                  bsonType: 'int'
                }
              }
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
            userId: 1
          },
          name: 'idx_task_lists_userId',
          unique: false
        }
      ]
    });
  }
);

listDb.task_lists.insertMany([
  {
    _id: uuidToBinary('414df218-a828-40fe-ac83-e084bce8b26e'),
    version: NumberLong("1"),
    userId: uuidToBinary('f7630005-9342-489d-a8ca-aaa25b2ad35d'),
    name: 'list 1',
    tasks: [
      {
        id:uuidToBinary('43d6154c-c145-442d-b701-8bb496a440f4'),
        description: 'task 1.1',
        completed: false,
        order: 1
      },
      {
        id:uuidToBinary('9ef9b8f0-09b8-4a74-8cb0-667b24ab6cc5'),
        description: 'task 1.2',
        completed: true,
        order: 2
      },
      {
        id:uuidToBinary('72297878-a155-426f-b9da-a7e1fe8281d5'),
        description: 'task 1.3',
        completed: false,
        order: 3
      }
    ]
  },
  {
    _id: uuidToBinary('60e6cea2-6927-443c-8471-5dbaac971da6'),
    version: NumberLong("1"),
    userId: uuidToBinary('f7630005-9342-489d-a8ca-aaa25b2ad35d'),
    name: 'empty list',
    tasks: []
  },
  {
    _id: uuidToBinary('565f38d8-d7b6-4d58-bef1-a655387fca47'),
    version: NumberLong("1"),
    userId: uuidToBinary('f7630005-9342-489d-a8ca-aaa25b2ad35d'),
    name: 'list 2',
    tasks: [
      {
        id:uuidToBinary('19a49a63-620c-485d-889e-27ca1ef6080c'),
        description: 'task 2.1',
        completed: false,
        order: 1
      },
      {
        id:uuidToBinary('d138b9a1-a666-44fc-b52e-43f502e825a9'),
        description: 'task 2.2',
        completed: true,
        order: 2
      },
      {
        id:uuidToBinary('e8697b01-35f6-48b6-baee-ea1b32efb9ff'),
        description: 'task 2.3',
        completed: false,
        order: 3
      }
    ]
  },
  {
    _id: uuidToBinary('9a703409-2688-4aec-858b-0600f5fdc6bc'),
    version: NumberLong("3"),
    userId: uuidToBinary('f7630005-9342-489d-a8ca-aaa25b2ad35d'),
    name: 'list 3',
    tasks: [
      {
        id:uuidToBinary('24731d17-b227-4fb1-a437-0b72685bce0b'),
        description: 'task 3.1',
        completed: false,
        order: 1
      },
      {
        id:uuidToBinary('afe7e603-ca20-49c6-a50a-1bf6142b9375'),
        description: 'task 3.2',
        completed: true,
        order: 2
      },
      {
        id:uuidToBinary('bc553d64-2f48-4573-b66d-b72d526f49aa'),
        description: 'task 3.3',
        completed: false,
        order: 3
      }
    ]
  }
]);
