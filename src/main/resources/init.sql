create table USERS
(
    id         bigint auto_increment,
    user_id    varchar(255) not null,
    email      varchar(255) not null,
    password   varchar(255) not null,
    content    mediumtext   null,
    role       varchar(20),
    is_deleted tinyint(1)   not null default false,
    created_at datetime     not null default now(),
    updated_at datetime     not null default now(),
    deleted_at datetime     null,
    PRIMARY KEY (id),
    UNIQUE (user_id),
    UNIQUE (email)
);

create table SKILL_SETS
(
    id         int auto_increment,
    name       varchar(255) not null,
    created_at datetime     not null default now(),
    primary key (id)
);

create table USER_SKILLS
(
    id         int auto_increment,
    user_id    bigint,
    skill_id   bigint,
    created_at datetime not null default now(),
    primary key (id),
    foreign key (user_id) references USERS (id) on delete cascade
);

create table REQUEST
(
    id           int auto_increment,
    from_user_id bigint      not null,
    to_user_id   bigint      not null,
    status       varchar(20) not null default 'WAITING',
    is_deleted   tinyint(1)  not null default false,
    created_at   datetime             default now(),
    updated_at   datetime             default now(),
    deleted_at   datetime    null,
    primary key (id),
    foreign key (from_user_id) references USERS (id),
    foreign key (to_user_id) references USERS (id)
);

create table POST
(
    id         int auto_increment,
    author_id  bigint,
    content    text       not null,
    is_deleted tinyint(1) not null default false,
    created_at datetime            default now(),
    updated_at datetime            default now(),
    deleted_at datetime   null,
    primary key (id),
    foreign key (author_id) references USERS (id) on delete set null
);


create table MENTORING_SCHEDULE
(
    id         int auto_increment,
    mentee_id  bigint,
    mentor_id  bigint,
    start_time datetime   not null,
    end_time   datetime   not null,
    is_deleted tinyint(1) not null default false,
    created_at datetime   not null default now(),
    updated_at datetime   not null default now(),
    deleted_at datetime,
    primary key (id),
    foreign key (mentee_id) references USERS (id) on delete set null,
    foreign key (mentor_id) references USERS (id) on delete set null
);

create table MENTORING_SCHEDULE_DETAIL
(
    id         int auto_increment,
    start_time datetime     not null,
    end_time   datetime     not null,
    title      varchar(255) not null,
    memo       mediumtext   null,
    request_id int,
    is_deleted tinyint(1)            default false,
    status     varchar(255) not null default 'PENDING',
    created_at datetime     not null default now(),
    updated_at datetime     not null default now(),
    deleted_at datetime,
    primary key (id),
    foreign key (request_id) references REQUEST (id) on delete set null
);

create table COMMENT
(
    id         int auto_increment,
    post_id    int        not null,
    author_id  bigint     not null,
    content    mediumtext not null,
    created_at datetime   not null default now(),
    updated_at datetime   not null default now(),
    primary key (id)
);

create table POST_LIKE
(
    id      int auto_increment,
    post_id int not null,
    user_id bigint,
    primary key (id),
    foreign key (user_id) references USERS (id)
);

create table MENTOR_REVIEWS
(
    id                 int auto_increment,
    content            mediumtext null,
    author_id          bigint,
    mentor_id          bigint,
    mentor_schedule_id int        not null,
    rate               tinyint    not null,
    created_at         datetime   not null default now(),
    primary key (id),
    foreign key (author_id) references USERS (id) on delete set null,
    foreign key (mentor_id) references USERS (id) on delete set null
);
