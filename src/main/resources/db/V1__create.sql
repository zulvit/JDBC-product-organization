-- CREATE TABLE
CREATE TABLE Overhead
(
    "ID"              serial  NOT NULL,
    "date"            DATE    NOT NULL,
    "organization_id" integer NOT NULL,
    CONSTRAINT Overhead_pk PRIMARY KEY ("ID")
) WITH (
      OIDS= FALSE
    );



CREATE TABLE Organizations
(
    "INN"              serial  NOT NULL,
    "name"             TEXT    NOT NULL,
    "checking_account" integer NOT NULL,
    CONSTRAINT Organizations_pk PRIMARY KEY ("INN")
) WITH (
      OIDS= FALSE
    );



CREATE TABLE Invoice
(
    "ID"          serial  NOT NULL PRIMARY KEY,
    "overhead_id" integer NOT NULL,
    "product_id"  integer NOT NULL,
    "price"       float8 NOT NULL,
    "amount"      integer NOT NULL
) WITH (
      OIDS= FALSE
    );



CREATE TABLE Products
(
    "ID"    serial NOT NULL,
    "title" TEXT   NOT NULL,
    CONSTRAINT Products_pk PRIMARY KEY ("ID")
) WITH (
      OIDS= FALSE
    );



ALTER TABLE Overhead
    ADD CONSTRAINT Overhead_fk0 FOREIGN KEY ("organization_id") REFERENCES Organizations ("INN");
ALTER TABLE Invoice
    ADD CONSTRAINT Invoice_fk0 FOREIGN KEY ("overhead_id") REFERENCES Overhead ("ID");
ALTER TABLE Invoice
    ADD CONSTRAINT Invoice_fk1 FOREIGN KEY ("product_id") REFERENCES Products ("ID");





