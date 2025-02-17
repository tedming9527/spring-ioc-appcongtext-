#!/bin/bash

# 启动 HSQLDB（内存模式）
java -cp "$HOME/.m2/repository/org/hsqldb/hsqldb/2.7.4/hsqldb-2.7.4.jar" \
    org.hsqldb.Server \
    --database.0 "mem:testdb" \
    --dbname.0 testdb