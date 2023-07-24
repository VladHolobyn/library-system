CREATE TABLE "book_availability_subscription"
(
    "id"      BIGINT GENERATED ALWAYS AS IDENTITY,
    "user_id" BIGINT NOT NULL REFERENCES "user" ("id"),
    "book_id" BIGINT NOT NULL REFERENCES "book" ("id")
);
