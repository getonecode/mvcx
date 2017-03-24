package guda.mvcx.demo.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import guda.mvcx.core.annotation.action.Action;
import guda.mvcx.core.annotation.action.Req;
import guda.mvcx.core.annotation.action.ReqParam;
import guda.mvcx.core.annotation.action.View;
import guda.mvcx.core.enums.ViewTypeEnum;
import guda.mvcx.core.helper.BizResult;
import guda.mvcx.core.helper.JsonResult;
import guda.mvcx.core.helper.PageQuery;
import guda.mvcx.demo.biz.BlogBiz;
import guda.mvcx.demo.dao.model.BlogDO;
import guda.mvcx.demo.form.BlogEditForm;
import guda.mvcx.demo.form.BlogForm;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by well on 2017/3/24.
 */
@Action
@Req(value = "/blog")
@Singleton
public class BlogAction {

    @Inject
    private BlogBiz blogBiz;

    @Req(value = "/index", method = HttpMethod.GET)
    public String index(RoutingContext context, PageQuery pageQuery) {
        pageQuery.setTotalCount(100);
        BizResult list = blogBiz.list(pageQuery);
        context.data().putAll(list.data);
        return "blog/index.ftl";
    }

    @Req(value = "/create", method = HttpMethod.GET)
    public String create() {
        return "blog/create.ftl";
    }

    @Req(value = "/doCreate", method = HttpMethod.POST)
    public String doCreate(BlogForm blogForm) {
        if (blogForm.validate()) {
            return "blog/create.ftl";
        }
        BizResult bizResult = blogBiz.create(blogForm.toDO());
        if (bizResult.success) {
            return "redirect:/blog/index";
        } else {
            return "error.ftl";
        }

    }

    @Req(value = "/edit", method = HttpMethod.GET)
    public String edit(@ReqParam("blogId") Long blogId,BlogEditForm blogEditForm) {
        BizResult detail = blogBiz.detail(blogId);
        blogEditForm.init((BlogDO)detail.data.get("blogDO"));
        return "blog/edit.ftl";
    }

    @Req(value = "/doUpdate", method = HttpMethod.POST)
    public String doUpdate(BlogEditForm blogEditForm) {
        if (blogEditForm.validate()) {
            return "blog/edit.ftl";
        }
        BizResult bizResult = blogBiz.update(blogEditForm.toDO());
        if (bizResult.success) {
            return "redirect:/blog/index";
        } else {
            return "error.ftl";
        }

    }

    @Req(value = "/doDel", method = HttpMethod.POST)
    @View(type = ViewTypeEnum.json)
    public JsonResult doDel(@ReqParam("blogId") Long blogId) {
        JsonResult jsonResult = JsonResult.getInstance();
        if (blogId == null) {
            jsonResult.setMsg("参数不能为空");
            return jsonResult;
        }
        BizResult bizResult = blogBiz.delete(blogId);
        if (bizResult.success) {
            jsonResult.setSuccess(true);
            return jsonResult;
        } else {
            jsonResult.setMsg(bizResult.msg);
            return jsonResult;
        }


    }
}
