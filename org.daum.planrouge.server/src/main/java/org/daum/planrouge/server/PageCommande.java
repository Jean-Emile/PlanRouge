package org.daum.planrouge.server;

import org.daum.planrouge.server.cache.MemCache;
import org.kevoree.annotation.*;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 19/09/13
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "PlanRouge")
@ComponentType
public class PageCommande extends AbstractPage {

    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse) {
        String page = new String(MemCache.getRessource("index.html"));
        kevoreeHttpResponse.setContent(page);
        return kevoreeHttpResponse;
    }


}
