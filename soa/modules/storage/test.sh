#!/bin/sh
mvn -f sso-parent clean package && 
mvn -f sso-dao clean package && 
mvn -f sso-entity clean package && 
mvn -f sso-session clean package && 
mvn -f sso-core clean package && 
mvn -f sso-web clean package && 
mvn -f sso-utils clean package && 
mvn -f sso-service clean package && 
echo "================ 测试完成 ============="

