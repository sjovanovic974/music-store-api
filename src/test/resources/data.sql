
insert into artists(name, country)
    values ('Iron Maiden', 'UK'),
           ('Metallica', 'USA'),
           ('Partibrejkers', 'Serbia');


insert into product_categories(name)
    values ('CD'),
           ('LP'),
           ('DVD'),
           ('BOOK');


insert into products(active, description, image_url, name, sku,
    unit_price, units_in_stock, artist_id, category_id)
    values (true, 'Third album', 'www.google.com', 'Master of Puppets', 'CD-000001', 15.00, 3, 1, 1),
           (true, 'Seminal album', 'www.google.com', '	Kiselo i slatko', 'CD-000002', 12.00, 5, 2, 1),
           (false, 'Fourth album', 'www.google.com', '...and Justice for All', 'CD-000003', 18.00, 0, 1, 1),
           (true, 'Eponymous live album', 'www.google.com', 'Live After Death', 'LP-000001', 35.00, 2, 3, 2),
           (true, 'Fifth album', 'www.google.com', 'Powerslave', 'LP-000002', 25.00, 1, 3, 2),
           (false, 'Debut album', 'www.google.com', 'Partibrejkers', 'LP-000003', 35.00, 0, 2, 2),
           (true, 'Early Metallica', 'www.google.com', 'Riders on the lightning', 'BK-000001', 15.00, 2, 1, 3),
           (false, 'Early Iron Maiden', 'www.google.com', '	Troopers of the metal', 'BK-000002', 12.00, 0, 3, 3),
           (true, 'Partibrejkersi na EXIT Festivalu 2022', 'www.google.com', 'EXIT 2022', 'DD-000001', 15.00, 1, 2, 4);