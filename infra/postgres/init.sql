CREATE USER poc_user WITH PASSWORD 'poc_password';

CREATE DATABASE order_db
    OWNER poc_user;

CREATE DATABASE payment_db
    OWNER poc_user;

CREATE DATABASE inventory_db
    OWNER poc_user;

CREATE DATABASE shipping_db
    OWNER poc_user;