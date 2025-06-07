CREATE TABLE IF NOT EXISTS roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       rolename VARCHAR(255) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       enabled BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE IF NOT EXISTS user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id),
                            FOREIGN KEY (role_id) REFERENCES roles(id)
);


insert into product (name, brand, made_in, price) values ('Galaxy S6', 'Samsung Corp', 'Korea', 600.0);
insert into product (name, brand, made_in, price) values ('Galaxy S8', 'Samsung Corp', 'Korea', 800.0);
insert into product (name, brand, made_in, price) values ('Galaxy S10', 'Samsung Corp', 'Korea', 1000.0);
insert into product (name, brand, made_in, price) values ('Galaxy S21', 'Samsung Corp', 'Korea', 1000.0);

insert into product (name, brand, made_in, price) values ('Pizza Margherita', 'Domino\'s', 'Italy',  15);
insert into product (name, brand, made_in, price) values ('Sushi Rolls', 'Sushiro', 'Japan',  25);
insert into product (name, brand, made_in, price) values ('Tacos', 'Chipotle', 'USA',  10);
insert into product (name, brand, made_in, price) values ('Bulgogi', 'Korean BBQ', 'South Korea',  20);
insert into product (name, brand, made_in, price) values ('Paella', 'La Padrera', 'Spain',  18);
insert into product (name, brand, made_in, price) values ('Ramen', 'Ichiraku Ramen', 'Japan',  12);
insert into product (name, brand, made_in, price) values ('Dim Sum', 'Jing Fong', 'China',  15);

insert into product (name, brand, made_in, price) values ('Pasta Bolognese', 'Barilla', 'Italy',  17);
insert into product (name, brand, made_in, price) values ('Pad Thai', 'Thip Samai', 'Thailand',  14);
insert into product (name, brand, made_in, price) values ('Kebabs', 'Zoubi Kebabs', 'Turkey',  13);
insert into product (name, brand, made_in, price) values ('Curry Chicken', 'Taj Palace', 'India',  16);
