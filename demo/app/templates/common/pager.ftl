<#macro pager pageQuery baseUrl paramString>

    <#if pageQuery.getMaxPage() gt 1>
    <nav class="pagination">

        <#if pageQuery.pageNo gt 1>
            <a class="pagination-previous" href="${baseUrl!}?pageNo=1&${paramString!}">首页</a>
            <a class="pagination-previous" href="${baseUrl!}?pageNo=${pageQuery.pageNo - 1 }&${paramString!}">上一页</a>
        <#else>
            <a class="pagination-previous is-disabled">首页</a>
        </#if>
        <#if pageQuery.hasNextPage()>
            <a class="pagination-next" href="${baseUrl!}?pageNo=${pageQuery.pageNo + 1 }&${paramString!}">下一页</a>
            <a class="pagination-next" href="${baseUrl!}?pageNo=${pageQuery.getMaxPage()}&${paramString!}">末页</a>
        <#else>
            <a class="pagination-next  is-disabled">末页</a>
        </#if>
        <ul class="pagination-list">
            <#list pageQuery.pages as p>
                <#if p == pageQuery.pageNo >
                    <li>
                        <a class="pagination-link is-current">${p}</a>
                    </li>
                <#else>
                    <li>
                        <a class="pagination-link" href="${baseUrl!}?pageNo=${p}&${paramString!}">${p}</a>
                    </li>
                </#if>
            </#list>
        </ul>


    </nav>
    </#if>
</#macro>