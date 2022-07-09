DROP TABLE IF EXISTS car;
CREATE TABLE car (
     id bigint(20)  auto_increment NOT NULL,
     car_id varchar(50)  NOT NULL,
     model varchar(50) NOT NULL,
     rent_per_day decimal(8,2) NOT NULL,
     PRIMARY KEY (id)
);

INSERT INTO car(id,car_id,model,rent_per_day) VALUES (1,'BD888','BMW 650',100);
INSERT INTO car (id,car_id,model,rent_per_day) VALUES (2,'AJ889','BMW 650',100);
INSERT INTO car (id,car_id,model,rent_per_day) VALUES (3,'KY880','Toyota Camry',90);
INSERT INTO car (id,car_id,model,rent_per_day) VALUES (4,'RT881','Toyota Camry',90);

DROP TABLE IF EXISTS rental_cars;
CREATE TABLE rental_cars (
    id bigint(20)  auto_increment NOT NULL,
    phone_num varchar(50) NOT NULL,
    car_id varchar(50) NOT NULL,
    start_day DATE  NOT NULL,
    end_day DATE  NOT NULL,
    create_time DATETIME  NOT NULL,
    update_time DATETIME  NOT NULL,
    rent decimal(8,2) NOT NULL,
    return_flag varchar(2) NOT NULL DEFAULT 'N',
    PRIMARY KEY (id)
);


