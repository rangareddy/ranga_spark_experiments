#!/bin/bash

spark_apps_dir=$(pwd)
echo ""
echo "Spark Apps Directory: $spark_apps_dir"
echo ""

build_logs_dir=${spark_apps_dir}/log
rm -r -f "${build_logs_dir}"
mkdir -p "${build_logs_dir}"

for application in *; do
    if [ -d "$application" ]; then
        spark_app_dir=${spark_apps_dir}/${application}
        spark_app_pom_path=$spark_app_dir/pom.xml
        if [ -f "$spark_app_pom_path" ]; then
           spark_app_log=${build_logs_dir}/$application.log
           mvn clean package -DskipTests -U -f "$spark_app_pom_path" > "${spark_app_log}"
           build_success=$(cat "${spark_app_log}" | grep -i 'BUILD SUCCESS')
           if [ -z "$build_success" ]; then
      		echo ""	
               	echo "Build is failed for the application ${application} and log location is ${spark_app_log}"
           else 
                rm -r -f "${spark_app_log}"
	   fi 
        fi
    fi
done

echo ""
