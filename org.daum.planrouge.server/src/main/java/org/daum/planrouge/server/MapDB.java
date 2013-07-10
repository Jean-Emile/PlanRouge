package org.daum.planrouge.server;
import org.mapdb.*;

import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;


/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 09/07/13
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
public class MapDB {

    private DB db;
    private ConcurrentNavigableMap<String,String> map;

    public MapDB(String name, String password, String collectionName){


        // configure and open database using builder pattern.
        // all options are available with code auto-completion.
        db = DBMaker.newFileDB(new File(name))
                .closeOnJvmShutdown()
//                .encryptionEnable(password)
                .make();

        // open existing an collection (or create new)
        map = db.getTreeMap(collectionName);

    }

    public ConcurrentNavigableMap<String,String> getMap(){
        return map;
    }
    public DB getDB(){
        return db;
    }
    public void addInMap(String key, Object object){
        map.put(key,object.toString());
    }

    public void commit(){
        db.commit();        //persist changes into disk
    }

    public void rollback(){
        db.rollback();          //revert recent changes
    }

    public void close(){
        db.close();
    }


}
