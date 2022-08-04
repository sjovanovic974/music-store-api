
create table artists (
    id bigint not null auto_increment,
    name varchar(50) not null,
    primary key (id)) engine=InnoDB;

create table password_reset_token (
    id bigint not null auto_increment,
    expiration_time datetime(6),
    token varchar(255),
    user_id bigint not null,
    primary key (id)) engine=InnoDB;

create table product_categories (
    id bigint not null auto_increment,
    name varchar(4) not null,
    primary key (id)) engine=InnoDB;

create table products (
    id bigint not null auto_increment,
    active bit not null,
    date_created datetime(6),
    description varchar(255),
    image_url varchar(255) not null,
    last_updated datetime(6),
    name varchar(255) not null,
    sku varchar(255) not null,
    unit_price decimal(19,2) not null,
    units_in_stock integer not null,
    artist_id bigint not null,
    category_id bigint not null, primary key (id)) engine=InnoDB;

create table users (
    id bigint not null auto_increment,
    email varchar(255) not null,
    enabled bit not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    password varchar(60) not null,
    role varchar(255) not null,
    primary key (id)) engine=InnoDB;

create table verification_token (
    id bigint not null auto_increment,
    expiration_time datetime(6),
    token varchar(255), user_id bigint not null,
    primary key (id)) engine=InnoDB;

alter table artists add constraint UK_band_name unique (name);
alter table product_categories add constraint UK_category_name unique (name);
alter table products add constraint UK_sku unique (sku);
alter table users add constraint UK_Email unique (email);

alter table password_reset_token add constraint FK_USER_PASSWORD_TOKEN foreign key (user_id) references users (id);
alter table products add constraint FK4yife2w19dc69tfmgfbi00b8j foreign key (artist_id) references artists (id);
alter table products add constraint FK6t5dtw6tyo83ywljwohuc6g7k foreign key (category_id) references product_categories (id);
alter table verification_token add constraint FK_USER_VERIFY_TOKEN foreign key (user_id) references users (id);
