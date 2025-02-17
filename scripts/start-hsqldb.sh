#!/bin/bash

# 获取项目根目录
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

# 设置数据库目录
DB_DIR="$PROJECT_DIR/data/hsqldb"

# 创建数据库目录
mkdir -p "$DB_DIR"

# 启动 HSQLDB
java -cp "$PROJECT_DIR/target/classes:$HOME/.m2/repository/org/hsqldb/hsqldb/2.7.4/hsqldb-2.7.4.jar" \
    org.hsqldb.Server \
    --database.0 "file:$DB_DIR/testdb" \
    --dbname.0 testdb