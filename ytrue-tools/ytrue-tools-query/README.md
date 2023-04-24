# ytrue-tools-query的使用

## 方式1
```java
    @PostMapping("page")
    public ApiResultResponse<IPage<SysLog>> page(@RequestBody(required = false) PageQueryEntity pageQueryEntity) {
        IPage<SysLog> page = sysLogService.paginate(pageQueryEntity);
        return ApiResultResponse.success(page);
    }
```
```java
 /**
     * 分页
     *
     * @param pageQueryEntity
     * @return
     */
    @Override
    public IPage<T> paginate(PageQueryEntity pageQueryEntity) {
        pageQueryEntity = Objects.isNull(pageQueryEntity) ? new PageQueryEntity() : pageQueryEntity;
        return page(pageQueryEntity.page(), pageQueryEntity.lambdaQueryWrapperBuilder());
    }
```
前端请求
```text
http://127.0.0.1:7878/dev-api/sys/log/page
参数
{
    "page": 1,
    "filters": [
        {
            "column": "operator",
            "condition": "like",
            "value": "admin"
        },
        {
            "column": "httpMethod",
            "condition": "eq",
            "value": null
        },
        {
            "column": "consumingTime",
            "condition": "eq",
            "value": null
        },
        {
            "column": "requestIp",
            "condition": "eq",
            "value": "0:0:0:0:0:0:0:1"
        },
        {
            "column": "requestUri",
            "condition": "eq",
            "value": null
        },
        {
            "column": "type",
            "condition": "eq",
            "value": "OPT"
        },
        {
            "column": "startTime",
            "condition": "between",
            "value": [
                "2022-12-12",
                "2023-01-21"
            ]
        }
    ],
    "limit": 10,
    "sorts": [
        {
            "column": "startTime",
            "asc": false
        }
    ]
}
```
生成对应的sql
```text
DEBUG 10116 --- [nio-7000-exec-4] c.y.m.s.d.SysLogDao.selectPage_mpCount   : ==>  Preparing: SELECT COUNT(*) AS total FROM sys_log WHERE (operator LIKE ? AND request_ip = ? AND type = ? AND start_time BETWEEN ? AND ?)
DEBUG 10116 --- [nio-7000-exec-4] c.y.m.s.d.SysLogDao.selectPage_mpCount   : ==> Parameters: %admin%(String), 0:0:0:0:0:0:0:1(String), OPT(String), 2022-12-12(String), 2023-01-21(String)
DEBUG 10116 --- [nio-7000-exec-4] c.y.m.s.d.SysLogDao.selectPage_mpCount   : <==      Total: 1
DEBUG 10116 --- [nio-7000-exec-4] c.y.m.system.dao.SysLogDao.selectPage    : ==>  Preparing: SELECT id,request_ip,type,description,operator,class_path,action_method,request_uri,http_method,params,result,ex_desc,ex_detail,start_time,end_time,consuming_time,browser FROM sys_log WHERE (operator LIKE ? AND request_ip = ? AND type = ? AND start_time BETWEEN ? AND ?) ORDER BY start_time DESC,start_time DESC LIMIT ?
DEBUG 10116 --- [nio-7000-exec-4] c.y.m.system.dao.SysLogDao.selectPage    : ==> Parameters: %admin%(String), 0:0:0:0:0:0:0:1(String), OPT(String), 2022-12-12(String), 2023-01-21(String), 10(Long)
DEBUG 10116 --- [nio-7000-exec-4] c.y.m.system.dao.SysLogDao.selectPage    : <==      Total: 10
```

```java
    @PostMapping("list")
    public ApiResultResponse<List<SysLog>> list(@RequestBody(required = false) QueryEntity queryEntity) {
        IPage<SysLog> page = sysLogService.paginate(pageQueryEntity);
        return ApiResultResponse.success(page);
    }
```

```java
   /**
     * 列表
     *
     * @param queryEntity
     * @return
     */
    @Override
    public List<T> list(QueryEntity queryEntity) {
        queryEntity = Objects.isNull(queryEntity) ? new QueryEntity() : queryEntity;
        return list(queryEntity.lambdaQueryWrapperBuilder());
    }

```
QueryEntity和PageQueryEntity请求参数基本一致，就是不需要page和limit

