<#macro pager pageQuery baseUrl paramString>

    <#if pageQuery.getMaxPage() gt 1>
    <nav class="pagination">

        <#if pageQuery.page gt 1>
            <a class="pagination-previous" href="${baseUrl!}?page=1&${paramString!}">首页</a>
            <a class="pagination-previous" href="${baseUrl!}?page=${pageQuery.page - 1 }&${paramString!}">上一页</a>
        <#else>
            <a class="pagination-previous is-disabled">首页</a>
        </#if>
        <#if pageQuery.hasNextPage()>
            <a class="pagination-next" href="${baseUrl!}?page=${pageQuery.page + 1 }&${paramString!}">下一页</a>
            <a class="pagination-next" href="${baseUrl!}?page=${pageQuery.getMaxPage()}&${paramString!}">末页</a>
        <#else>
            <a class="pagination-next  is-disabled">末页</a>
        </#if>
        <ul class="pagination-list">
            <#list pageQuery.pages as p>
                <#if p == pageQuery.page >
                    <li>
                        <a class="pagination-link is-current">${p}</a>
                    </li>
                <#else>
                    <li>
                        <a class="pagination-link" href="${baseUrl!}?page=${p}&${paramString!}">${p}</a>
                    </li>
                </#if>
            </#list>
        </ul>


    </nav>
    </#if>
</#macro>