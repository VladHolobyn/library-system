ALTER TABLE book
    ALTER COLUMN cover_image TYPE bytea USING REPLACE(cover_image, '\', '\\')::bytea;