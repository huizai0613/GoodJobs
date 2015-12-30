package cn.goodjobs.common;

import java.util.ArrayList;

/**
 * Created by YeXiangYu on 15-9-10.
 */
public class AndroidBUSBean
{
    public static final int STATUSREFRESH = 0;
    public static final int STATUSUPDATE = 1;
    public static final int STATUSDELETE = 2;
    public static final int STATUSFINSH = 3;
    public static final int STATUSDATA = 4;

    private ArrayList entities;
    private Object object;
    private int status;


    public AndroidBUSBean(Object object, int status)
    {
        this.object = object;
        this.status = status;
    }

    public AndroidBUSBean(ArrayList entities, int status)
    {
        this.entities = entities;
        this.status = status;
    }

    public Object getObject()
    {
        return object;
    }

    public void setObject(Object object)
    {
        this.object = object;
    }

    public ArrayList getEntities()
    {
        return entities;
    }

    public void setEntities(ArrayList entities)
    {
        this.entities = entities;
    }

    public AndroidBUSBean(int status)
    {
        this.status = status;
    }


    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }
}
