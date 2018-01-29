    CREATE TABLE public.exchange
(
    id integer NOT NULL DEFAULT nextval('"exchange_id_seq"'::regclass),
    name character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT exange_pk PRIMARY KEY (id),
    CONSTRAINT "Exange_name_key" UNIQUE (name)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.exchange
    OWNER to postgres;

CREATE TABLE public.coin
(
    id integer NOT NULL DEFAULT nextval('"coin_id_seq"'::regclass),
    name character varying COLLATE pg_catalog."default" NOT NULL,
    short_name character varying COLLATE pg_catalog."default" NOT NULL,
    final boolean NOT NULL DEFAULT false,
    CONSTRAINT coin_pk PRIMARY KEY (id),
    CONSTRAINT "Coin_name_key" UNIQUE (name),
    CONSTRAINT "Coin_short_name_key" UNIQUE (short_name)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.coin
    OWNER to postgres;
    
        CREATE TABLE public.user_info
(
    id integer NOT NULL DEFAULT nextval('"user_id_seq"'::regclass),
    login character varying COLLATE pg_catalog."default" NOT NULL,
    creation_timestamp timestamp without time zone NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id),
    CONSTRAINT "User_login_key" UNIQUE (login)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.user_info
    OWNER to postgres;
    
CREATE TABLE public.coin_by_exchange
(
    id integer NOT NULL DEFAULT nextval('coin_by_exchange_id_seq'::regclass),
    id_coin integer NOT NULL,
    id_exchange integer NOT NULL,
    id_coin_pair integer NOT NULL,
    CONSTRAINT coin_by_exchange_pk PRIMARY KEY (id),
    CONSTRAINT coin_by_exchange_fk0 FOREIGN KEY (id_coin)
        REFERENCES public.coin (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT coin_by_exchange_fk1 FOREIGN KEY (id_exchange)
        REFERENCES public.exchange (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT coin_by_exchange_id_coin_pair_fkey FOREIGN KEY (id_coin_pair)
        REFERENCES public.coin (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.coin_by_exchange
    OWNER to postgres; 
    
    CREATE TABLE public.coin_per_user
(
    id integer NOT NULL DEFAULT nextval('"coin_per_user_id_seq"'::regclass),
    id_user integer NOT NULL,
    id_coin_by_exchange integer NOT NULL,
    quantity numeric NOT NULL,
    invested numeric NOT NULL,
    satoshi_buy numeric NOT NULL,
    buy_date timestamp with time zone,
    CONSTRAINT coin_per_user_pk PRIMARY KEY (id),
    CONSTRAINT "Coin_per_user_fk" FOREIGN KEY (id_user)
        REFERENCES public.user_info (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT coin_by_exange_fk FOREIGN KEY (id_coin_by_exchange)
        REFERENCES public.coin_by_exchange (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.coin_per_user
    OWNER to postgres;
    
    CREATE TABLE public.exchange_api
(
    id integer NOT NULL DEFAULT nextval('"exchange_api_id_seq"'::regclass),
    id_exchange integer NOT NULL,
    api_url character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT exchange_api_pk PRIMARY KEY (id),
    CONSTRAINT "Exchange_api_fk0" FOREIGN KEY (id_exchange)
        REFERENCES public.exchange (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.exchange_api
    OWNER to postgres;
    
    CREATE TABLE public."group"
(
    id integer NOT NULL DEFAULT nextval('group_id_seq'::regclass),
    name character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT group_pk PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public."group"
    OWNER to postgres;
    
    CREATE TABLE public.quotation
(
    id integer NOT NULL DEFAULT nextval('"quotation_id_seq"'::regclass),
    id_coin_by_exchange integer NOT NULL,
    satoshis numeric NOT NULL,
    "timestamp" timestamp without time zone NOT NULL,
    CONSTRAINT quotation_pk PRIMARY KEY (id),
    CONSTRAINT "Quotation_fk0" FOREIGN KEY (id_coin_by_exchange)
        REFERENCES public.coin_by_exchange (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.quotation
    OWNER to postgres;
    
    CREATE TABLE public.user_group
(
    id integer NOT NULL DEFAULT nextval('"user_group_id_seq"'::regclass),
    id_group integer NOT NULL,
    id_user integer NOT NULL,
    is_manager boolean NOT NULL,
    CONSTRAINT user_group_pk PRIMARY KEY (id),
    CONSTRAINT "User_group_fk0" FOREIGN KEY (id_group)
        REFERENCES public."group" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT "User_group_fk1" FOREIGN KEY (id_user)
        REFERENCES public.user_info (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.user_group
    OWNER to postgres;
    
