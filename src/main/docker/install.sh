#!/bin/sh

echo "Criando usuários"
sleep 10
sqlplus sys/oracle@localhost/XE as SYSDBA <<EOF
@assets/create_user.sql
quit
EOF

echo "Configuração completada!"

exit 0
