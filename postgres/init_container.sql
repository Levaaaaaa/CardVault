DO
$$
BEGIN
   IF NOT EXISTS (
      SELECT FROM pg_database WHERE datname = 'cardvault_db'
   ) THEN
      CREATE DATABASE "cardvault_db";
   END IF;
END
$$;
