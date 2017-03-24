<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${daoIntfPackage}.${className}DAO">

    <resultMap type="${doPackage}.${className}DO" id="${doNameLower}ResultMap">
        <#list list as item>
        <result column="${item.columnName}" property="${item.propName}" />
        </#list>
        <result property="gmtCreate" column="gmt_create"  />
        <result property="gmtUpdate" column="gmt_update"  />
    </resultMap>


    <sql id="${tableName}_columns">
    <#list list as l><#if l_index == 0>${l.columnName}</#if></#list>,${colsWithoutCommColumns},gmt_create,gmt_update
    </sql>
    <sql id="${tableName}_selectField">
        select <#list list as l><#if l_index == 0>${l.columnName}</#if></#list>,${colsWithoutCommColumns},gmt_create,gmt_update
        from ${tableNamePrefix}${tableName}
    </sql>

    <insert id="insert" parameterType="${doNameLower}DO">
        INSERT INTO ${tableNamePrefix}${tableName}(${colsWithoutCommColumns},gmt_create,gmt_update)
        VALUES (<#list list as item><#if item_index gt 0>${r'#{'}${item.propName}${r'}'}, </#if></#list> now(), now())
        <selectKey  resultType="java.lang.Long"  keyProperty="${colId}">
            select LAST_INSERT_ID()
        </selectKey>
    </insert>

    <select id="selectById" resultMap="${doNameLower}ResultMap" parameterClass="java.lang.Long">
        select  <include refid="${tableName}_columns"/>
        from ${tableNamePrefix}${tableName} where  <#list list as l><#if l_index == 0>${l.columnName}=${r'#{'}${l.propName}${r'}'}</#if></#list>
    </select>


    <delete id="delById">
        delete from ${tableNamePrefix}${tableName}  where  <#list list as l><#if l_index == 0>${l.columnName}=${r'#{'}${l.propName}${r'}'}</#if></#list>
    </delete>

    <select id="selectByIds" resultMap="${doNameLower}ResultMap">
        <include refid="${tableName}.selectField"/>
        where  <#list list as l><#if l_index == 0>${l.columnName}</#if></#list> in
        <foreach collection="idsList" item="classIds" open="(" separator="," close=")">
        ${r'#{'}classIds[]${r'}'}
        </foreach>
    </select>

    <select id="selectForPage" resultMap="${doNameLower}ResultMap" >
        <include refid="${tableName}_selectField"/>
        limit ${r'#{'}startRow${r'}'},${r'#{'}pageSize${r'}'}
    </select>

    <select id="countForPage" resultClass="java.lang.Integer"  >
        select count(*) from ${tableNamePrefix}${tableName}
    </select>

    <update id="updateByIdSelective" parameterType="${doNameLower}DO">
        update ${tableNamePrefix}${tableName} set gmt_update=now()
        <#list list as item >
            <#if item_index gt 0>
            <if test="${item.propName} != null and ${item.propName}!= ''" >
                ,${item.columnName}=${r'#{'}${item.propName}${r'}'}
            </if>
            </#if>
        </#list>
        where  <#list list as l><#if l_index == 0>${l.columnName}=${r'#{'}${l.propName}${r'}'}</#if></#list>
    </update>



</mapper>