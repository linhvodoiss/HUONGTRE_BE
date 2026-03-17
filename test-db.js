const mysql = require('mysql2/promise');
require('dotenv').config();

async function test() {
  const connection = await mysql.createConnection({
    host: process.env.DB_HOST || 'localhost',
    user: process.env.DB_USERNAME || 'root',
    password: process.env.DB_PASSWORD || '12345',
    database: process.env.DB_NAME || 'HuongTreSystem',
  });

  try {
    const [tables] = await connection.query('SHOW TABLES');
    console.log('Tables:', tables);

    const [categories] = await connection.query('SELECT * FROM category LIMIT 5');
    console.log('Categories:', categories);

    const [products] = await connection.query('SELECT * FROM product LIMIT 5');
    console.log('Products:', products);

    if (products.length > 0) {
        console.log('Product columns:', Object.keys(products[0]));
    }

  } catch (err) {
    console.error(err);
  } finally {
    await connection.end();
  }
}

test();
