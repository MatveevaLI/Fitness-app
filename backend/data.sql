INSERT INTO categories (name) VALUES
('breakfast'), 
('lunch'), 
('dinner'), 
('snack')

INSERT INTO type (name) VALUES
('vegetables'), 
('fruits'), 
('meat'),
('fish'),
('cereals') 

INSERT INTO products(name, calories, fats, carbohydrates, protein, mass, type_id, created_at, updated_at, user_id) VALUES
('potato', '77','0.4', '16.3', '2', '100','1', CURRENT_DATE, CURRENT_DATE, '1'),
('banana', '89','0.3', '22.8', '1.1', '100','2', CURRENT_DATE, CURRENT_DATE, '1'),
('chicken', '238','18.4', '0', '18.2', '100','3', CURRENT_DATE, CURRENT_DATE, '1'),
('salmon', '153','8.1', '0', '20', '100','4', CURRENT_DATE, CURRENT_DATE, '1'),
('rice', '303','2.6', '62.3', '7.5', '100','5', CURRENT_DATE, CURRENT_DATE, '1');

INSERT INTO food_diaries(category_id, product_id, user_id, created_at, updated_at) VALUES
('1', '1','1',CURRENT_DATE ,CURRENT_DATE), 
('2', '2','1',CURRENT_DATE,CURRENT_DATE), 
('3', '3','1',CURRENT_DATE,CURRENT_DATE), 
('4', '4','1',CURRENT_DATE,CURRENT_DATE), 
('4', '5', '1',CURRENT_DATE,CURRENT_DATE);

INSERT INTO food_diaries(category_id, product_id, user_id, created_at, updated_at) VALUES
('1', '1','2',CURRENT_DATE ,CURRENT_DATE), 
('2', '2','2',CURRENT_DATE,CURRENT_DATE), 
('3', '3','2',CURRENT_DATE,CURRENT_DATE), 
('4', '4','2',CURRENT_DATE,CURRENT_DATE), 
('4', '5', '2',CURRENT_DATE,CURRENT_DATE);