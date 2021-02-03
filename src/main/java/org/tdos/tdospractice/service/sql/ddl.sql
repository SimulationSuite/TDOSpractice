create or replace function upd_timestamp() returns trigger as
$$
begin
 new.updated_at= current_timestamp;
 return new;
end
$$
language plpgsql;
create extension "uuid-ossp";

create table if not exists class(
    id UUID primary key DEFAULT uuid_generate_v4(),
    "name" varchar(255) not null,
    grade varchar(255) not null,
    major varchar(255) not null,
    department varchar(255) not null,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

INSERT INTO "public"."class"("id", "name", "grade", "major", "department") VALUES ('fb0a1080-b11e-427c-8567-56ca6105ea07', '空班级', '', '', '');


create trigger t_name before update on class for each row execute procedure upd_timestamp();

create table if not exists sim_user(
    id varchar(255) NOT NULL,
    "name" varchar(255) not null,
    gender int4 default 0,
    password varchar(255) not null,
    role_id int4 default 0,
    class_id UUID references "class"("id") default null,
    phone varchar(255) not null,
    identification_number varchar(255) not null,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    CONSTRAINT "sim_user_pkey" PRIMARY KEY ( "id" )
);

create trigger t_name before update on sim_user for each row execute procedure upd_timestamp();

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

create unique index if not exists course_name
    on course ("name");

create index if not exists course_owner_id_index
    on course (owner_id);

create trigger t_name before update on course for each row execute procedure upd_timestamp();



create table if not exists class_course(
    user_id varchar(255) references "sim_user"("id") on delete cascade NOT NULL,
    course_id UUID references "course"("id") on delete cascade NOT NULL,
    class_id UUID references "class"("id") on delete cascade NOT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    CONSTRAINT "class_course_pk" PRIMARY KEY ( "user_id", "course_id" )
);

create trigger t_name before update on class_course for each row execute procedure upd_timestamp();


create table if not exists chapter(
    id UUID primary key DEFAULT uuid_generate_v4(),
    "name" varchar(255) not null,
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

create table if not exists small_section(
    id UUID primary key DEFAULT uuid_generate_v4(),
    "name" varchar(255) not null,
    "order" int4 NOT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create trigger t_name before update on small_section for each row execute procedure upd_timestamp();

INSERT INTO "chapter"("id", "name", "order") VALUES ('fb0a1080-b11e-427c-8567-56ca6105ea07', '空章', -1);
INSERT INTO "section"("id", "name", "order") VALUES ('fb0a1080-b11e-427c-8567-56ca6105ea07', '空节', -1);
INSERT INTO "small_section"("id", "name", "order") VALUES ('fb0a1080-b11e-427c-8567-56ca6105ea07', '空小节', -1);

create table if not exists course_chapter_section(
    course_id UUID references "course"("id") on delete cascade NOT NULL,
    chapter_id UUID references "chapter"("id") on delete cascade NOT NULL,
    section_id UUID references "section"("id") on delete cascade NOT NULL,
    small_section_id UUID references "small_section"("id") on delete cascade NOT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    CONSTRAINT "course_chapter_section_pk" PRIMARY KEY ( "course_id", "chapter_id", "section_id", "small_section_id")
);

create trigger t_name before update on course_chapter_section for each row execute procedure upd_timestamp();

create table if not exists courseware(
    id UUID primary key DEFAULT uuid_generate_v4(),
    "name" varchar(255) NOT NULL,
    kind int4 default 0,
    "type" int4 default 0,
    "url" varchar(255) DEFAULT NULL,
    duration int8 DEFAULT NULL,
    "size" int8 DEFAULT 0,
    category_id varchar(255),
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create unique index if not exists courseware_name
    on courseware ("name");

create index if not exists courseware_name_index
    on courseware ("name");

create trigger t_name before update on courseware for each row execute procedure upd_timestamp();

create table if not exists chapter_section_courseware(
    id UUID primary key DEFAULT uuid_generate_v4(),
    chapter_id UUID references "chapter"("id") on delete cascade NOT NULL,
    section_id UUID references "section"("id") on delete cascade NOT NULL,
    courseware_id UUID references "courseware"("id") on delete cascade NOT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create trigger t_name before update on chapter_section_courseware for each row execute procedure upd_timestamp();


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
    section_id UUID references "section"("id") on delete cascade NOT NULL,
    end_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    qualified_score int4 DEFAULT NULL,
    status int4 DEFAULT 0,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create unique index if not exists assignment_name
    on assignment ("name");

create index if not exists assignment_section_id_index
    on assignment ("section_id");

create trigger t_name before update on assignment for each row execute procedure upd_timestamp();

create table if not exists question_back(
    id UUID primary key DEFAULT uuid_generate_v4(),
    "type" int4 DEFAULT 0,
    content text,
    choice text,
    answer text,
    pic_url varchar(255),
    model_id varchar(255) DEFAULT null,
    category_id varchar(255),
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create trigger t_name before update on question_back for each row execute procedure upd_timestamp();

create table if not exists question_back_assignment(
    assignment_id UUID references "assignment"("id") on delete cascade NOT NULL,
    question_id UUID references "question_back"("id") on delete cascade NOT NULL,
    "order" int4 NOT NULL,
    score int4 DEFAULT NULL,
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
    question_id UUID references "question_back"("id") NOT NULL,
    assignment_id UUID references "assignment"("id") NOT NULL,
    user_id varchar(255) not null,
    answer text,
    score int4 DEFAULT NULL,
    status int4 default 0,
    committed_at TIMESTAMP(0)  without time zone,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create index if not exists student_answer_user_id_index
    on student_answer ("user_id");

create trigger t_name before update on student_answer for each row execute procedure upd_timestamp();


create table if not exists student_score(
    assignment_id UUID references "assignment"("id") DEFAULT NULL,
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
    "name" varchar(255) NOT NULL,
    pic_url varchar(255) DEFAULT null,
    end_at TIMESTAMP(0)  without time zone default NULL,
    step text DEFAULT null,
    duration int8 DEFAULT 0,
    category_id varchar(255) NOT null,
    introduce varchar(255) DEFAULT null,
    report_requirement varchar(255) DEFAULT null,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create trigger t_name before update on experiment for each row execute procedure upd_timestamp();

create table if not exists chapter_section_experiment(
    id UUID primary key DEFAULT uuid_generate_v4(),
    section_id UUID references "section"("id") on delete cascade NOT NULL,
    experiment_id UUID references "experiment"("id") on delete cascade NOT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create trigger t_name before update on chapter_section_experiment for each row execute procedure upd_timestamp();

create table if not exists image(
    id UUID primary key DEFAULT uuid_generate_v4(),
    "name" varchar(255) NOT NULL,
    "imageid" varchar(255) NOT NULL,
    "size" int8 DEFAULT 0,
    introduction text not null,
    kind int4 DEFAULT 0,
    "type" int4 DEFAULT 0,
    parent_image_id varchar(255) DEFAULT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create trigger t_name before update on image for each row execute procedure upd_timestamp();

create table if not exists container(
    container_id varchar(255) primary key,
    "name" varchar(255) NOT NULL,
    user_id varchar(255) references "sim_user"("id") NOT NULL,
    course_id UUID references "course"("id"),
    experiment_id UUID references "experiment"("id") NOT NULL,
    image_id UUID references "image"("id") NOT NULL,
    ports varchar(255) NOT NULL,
    nodeOrder int4 NOT NULL,
    status int4 NOT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc')
);

create trigger t_name before update on container for each row execute procedure upd_timestamp();

create table if not exists experiment_image(
    experiment_id UUID references "experiment"("id") on delete cascade NOT NULL,
    image_id UUID references "image"("id") on delete cascade NOT NULL,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    CONSTRAINT "experiment_image_pk" PRIMARY KEY ( "experiment_id", "image_id")
);

create trigger t_name before update on experiment_image for each row execute procedure upd_timestamp();


create table if not exists experiment_report(
    experiment_id UUID references "experiment"("id") on delete cascade NOT NULL,
    user_id varchar(255) NOT NULL,
    info text DEFAULT null,
    score int4 DEFAULT NULL,
    status int4 default 0,
    isCorrect int4 default 0,
    end_at TIMESTAMP(0)  without time zone,
    created_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    updated_at TIMESTAMP(0)  without time zone default (now() at time zone 'utc'),
    CONSTRAINT "experiment_report_pk" PRIMARY KEY ( "experiment_id", "user_id")
);

create trigger t_name before update on experiment_report for each row execute procedure upd_timestamp();

create table if not exists courseware_remark(
    id UUID primary key DEFAULT uuid_generate_v4(),
    courseware_id UUID  references "courseware"("id") on delete cascade NOT NULL,
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

