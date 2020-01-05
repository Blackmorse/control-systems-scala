# --- !Ups

create table `documents` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `number` INT,
    `name` text,
    `document_date` text,

    CONSTRAINT
      PRIMARY KEY(`id`)
)


# --- !Downs
drop table `documents`