create or replace function upd_timestamp() returns trigger as
$$
begin
 new.updated_at= current_timestamp;
 return new;
end
$$
language plpgsql;

create extension "uuid-ossp";

create table if not exists sim_user(
    id varchar(255) NOT NULL,
    "name" varchar(255) not null,
    gender int4 default 0,
    password varchar(255) not null,
    role_id int4 default 0,
    class_id varchar(255) default null,
    phone varchar(255) not null,
    identification_number varchar(255) not null,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    CONSTRAINT "sim_user_pkey" PRIMARY KEY ( "id" )
);

create trigger t_name before update on sim_user for each row execute procedure upd_timestamp();

create table if not exists class(
    id UUID primary key DEFAULT uuid_generate_v4(),
    "name" varchar(255) not null,
    grade varchar(255) not null,
    major varchar(255) not null,
    department varchar(255) not null,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create trigger t_name before update on class for each row execute procedure upd_timestamp();

create table if not exists class_course(
    class_id varchar(255) NOT NULL,
    course_id varchar(255) NOT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    CONSTRAINT "class_course_pk" PRIMARY KEY ( "class_id", "course_id" )
);

create trigger t_name before update on class_course for each row execute procedure upd_timestamp();

create table if not exists course_chapter_section(
    course_id UUID NOT NULL,
    chapter_id UUID NOT NULL,
    section_id UUID NOT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    CONSTRAINT "course_chapter_section_pk" PRIMARY KEY ( "course_id", "chapter_id", "section_id")
);

create trigger t_name before update on course_chapter_section for each row execute procedure upd_timestamp();


create table if not exists course(
    id UUID primary key DEFAULT uuid_generate_v4(),
    "name" varchar(255) not null,
    pic_url varchar(255) not null,
    owner_id varchar(255) not null,
    introduction text not null,
    start_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    end_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    status int4 default 0,
    "type" int4 default 0,
    model_id varchar(255) DEFAULT null,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create index if not exists course_owner_id_index
    on course (owner_id);

create trigger t_name before update on course for each row execute procedure upd_timestamp();

create table if not exists chapter(
    id UUID primary key DEFAULT uuid_generate_v4(),
    "name" varchar(255) not null,
    introduction text not null,
    "order" int4 NOT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create trigger t_name before update on chapter for each row execute procedure upd_timestamp();

create table if not exists section(
    id UUID primary key DEFAULT uuid_generate_v4(),
    "name" varchar(255) not null,
    "order" int4 NOT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create trigger t_name before update on section for each row execute procedure upd_timestamp();

create table if not exists section_courseware(
    section_id varchar(255) NOT NULL,
    courseware_id varchar(255) NOT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    CONSTRAINT "section_courseware_pk" PRIMARY KEY ( "section_id", "courseware_id")
);

create trigger t_name before update on section_courseware for each row execute procedure upd_timestamp();

create table if not exists courseware(
    id UUID primary key DEFAULT uuid_generate_v4(),
    "name" varchar(255) NOT NULL,
    kind int4 default 0,
    "type" int4 default 0,
    "url" varchar(255) DEFAULT NULL,
    category_id varchar(255),
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create index if not exists courseware_name_index
    on courseware ("name");

create trigger t_name before update on courseware for each row execute procedure upd_timestamp();

create table if not exists category(
    id UUID primary key DEFAULT uuid_generate_v4(),
    "name" varchar(255) NOT NULL,
    parent_category_id varchar(255),
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create trigger t_name before update on category for each row execute procedure upd_timestamp();

create table if not exists assignment(
    id UUID primary key DEFAULT uuid_generate_v4(),
    "name" varchar(255) NOT NULL,
    section_id varchar(255) NOT null,
    end_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    qualified_score int4 DEFAULT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create index if not exists assignment_section_id_index
    on assignment ("section_id");

create trigger t_name before update on assignment for each row execute procedure upd_timestamp();

create table if not exists question_back(
    id UUID primary key DEFAULT uuid_generate_v4(),
    "type" int4 DEFAULT 0,
    answer text,
    question text,
    model_id varchar(255) DEFAULT null,
    category_id varchar(255),
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create trigger t_name before update on question_back for each row execute procedure upd_timestamp();

create table if not exists question_back_assignment(
    assignment_id varchar(255) NOT NULL,
    question_id varchar(255) NOT NULL,
    "order" int4 NOT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    CONSTRAINT "question_back_assignment_pk" PRIMARY KEY ( "assignment_id", "question_id")
);

CREATE SEQUENCE question_back_assignment_order_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
alter table question_back_assignment alter column "order" set default nextval('question_back_assignment_order_seq');

create trigger t_name before update on question_back_assignment for each row execute procedure upd_timestamp();

create table if not exists student_answer(
    id UUID primary key DEFAULT uuid_generate_v4(),
    question_id varchar(255) DEFAULT null,
    assignment_id varchar(255) NOT NULL,
    user_id varchar(255) not null,
    answer text,
    score int4 DEFAULT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create index if not exists student_answer_user_id_index
    on student_answer ("user_id");

create trigger t_name before update on student_answer for each row execute procedure upd_timestamp();


create table if not exists student_score(
    assignment_id varchar(255) DEFAULT null,
    user_id varchar(255) NOT NULL,
    score int4 DEFAULT NULL,
    status int4 DEFAULT 0,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    CONSTRAINT "student_score_pk" PRIMARY KEY ( "assignment_id", "user_id")
);

create trigger t_name before update on student_score for each row execute procedure upd_timestamp();

create table if not exists experiment(
    id UUID primary key DEFAULT uuid_generate_v4(),
    section_id varchar(255) DEFAULT null,
    "name" varchar(255) NOT NULL,
    pic_url varchar(255) DEFAULT null,
    end_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    step_url varchar(255) DEFAULT null,
    duration int8 DEFAULT 0,
    category_id varchar(255) NOT null,
    "type" int4 default 0,
    parent_experiment_id varchar(255) DEFAULT null,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create trigger t_name before update on experiment for each row execute procedure upd_timestamp();


create table if not exists experiment_image(
    experiment_id UUID NOT NULL,
    image_id UUID NOT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    CONSTRAINT "experiment_image_pk" PRIMARY KEY ( "experiment_id", "image_id")
);

create trigger t_name before update on experiment_image for each row execute procedure upd_timestamp();

create table if not exists image(
    id UUID primary key DEFAULT uuid_generate_v4(),
    "name" varchar(255) NOT NULL,
    "size" int8 DEFAULT 0,
    introduction text not null,
    kind int4 DEFAULT 0,
    "type" int4 DEFAULT 0,
    parent_image_id varchar(255) DEFAULT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create trigger t_name before update on image for each row execute procedure upd_timestamp();

create table if not exists experiment_report(
    experiment_id varchar(255) NOT NULL,
    user_id varchar(255) NOT NULL,
    url varchar(255) DEFAULT null,
    score int4 DEFAULT NULL,
    status int4 default 0,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    CONSTRAINT "experiment_report_pk" PRIMARY KEY ( "experiment_id", "user_id")
);

create trigger t_name before update on experiment_report for each row execute procedure upd_timestamp();

create table if not exists courseware_remark(
    id UUID primary key DEFAULT uuid_generate_v4(),
    courseware_id UUID NOT NULL,
    user_id varchar(255) NOT NULL,
    title varchar(255) DEFAULT null,
    content varchar(255) DEFAULT NULL,
    "type" int4 DEFAULT 0,
    remark_page int4 DEFAULT NULL,
    remark_at int8  DEFAULT NUll,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create trigger t_name before update on courseware_remark for each row execute procedure upd_timestamp();


create table if not exists user_serve(
    id UUID primary key DEFAULT uuid_generate_v4(),
    user_id varchar(255) NOT NULL,
    server_ip varchar(255) DEFAULT null,
    server_password varchar(255) DEFAULT null,
    server_port varchar(255) DEFAULT null,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create trigger t_name before update on user_serve for each row execute procedure upd_timestamp();
