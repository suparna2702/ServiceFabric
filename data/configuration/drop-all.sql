DROP PROCEDURE IF EXISTS drop_all_tables;

DELIMITER //
CREATE PROCEDURE drop_all_tables()
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE count INT DEFAULT 0;
  DECLARE table_name VARCHAR(256);
  DECLARE table_names CURSOR FOR
    SELECT t.table_name
    FROM information_schema.tables t
    WHERE database() LIKE 'similan_%' AND t.table_schema = database()
	  AND lower(t.table_name) <> lower('ConfigurationParameter')
    ORDER BY t.table_name;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  SELECT CONCAT('Deleting tables from ', database());

  SET FOREIGN_KEY_CHECKS = 0;

  OPEN table_names;

  drop_loop: LOOP
    FETCH table_names INTO table_name;
    IF done THEN
      LEAVE drop_loop;
    END IF;
    SET @drop_statement = CONCAT('DROP TABLE `', table_name, '`');
    PREPARE prepared_drop_statement FROM @drop_statement;
    EXECUTE prepared_drop_statement;
    DEALLOCATE PREPARE prepared_drop_statement;
    SET count = count + 1;
  END LOOP;

  CLOSE table_names;
  
  SET FOREIGN_KEY_CHECKS = 0;
  
  SELECT CONCAT('Tables deleted from ', database() ,': ', count);
END//
DELIMITER ;

call drop_all_tables();

DROP PROCEDURE IF EXISTS drop_all_tables;