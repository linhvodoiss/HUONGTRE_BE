const mysql = require('mysql2/promise');

async function test() {
  const connection = await mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '12345',
    database: 'HuongTreSystem',
  });

  try {
    console.log('--- CATEGORY TABLE ---');
    const [categories] = await connection.query('SELECT * FROM category');
    console.log(categories);

    console.log('--- PRODUCT TABLE ---');
    const [products] = await connection.query('SELECT * FROM product');
    console.log(products);

    console.log('--- PRODUCT COLUMNS ---');
    const [columns] = await connection.query('SHOW COLUMNS FROM product');
    console.log(columns);

  } catch (err) {
    console.error(err);
  } finally {
    await connection.end();
  }
}

test();
