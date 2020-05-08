INSERT INTO customer(customer_name, customer_address) 
VALUES 
("Tom Ford", "123 Dublin Road"),
("Clara Oswald", "123 Dublin Road"),
("John Smith", "123 Dublin Road"),
("Amy Pond", "123 Dublin Road"),
("Rory Williams", "123 Dublin Road"),
("Rose Tyler", "123 Dublin Road"),
("Martha Jones", "123 Dublin Road"),
("Mickey Smith", "123 Dublin Road"),
("Donna Noble", "123 Dublin Road"),
("Jack Hartness", "123 Dublin Road");


INSERT INTO vehicle(vehicle_registration, vehicle_type) 
VALUES 
("152DL345", "Car"),
("161C3457", "Van"),
("181MH3456", "Truck"),
("181MH3458", "Van"),
("181MH3459", "Car"),
("181MH3461", "Truck"),
("191LH1111", "Car"),
("191LH1112", "Car"),
("191LH1113", "Van"),
("191LH1114", "Truck"),
("192D33457", "Van"),
("201CN3456", "Car"),
("201CN3457", "Car"),
("201LH3025", "Car"),
("201LH304", "Truck"),
("201LH305", "Truck"),
("201LH306", "Van"),
("201LH3064", "Truck"),
("201LH307", "Car"),
("201LH3076", "Car"),
("201LH308", "Van"),
("201LH3083", "Truck"),
("201LH309", "Car"),
("201LH310", "Truck"),
("201LH311", "Van"),
("201LH312", "Car");


INSERT INTO customer_vehicle VALUES 
(
(SELECT customer_id FROM customer WHERE customer_name = "Tom Ford"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "152DL345")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Tom Ford"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "161C3457")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Tom Ford"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "181MH3456")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Clara Oswald"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "181MH3458")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Clara Oswald"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "181MH3459")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "John Smith"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "181MH3461")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "John Smith"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "191LH1111")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "John Smith"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "191LH1112")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Amy Pond"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "191LH1113")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Amy Pond"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "191LH1114")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Rory Williams"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "192D33457")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Rory Williams"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "201CN3456")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Rory Williams"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "201CN3457")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Rose Tyler"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "201LH3025")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Rose Tyler"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "201LH304")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Martha Jones"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "201LH305")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Martha Jones"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "201LH306")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Martha Jones"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "201LH3064")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Mickey Smith"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "201LH307")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Mickey Smith"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "201LH3076")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Donna Noble"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "201LH308")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Donna Noble"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "201LH3083")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Jack Hartness"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "201LH309")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Jack Hartness"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "201LH310")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Jack Hartness"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "201LH311")
),
(
(SELECT customer_id FROM customer WHERE customer_name = "Jack Hartness"),
(SELECT vehicle_id FROM vehicle WHERE vehicle_registration = "201LH312")
);


INSERT INTO vehicle_type_cost VALUES 
("Car", 3.00),
("Van", 6.00),
("Truck", 9.00);