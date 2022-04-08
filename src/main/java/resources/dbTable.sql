CREATE TABLE IF NOT EXISTS public.player
(
    "player_id"
    bigserial
    NOT
    NULL,
    "registration_date"
    timestamp
    without
    time
    zone
    NOT
    NULL,
    "status"
    integer
    NOT
    NULL,
    "country"
    integer
    NOT
    NULL,
    "balance"
    double
    precision
    NOT
    NULL,
    "currency"
    character
    varying
(
    3
) NOT NULL,
    PRIMARY KEY
(
    "player_id"
)
    );

CREATE TABLE IF NOT EXISTS public.countries
(
    "country_id"
    integer
    NOT
    NULL,
    "country_name"
    character
    varying
(
    50
) NOT NULL,
    "country_code" character varying
(
    2
) NOT NULL,
    PRIMARY KEY
(
    "country_id"
)
    );

CREATE TABLE IF NOT EXISTS public.status
(
    "status_id"
    integer
    NOT
    NULL,
    "state_name"
    character
    varying
(
    20
) NOT NULL,
    "description" character varying
(
    50
) NOT NULL,
    PRIMARY KEY
(
    "status_id"
)
    );

CREATE TABLE IF NOT EXISTS public.game
(
    "game_id" character varying
(
    50
) NOT NULL,
    "status_id" integer NOT NULL,
    PRIMARY KEY
(
    "game_id"
)
    );

CREATE TABLE IF NOT EXISTS public."game_round"
(
    "round_id"
    integer
    NOT
    NULL,
    "player_id"
    integer
    NOT
    NULL,
    "game_id"
    character
    varying
(
    50
) NOT NULL,
    "start_time" timestamp without time zone NOT NULL,
    "end_time" timestamp
                           without time zone,
    "is_finished" boolean NOT NULL,
    PRIMARY KEY
(
    "round_id"
)
    );

CREATE TABLE IF NOT EXISTS public."transaction"
(
    "tx_id"
    integer
    NOT
    NULL,
    "round_id"
    integer
    NOT
    NULL,
    "amount"
    numeric
    NOT
    NULL,
    type
    character
    varying
(
    20
) NOT NULL,
    "time_stamp" timestamp without time zone NOT NULL,
    "wallet_tx_id" character varying
(
    50
) NOT NULL,
    "currency" character varying
(
    3
) NOT NULL,
    PRIMARY KEY
(
    "tx_id"
)
    );

ALTER TABLE public.player
    ADD FOREIGN KEY ("country")
        REFERENCES public.countries ("country_id")
        ON DELETE CASCADE ON UPDATE CASCADE
    NOT VALID;


ALTER TABLE public.player
    ADD FOREIGN KEY ("status")
        REFERENCES public.status ("status_id")
        ON DELETE CASCADE ON UPDATE CASCADE
    NOT VALID;


ALTER TABLE public.game
    ADD FOREIGN KEY ("status_id")
        REFERENCES public.status ("status_id")
        ON DELETE CASCADE ON UPDATE CASCADE
    NOT VALID;


ALTER TABLE public."game_round"
    ADD FOREIGN KEY ("player_id")
        REFERENCES public.player ("player_id")
        ON DELETE CASCADE ON UPDATE CASCADE
    NOT VALID;


ALTER TABLE public."game_round"
    ADD FOREIGN KEY ("game_id")
        REFERENCES public.game ("game_id")
        ON DELETE CASCADE ON UPDATE CASCADE
    NOT VALID;


ALTER TABLE public."transaction"
    ADD FOREIGN KEY ("round_id")
        REFERENCES public."game_round" ("round_id")
        ON DELETE CASCADE ON UPDATE CASCADE
    NOT VALID;


CREATE INDEX idx_country_id
    ON countries (country_id);

CREATE INDEX idx_game_id
    ON game (game_id);

CREATE INDEX idx_game_round_id
    ON game_round (round_id);

CREATE INDEX idx_game_round_game_id
    ON game_round (game_id);

CREATE INDEX idx_game_round_start_time
    ON game_round (start_time);

CREATE INDEX idx_game_round_end_time
    ON game_round (end_time);

CREATE INDEX idx_game_round_is_finished
    ON game_round (is_finished);

CREATE INDEX idx_player_id
    ON player (player_id);

CREATE INDEX idx_player_status
    ON player (status);

CREATE INDEX idx_player_balance
    ON player (balance);

CREATE INDEX idx_player_currency
    ON player (currency);


CREATE INDEX idx_status_id
    ON status (status_id);

CREATE INDEX idx_transaction_tx_id
    ON transaction (tx_id);

CREATE INDEX idx_transaction_round_id
    ON transaction (round_id);

CREATE INDEX idx_transaction_amount
    ON transaction (amount);

CREATE INDEX idx_transaction_type
    ON transaction (type);

CREATE INDEX idx_transaction_wallet_tx_id
    ON transaction (wallet_tx_id);

CREATE INDEX idx_transaction_currency
    ON transaction (currency);