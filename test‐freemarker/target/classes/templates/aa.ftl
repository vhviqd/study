<!DOCTYPE html>
<html lang="en">
<head>
    <title> hello ${aa}</title>
</head>
<body>
    <img src="../img/aa.png">
    <#if model??>
        <#list model as item>
                ${item.value}
        </#list>
    </#if>
</body>
</html>