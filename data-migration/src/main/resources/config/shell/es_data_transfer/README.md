1. 在所有es机器上执行脚本elasticsearch_data_transfer_prepare.sh，
备份数据目录和新建临时索引，临时索引以_temp结尾。（注意一定要用bash执行脚本）

2. 使用java程序将es数据转移到临时索引中(临时索引以_temp结尾)

3. 随便找一台es机器执行脚本elasticsearch_data_transfer.sh，该脚本将临时索引中数据导入正式索引中。

4. 确认数据无误后，在所有es机器上执行脚本elasticsearch_data_delete_backup_data.sh，
删除临时索引和备份数据。（注意一定要用bash执行脚本）