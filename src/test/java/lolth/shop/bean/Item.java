package lolth.shop.bean;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

public class Item
{
    private String id;
    private String classification; // 类别
    private String name; // 名称
    private String price = ""; // 价格
    private String createTime = DateFormatUtils.format(new Date(), "yyyyMMdd"); // 创建时间
    private String domain;

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(id);
        sb.append("\t");
        sb.append(classification);
        sb.append("\t");
        sb.append(name);
        sb.append("\t");
        sb.append(price);
        sb.append("\t");
        sb.append(createTime);
        sb.append("\t");
        sb.append(domain);

        return sb.toString();
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getClassification()
    {
        return classification;
    }

    public void setClassification(String classification)
    {
        this.classification = classification;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getDomain()
    {
        return domain;
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
    }

}
