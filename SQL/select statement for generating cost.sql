SELECT SUM(cost) AS bill FROM toll_event 
NATURAL JOIN customer_vehicle 
NATURAL JOIN customer 
NATURAL JOIN vehicle 
NATURAL JOIN vehicle_type_cost 
WHERE customer_name = "Tom Ford" 
AND timestamp BETWEEN SUBDATE(CURDATE(), INTERVAL 1 MONTH) 
AND NOW();