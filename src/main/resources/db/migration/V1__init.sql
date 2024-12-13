create sequence fieldaccess_fieldid_seq as integer;
create sequence packageaccess_packageid_seq as integer;
create sequence resourceaccess_resourceid_seq as integer;

create table client
(
    username             varchar(255) not null
        constraint client_pk
            primary key,
    is_adapter           boolean      not null,
    allowed_environments varchar(1000)
);

create table package_access
(
    package_id     integer default nextval('packageaccess_packageid_seq'::regclass) not null
        constraint packageaccess_pk
            primary key,
    domain_name    varchar(255)                                                     not null,
    package_name   varchar(255)                                                     not null,
    has_fullaccess boolean                                                          not null,
    username       varchar(255)                                                     not null
        constraint packageaccess_client_fk
            references client
);

create table resource_access
(
    resource_id         integer default nextval('resourceaccess_resourceid_seq'::regclass) not null
        constraint resourceaccess_pk
            primary key,
    package_id          integer                                                            not null,
    resource_name       varchar(255)                                                       not null,
    has_access          boolean                                                            not null,
    has_write_access    boolean                                                            not null,
    has_read_all_access boolean                                                            not null
);

create table field_access
(
    field_id     integer default nextval('fieldaccess_fieldid_seq'::regclass) not null
        constraint fieldaccess_pk
            primary key,
    resource_id  integer                                                      not null
        constraint fieldaccess_resourceaccess_resourceid_fk
            references resource_access,
    field_name   varchar(255)                                                 not null,
    must_contain text,
    has_access   boolean                                                      not null
);

alter sequence fieldaccess_fieldid_seq owned by field_access.field_id;
alter sequence packageaccess_packageid_seq owned by package_access.package_id;
alter sequence resourceaccess_resourceid_seq owned by resource_access.resource_id;
