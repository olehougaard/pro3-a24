SET SCHEMA 'car_base';

CREATE TABLE Car (
  license_number VARCHAR(20) PRIMARY KEY,
  model VARCHAR(50) NOT NULL,
  year INT NOT NULL CHECK(year >= 1900),
  price_amount DECIMAL NOT NULL CHECK(price_amount > 0),
  price_currency CHAR(3) NOT NULL);

CREATE TABLE "User" (
  username VARCHAR(50) PRIMARY KEY,
  password TEXT NOT NULL
);

CREATE TABLE Roles (
  role VARCHAR(20) PRIMARY KEY
);

CREATE TABLE User_Roles (
  username VARCHAR(50) REFERENCES "User"(username),
  role VARCHAR(20) REFERENCES Roles(role),
  primary key (username, role)
);
