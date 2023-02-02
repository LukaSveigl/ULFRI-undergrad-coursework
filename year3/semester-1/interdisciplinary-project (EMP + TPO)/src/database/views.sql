-- --------------------------------------------------------------
-- returns coordinates and country of all parcel centers
-- --------------------------------------------------------------
CREATE VIEW parcel_center_locations
AS
    SELECT b.id AS branch_id, c.latitude AS latitude, c.longitude AS longitude, c.country_code as country
    FROM branch b
    INNER JOIN street s on b.street_name = s.street_name and b.street_number = s.street_number and b.city_code = s.city_code and b.city_name = s.city_name and b.country_code = s.country_code
    INNER JOIN city c on s.city_code = c.code and s.country_code = c.country_code and s.city_name = c.name
    INNER JOIN branch_type bt on b.branch_type_id = bt.id
    WHERE bt.id=1;
-- --------------------------------------------------------------
--
-- --------------------------------------------------------------