## 方式2
```java
    @GetMapping("page")
    public ApiResultResponse<IPage<SysJob>> page(SysJobSearchParams params, Pageable pageable) {

        LambdaQueryWrapper<SysJob> queryWrapper = QueryHelp.<SysJob>lambdaQueryWrapperBuilder(params)
                .orderByAsc(SysJob::getJobSort)
                .orderByDesc(SysJob::getId);

        return ApiResultResponse.success(sysJobService.page(pageable.page(), queryWrapper));
    }
```
```java
@Data
public class SysJobSearchParams implements Serializable {

    private static final long serialVersionUID = -8023936370399354477L;

    @Query(condition = QueryMethod.like)
    @Schema(title = "岗位名称")
    private String jobName;

    @Query
    @Schema(title = "是否启用")
    private Boolean status;

    @Query(condition = QueryMethod.between)
    @Schema(title = "创建时间")
    private List<String> createTime;
}
```
前端请求
```java
http://127.0.0.1:7878/dev-api/sys/job/page?jobName=java%E5%BC%80%E5%8F%91&status=true&createTime%5B0%5D=2022-12-04&createTime%5B1%5D=2023-01-04&page=1&limit=10
```
生成对应的sql
```text
DEBUG 10116 --- [nio-7000-exec-1] c.y.m.system.dao.SysJobDao.selectPage    : ==>  Preparing: SELECT id,job_name,job_sort,status,create_by,update_by,create_time,update_time FROM sys_job WHERE (job_name LIKE ? AND status = ? AND create_time BETWEEN ? AND ?) ORDER BY job_sort ASC,id DESC LIMIT ?
DEBUG 10116 --- [nio-7000-exec-1] c.y.m.system.dao.SysJobDao.selectPage    : ==> Parameters: %java开发%(String), true(Boolean), 2022-12-04(String), 2023-01-04(String), 10(Long)
```
## 方式三
```java
    @GetMapping("page")
    @Operation(summary="分页")
    @PreAuthorize("@pms.hasPermission('system:user:page')")
    public ApiResultResponse<IPage<SysUserListVO>> page(SysUserSearchParams params, Pageable pageable) {

        QueryEntity queryEntity = QueryHelp
                .queryEntityBuilder(params)
                .addSort(SysUserListVO::getId, false);

        return ApiResultResponse.success(sysUserService.paginate(pageable.page(), queryEntity));
    }
```
```java
@Data
public class SysUserSearchParams implements Serializable {
    private static final long serialVersionUID = -1210636061661488366L;

    @Query(condition = QueryMethod.like, alias = "u")
    @Schema(title = "用户名称")
    private String username;

    @Query(condition = QueryMethod.like, alias = "u")
    @Schema(title = "电话号码")
    private String phone;

    @Query(condition = QueryMethod.eq, alias = "u")
    @Schema(title = "是否启用")
    private Boolean status;

    @Query(condition = QueryMethod.between, alias = "u")
    @Schema(title = "创建时间")
    private List<String> createTime;
}

```
```java
    @Override
    public IPage<SysUserListVO> paginate(IPage<SysUserListVO> page, QueryEntity query) {
        return sysUserDao.listWithDeptName(page, query);
    }
```
```java
     /**
     * 列表查询
     *
     * @param page
     * @param queryEntity
     * @return
     */
    IPage<SysUserListVO> listWithDeptName(IPage<SysUserListVO> page, QueryEntity queryEntity);
```
```xml
    <!--列表查询-->
    <select id="listWithDeptName" resultType="com.ytrue.modules.system.model.vo.SysUserListVO">
        SELECT u.id,
               d.dept_name,
               u.username,
               u.nick_name,
               u.email,
               u.phone,
               u.gender,
               u.avatar_path,
               u.`status`,
               u.create_time
        FROM sys_user u
                 LEFT JOIN sys_dept d ON u.dept_id = d.id
    </select>
```
前端请求
```text
http://127.0.0.1:7878/dev-api/sys/user/page?username=test_admin&phone=17638728398&createTime%5B0%5D=2022-12-14&createTime%5B1%5D=2023-01-19&page=1&limit=10
```
生成对应的sql
```text
DEBUG 10116 --- [nio-7000-exec-7] c.y.m.s.d.S.listWithDeptName_mpCount     : ==>  Preparing: SELECT COUNT(*) AS total FROM sys_user u WHERE u.username LIKE '%test_admin%' AND u.phone LIKE '%17638728398%' AND u.create_time BETWEEN '2022-12-14' AND '2023-01-19' ORDER BY id DESC
DEBUG 10116 --- [nio-7000-exec-7] c.y.m.s.d.S.listWithDeptName_mpCount     : ==> Parameters: 
DEBUG 10116 --- [nio-7000-exec-7] c.y.m.s.d.S.listWithDeptName_mpCount     : <==      Total: 1
DEBUG 10116 --- [nio-7000-exec-7] c.y.m.s.dao.SysUserDao.listWithDeptName  : ==>  Preparing: SELECT u.id, d.dept_name, u.username, u.nick_name, u.email, u.phone, u.gender, u.avatar_path, u.`status`, u.create_time FROM sys_user u LEFT JOIN sys_dept d ON u.dept_id = d.id WHERE u.username LIKE '%test_admin%' AND u.phone LIKE '%17638728398%' AND u.create_time BETWEEN '2022-12-14' AND '2023-01-19' ORDER BY id DESC LIMIT ?
DEBUG 10116 --- [nio-7000-exec-7] c.y.m.s.dao.SysUserDao.listWithDeptName  : ==> Parameters: 10(Long)
DEBUG 10116 --- [nio-7000-exec-7] c.y.m.s.dao.SysUserDao.listWithDeptName  : <==      Total: 1
```
## 其他功能
1. mybatis plus的 selectOne 自动加上 limit =1 对应源码文件 MpQueryLimitAspect.java
2. 更多完整使用自行看源码
