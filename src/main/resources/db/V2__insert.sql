-- INSERT VALUES
INSERT INTO Products (title)
VALUES ('I7-7700k'),
       ('R5 3600x');

INSERT INTO Organizations ("INN", name, checking_account)
VALUES (827364, 'VK-RU', 9837498),
       (829999, 'Facebook', 8888321);

INSERT INTO Overhead (date, organization_id)
VALUES ('08-09-23', 827364),
       ('09-09-23', 827364),
       ('23-11-23', 829999);

INSERT INTO Invoice (overhead_id, product_id, price, amount)
VALUES (1, 1, 200, 30),
       (2, 2, 322, 10),
       (3, 2, 999, 1);