    CREATE SEQUENCE public.coin_by_exchange_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
    
ALTER SEQUENCE public.coin_by_exchange_id_seq
    OWNER TO postgres;
    
    CREATE SEQUENCE public.coin_id_seq
    INCREMENT 1
    START 45951
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.coin_id_seq
    OWNER TO postgres;
    
    CREATE SEQUENCE public.exchange_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.exchange_id_seq
    OWNER TO postgres;
    
    CREATE SEQUENCE public.group_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.group_id_seq
    OWNER TO postgres;
    
    CREATE SEQUENCE public."quotation_id_seq"
    INCREMENT 1
    START 115026
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public."quotation_id_seq"
    OWNER TO postgres;
    
    CREATE SEQUENCE public."user_id_seq"
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public."user_id_seq"
    OWNER TO postgres;
    
    CREATE SEQUENCE public."coin_per_user_id_seq"
    INCREMENT 1
    START 26
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public."coin_per_user_id_seq"
    OWNER TO postgres;
    
    CREATE SEQUENCE public."exchange_api_id_seq"
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public."exchange_api_id_seq"
    OWNER TO postgres;
    
    
     CREATE SEQUENCE public."user_group_id_seq"
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public."user_group_id_seq"
    OWNER TO postgres;   
    