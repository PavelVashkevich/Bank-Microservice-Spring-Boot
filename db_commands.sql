CREATE TABLE currency_exchanges(
    id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    symbol varchar NOT NULL,
    rate numeric NOT NULL,
    datetime DATE NOT NULL
);