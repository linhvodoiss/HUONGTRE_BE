const mysql = require('mysql2/promise');

async function test() {
  const connection = await mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '12345',
    database: 'HuongTreSystem',
  });

  try {
    console.log('--- PRODUCTOPTIONGROUP TABLE ---');
    const [pog] = await connection.query('SELECT * FROM productoptiongroup');
    console.log(pog);

    console.log('--- OPTIONGROUP TABLE ---');
    const [og] = await connection.query('SELECT * FROM optiongroup');
    console.log(og);

  } catch (err) {
    console.error(err);
  } finally {
    await connection.end();
  }
}

test();
