SELECT SUM(amount) AS total_sales
FROM orders
WHERE order_date BETWEEN '2024-03-01' AND '2024-03-31';

SELECT customer, SUM(amount) AS total_spent
FROM orders
GROUP BY customer
ORDER BY total_spent DESC
LIMIT 1;

SELECT AVG(amount) AS avg_order_value
FROM orders
WHERE order_date BETWEEN '2024-01-01' AND '2024-03-31';