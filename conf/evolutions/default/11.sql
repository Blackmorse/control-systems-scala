# --- !Ups
create table `users` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(30),
    `password_hash` VARCHAR(256),

    CONSTRAINT
      PRIMARY KEY(`id`),
      UNIQUE KEY `unique_username` (`username`)
)

# --- !Downs
drop table `users`