SELECT customer_name FROM toll_event 
NATURAL JOIN customer_vehicle 
NATURAL JOIN customer 
NATURAL JOIN vehicle 
WHERE billing_status = 'Incomplete';


SELECT customer_name FROM customer 
WHERE customer_name = ?;


SELECT SUM(cost) AS bill FROM toll_event 
NATURAL JOIN customer_vehicle 
NATURAL JOIN customer 
NATURAL JOIN vehicle 
NATURAL JOIN vehicle_type_cost 
WHERE customer_name = ? 
AND timestamp BETWEEN SUBDATE(CURDATE(), INTERVAL 3 MONTH) 
AND NOW();


UPDATE toll_event 
SET billing_status = ? 
WHERE vehicle_registration IN 
(SELECT vehicle_registration FROM toll_event 
NATURAL JOIN customer_vehicle 
NATURAL JOIN customer 
NATURAL JOIN vehicle 
WHERE customer_name = ?);