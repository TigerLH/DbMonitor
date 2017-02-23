package com.beecloud.monitor;

import com.beecloud.entity.DbConfig;
import com.beecloud.util.YmlReader;
import org.assertj.db.type.*;

import java.util.List;

/**
 * @author hong.lin
 * @description
 * @date 2017/2/22.
 */
public class DbMonitor {
    private Changes changes = null;
    private String configPath;
    public DbMonitor(String configPath){
         this.configPath = configPath;
    }


    /**
     * 开启监控
     */
    public void startMonitor() throws Exception {
        DbConfig dbConfig = YmlReader.getDbConfig(configPath);
        Source source = new Source(dbConfig.getUrl(),dbConfig.getUserName(),dbConfig.getPassWord());
        changes = new Changes(source);
        changes.setStartPointNow();
        System.out.println("Monitor is Running for :"+dbConfig.getUrl());
    }

    /**
     * 关闭监控&&显示变化
     */
    public void endMonitor(){
        changes.setEndPointNow();
        this.showChanges();
    }


    /**
     * 显示数据变化
     */
    private void showChanges(){
        List<Change> changeList = changes.getChangesList();
        if(changeList.size()==0){
            System.out.println("there has no changes");
            return;
        }
        for(Change change:changeList){
            ChangeType type = change.getChangeType();
            String tableName = change.getDataName();
            if(ChangeType.CREATION.equals(type)){
                String insertValue = "";
                List<Value> list = change.getRowAtEndPoint().getValuesList();
                for(Value value : list){
                    insertValue = insertValue+value.getColumnName()+"="+value.getValue()+",";
                }
                System.out.println(tableName+" 中插入数据:"+insertValue.substring(0,insertValue.length()-1));
                System.out.println("\n");
            }else if(ChangeType.DELETION.equals(type)){
                List<Value> valuesList = change.getRowAtStartPoint().getValuesList();
                String delValue = "";
                for(Value value : valuesList){
                    delValue = delValue+value.getColumnName()+"="+value.getValue()+",";
                }
                System.out.println(tableName+" 中删除数据:"+delValue.substring(0,delValue.length()-1));
                System.out.println("\n");
            }else if(ChangeType.MODIFICATION.equals(type)){
                List<Value> valuesList_start = change.getRowAtStartPoint().getValuesList();
                List<Value> valuesList_end = change.getRowAtEndPoint().getValuesList();
                int size = valuesList_start.size();
                String modify = "";
                for(int i=0;i<size;i++){
                    Value start = valuesList_start.get(i);
                    Value end = valuesList_end.get(i);
                    if((null == start.getValue())&&(null == end.getValue())){
                        continue;
                    }
                    if(null != start.getValue()&&(null == end.getValue())){
                        modify = modify+start.getColumnName()+" From "+start.getValue()+" To "+"null"+"\n";
                        continue;
                    }
                    if(null == start.getValue()&&(null != end.getValue())){
                        modify = modify+start.getColumnName()+" From "+"null"+" To "+end.getValue()+"\n";
                        continue;
                    }
                    if(!start.getValue().equals(end.getValue())){
                        modify = modify+start.getColumnName()+" From "+start.getValue()+" To "+end.getValue()+"\n";
                    }
                }
                System.out.println(tableName+"中更新数据:"+modify+" Where Id="+valuesList_start.get(0).getValue());
                System.out.println("\n");
            }
        }
    }

}
