DROP DATABASE IF EXISTS `demo_blog`;
CREATE DATABASE `demo_blog` default charset=utf8 ;
use `demo_blog`;


delete from mysql.user where User = 'demo_blog_user';
grant all on `demo_blog`.* to 'demo_blog_user'@'%' identified by 'demo_blog_pwd';
grant all on `demo_blog`.* to 'demo_blog_user'@'localhost' identified by 'demo_blog_pwd';
flush privileges;