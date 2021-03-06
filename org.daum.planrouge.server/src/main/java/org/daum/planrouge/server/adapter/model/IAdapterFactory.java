package org.daum.planrouge.server.adapter.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.planrouge.container.KMFContainer;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 15/07/13
 * Time: 11:11
 * To change this template use File | Settings | File Templates.
 */
public interface IAdapterFactory {
    public JSONObject build(KMFContainer container) throws JSONException;
    public  <T> T build(JSONObject json) throws JSONException;
}
