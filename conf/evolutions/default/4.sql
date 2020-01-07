# --- !Ups
create table `document_parameters` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `document_id` INT NOT NULL,
    `parameter_id` INT NOT NULL,
    `parameter_value` text,
    CONSTRAINT PRIMARY KEY(`id`),
    FOREIGN KEY (`document_id`) REFERENCES `documents` (`id`),
    FOREIGN KEY (`parameter_id`) REFERENCES `parameters` (`id`)
)

# --- !Downs

drop table `document_parameters`