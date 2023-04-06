CREATE TYPE borrowing_status AS ENUM('RESERVED', 'BORROWED', 'RETURNED');
CREATE TYPE user_status AS ENUM('CLIENT', 'ADMIN');

CREATE CAST (CHARACTER VARYING as user_status) WITH INOUT AS IMPLICIT;
CREATE CAST (CHARACTER VARYING as borrowing_status) WITH INOUT AS IMPLICIT;

CREATE TABLE "book"(
    "id" BIGINT GENERATED ALWAYS AS IDENTITY,
    "title" VARCHAR(255) NOT NULL,
    "cover_image" VARCHAR(255) NOT NULL,
    "description" TEXT NULL,
    "publication_year" INTEGER NOT NULL,
    "quantity" INTEGER NOT NULL
);
ALTER TABLE "book" ADD PRIMARY KEY("id");

CREATE TABLE "borrowing"(
    "id" BIGINT GENERATED ALWAYS AS IDENTITY,
    "user_id" BIGINT NOT NULL,
    "book_id" BIGINT NOT NULL,
    "reservation_date" DATE NULL,
    "checkout_date" DATE NULL,
    "due_date" DATE NULL,
    "return_date" DATE NULL,
    "status" borrowing_status NOT NULL
);
ALTER TABLE "borrowing" ADD PRIMARY KEY("id");

CREATE TABLE "user"(
    "id" BIGINT GENERATED ALWAYS AS IDENTITY,
    "first_name" VARCHAR(255) NOT NULL,
    "last_name" VARCHAR(255) NOT NULL,
    "email" VARCHAR(255) NOT NULL UNIQUE,
    "address" VARCHAR(255) NOT NULL,
    "phone_number" VARCHAR(255) NOT NULL,
    "password" VARCHAR(255) NOT NULL,
    "role" user_status NOT NULL
);
ALTER TABLE "user" ADD PRIMARY KEY("id");

CREATE TABLE "category"(
    "id" BIGINT GENERATED ALWAYS AS IDENTITY,
    "name" VARCHAR(255) NOT NULL UNIQUE
);
ALTER TABLE "category" ADD PRIMARY KEY("id");

CREATE TABLE "author"(
    "id" BIGINT GENERATED ALWAYS AS IDENTITY,
    "first_name" VARCHAR(255) NOT NULL,
    "last_name" VARCHAR(255) NOT NULL
);
ALTER TABLE "author" ADD PRIMARY KEY("id");

CREATE TABLE "book-author"(
    "book_id" BIGINT NOT NULL,
    "author_id" BIGINT NOT NULL
);
ALTER TABLE "book-author" ADD PRIMARY KEY("book_id", "author_id");

CREATE TABLE "book-category"(
    "book_id" BIGINT NOT NULL,
    "category_id" BIGINT NOT NULL
);
ALTER TABLE "book-category" ADD PRIMARY KEY("book_id", "category_id");


ALTER TABLE "book-category"
    ADD CONSTRAINT "book_id_foreign" FOREIGN KEY("book_id") REFERENCES "book"("id"),
    ADD CONSTRAINT "category_id_foreign" FOREIGN KEY("category_id") REFERENCES "category"("id");

ALTER TABLE "book-author"
    ADD CONSTRAINT "author_id_foreign" FOREIGN KEY("author_id") REFERENCES "author"("id"),
    ADD CONSTRAINT "book_id_foreign" FOREIGN KEY("book_id") REFERENCES "book"("id");

ALTER TABLE "borrowing"
    ADD CONSTRAINT "borrowing_user_id_foreign" FOREIGN KEY("user_id") REFERENCES "user"("id"),
    ADD CONSTRAINT "borrowing_book_id_foreign" FOREIGN KEY("book_id") REFERENCES "book"("id");