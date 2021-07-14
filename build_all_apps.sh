
spark_apps_dir=$(pwd)
echo "$spark_apps_dir"
for f in *; do
    if [ -d "$f" ]; then
        spark_app_dir=${spark_apps_dir}/$f
        echo "$spark_app_dir"
        spark_app_pom_path=$spark_app_dir/pom.xml
        if [ -f $spark_app_pom_path ]; then
           mvn clean package -DskipTests -f $spark_app_pom_path 
        fi
    fi
done
