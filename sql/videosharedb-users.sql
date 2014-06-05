drop user 'videoshare'@'localhost';
create user 'videoshare'@'localhost' identified by 'videoshare';
grant all privileges on videosharedb.* to 'videoshare'@'localhost';
flush privileges;
