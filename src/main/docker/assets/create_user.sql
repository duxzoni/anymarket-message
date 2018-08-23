CREATE TABLESPACE MESSAGES DATAFILE '/u01/app/oracle/oradata/XE/MESSAGES_DATAFILE.dbf' SIZE 500M autoextend on next 128m maxsize 4096m EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;

create user MESSAGES identified by MESSAGES 
temporary tablespace temp 
default tablespace MESSAGES 
quota unlimited on MESSAGES 
account unlock; 
grant connect, resource to  MESSAGES;

GRANT DBA TO MESSAGES;