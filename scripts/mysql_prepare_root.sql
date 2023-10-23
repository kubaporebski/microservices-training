-- TODO: Maybe it will be beneficial in the foreseeable future.
create user root@'%' identified by 'root';
grant all privileges on *.* to root@'%' with grant option;
flush privileges;
drop user root@localhost;
flush privileges;



