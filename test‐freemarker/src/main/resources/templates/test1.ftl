<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hello World!</title>

</head>
<body>
<#--模型-->
    Hello ${name} !

    <table>
        <tr>
            <td>序号</td>
            <td>姓名</td>
            <td>年龄</td>
            <td>钱包</td>
        </tr>
        <#list stus as stu>
            <tr>
            <td>${stu_index + 1}</td>
            <td>${stu.name}</td>
            <td>${stu.age}</td>
           <#-- <td>${stu.mondy}</td>-->
            </tr>
        </#list>
        <br/>
        ---------------------------
        输出stu1和stu2的学生信息：<br/>
        姓名：${stuMap['stu1'].name}<br/>
        年龄：${stuMap['stu1'].age}<br/>
        姓名：${stuMap['stu2'].name}<br/>
        年龄：${stuMap['stu2'].age}<br/>
    </table>
</body>
</html>