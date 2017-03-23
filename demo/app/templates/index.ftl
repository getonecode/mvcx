<html>
<header>
<#include "common/head.ftl">
</header>
<body>
<#include "common/header.ftl">
<div class="container">
    index

<#include "common/pager.ftl">
<@pager pageQuery=context._query baseUrl="/index" paramString="abc=123"/>
</div>
</body>
</html>



