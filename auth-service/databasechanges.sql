-- Создание таблицы fire_real_time_report
CREATE TABLE fire_real_time_report (
                                       id SERIAL PRIMARY KEY,
                                       start_date DATE,
                                       end_date DATE,
                                       region VARCHAR(255),
                                       fire_area DECIMAL(10, 2),
                                       fire_cause TEXT,
                                       latitude DECIMAL(8, 6),
                                       longitude DECIMAL(9, 6),
                                       fire_real_time_economic_damage_report_id INT,
                                       fire_real_time_vegetation_information_id INT,
                                       FOREIGN KEY (fire_real_time_economic_damage_report_id) REFERENCES fire_real_time_economic_damage_report (id),
                                       FOREIGN KEY (fire_real_time_vegetation_information_id) REFERENCES fire_real_time_vegetation_information (id)
);

-- Создание таблицы fire_real_time_economic_damage_report
CREATE TABLE fire_real_time_economic_damage_report (
                                                       id SERIAL PRIMARY KEY,
                                                       agriculture_damage DECIMAL(10, 2),
                                                       forestry_damage DECIMAL(10, 2),
                                                       infrastructure_damage DECIMAL(10, 2),
                                                       total_economic_damage DECIMAL(10, 2),
                                                       firefighting_costs DECIMAL(10, 2),
                                                       analysis VARCHAR(10000),
                                                       conclusions VARCHAR(10000)
);

-- Создание таблицы fire_real_time_vegetation_information
CREATE TABLE fire_real_time_vegetation_information (
                                                       id SERIAL PRIMARY KEY,
                                                       vegetation_type VARCHAR(255),
                                                       vegetation_area DECIMAL(10, 2),
                                                       vegetation_density DECIMAL(8, 2),
                                                       vegetation_moisture DECIMAL(8, 2)
);
INSERT INTO fire_real_time_report (start_date, end_date, region, fire_area, fire_cause, latitude, longitude, fire_real_time_economic_damage_report_id, fire_real_time_vegetation_information_id)
VALUES
    ('2023-10-20', '2023-10-21', 'Жамбылская область', 50.75, 'Природные причины: молния, гроза', 43.123456, 76.654321, 1, 1);


INSERT INTO fire_real_time_economic_damage_report (agriculture_damage, forestry_damage, infrastructure_damage, total_economic_damage, firefighting_costs, analysis, conclusions
) VALUES (
             1000000,  500000,   300000,   1800000,   250000,
             'Анализ отчета об экономическом ущербе, вызванном пожаром, выявил значительные финансовые потери для района. Ущерб для сельского хозяйства, лесопромышленности и инфраструктуры составил внушительную сумму в размере 1 800 000 тенге. Этот ущерб включает в себя затраты на восстановление и ремонт, потери доходов от продажи сельскохозяйственной и лесопромышленной продукции, а также затраты на тушение пожара. Данный анализ подчеркивает необходимость принятия мер по предотвращению пожаров и эффективному управлению кризисными ситуациями для минимизации экономического ущерба и обеспечения устойчивости района.', 'Суммарный экономический ущерб составил 1 800 000 тенге. Этот ущерб включает в себя потерю доходов от сельского хозяйства и лесопромышленности, а также затраты на восстановление инфраструктуры.'
         );
INSERT INTO fire_real_time_vegetation_information (vegetation_type, vegetation_area, vegetation_density, vegetation_moisture)
VALUES ('Лес', 500.0, 30.5, 12.5);

ALTER TABLE fire_real_time_vegetation_information
    ADD COLUMN information VARCHAR(25000);